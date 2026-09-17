package cc.kertaskerja.integration.perencanaan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PerencanaanTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(PerencanaanTokenProvider.class);
    private static final Duration DEFAULT_TOKEN_TTL = Duration.ofMinutes(30);

    private final WebClient webClient;
    private final PerencanaanProperties properties;
    private final ObjectMapper objectMapper;

    private volatile String cachedToken;
    private volatile Instant expiresAt;
    private volatile Mono<String> inFlight;

    public PerencanaanTokenProvider(PerencanaanProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.webClient = buildAuthWebClient(properties);
    }

    public Mono<String> getToken() {
        if (isTokenUsable()) {
            return Mono.just(cachedToken);
        }
        synchronized (this) {
            if (isTokenUsable()) {
                return Mono.just(cachedToken);
            }
            if (inFlight != null) {
                return inFlight;
            }
            Mono<String> fresh = login()
                    .doOnError(err -> {
                        synchronized (this) {
                            inFlight = null;
                        }
                    })
                    .cache();
            inFlight = fresh;
            return fresh;
        }
    }

    private boolean isTokenUsable() {
        return cachedToken != null && !cachedToken.isBlank()
                && expiresAt != null && expiresAt.isAfter(Instant.now());
    }

    private Mono<String> login() {
        return webClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of(
                        "username", properties.username(),
                        "password", properties.password())))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseToken)
                .doOnNext(token -> log.debug("Successfully obtained perencanaan auth token"));
    }

    private String parseToken(String payload) {
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode dataNode = (rootNode != null && rootNode.isObject() && rootNode.has("data"))
                    ? rootNode.get("data")
                    : rootNode;

            String token = findToken(dataNode);
            if (token == null || token.isBlank()) {
                log.warn("Unable to find token in perencanaan login response: {}", payload);
                throw new IllegalStateException("Perencanaan login response contained no token");
            }

            expiresAt = resolveExpiry(rootNode, dataNode);
            cachedToken = token;
            return token;
        } catch (Exception e) {
            log.warn("Failed to parse perencanaan login response: {}", payload, e);
            throw new IllegalStateException("Failed to parse perencanaan login response", e);
        }
    }

    private String findToken(JsonNode node) {
        if (node == null || !node.isObject()) {
            return null;
        }
        for (String field : new String[]{"sessionId", "session_id", "token", "access_token", "accessToken", "jwt", "id_token"}) {
            JsonNode value = node.get(field);
            if (value != null && value.isTextual() && !value.asText().isBlank()) {
                return value.asText();
            }
        }
        JsonNode nested = node.get("token");
        if (nested != null && nested.isObject()) {
            return findToken(nested);
        }
        return null;
    }

    private Instant resolveExpiry(JsonNode rootNode, JsonNode dataNode) {
        Long expiresIn = findExpiresIn(rootNode);
        if (expiresIn == null) {
            expiresIn = findExpiresIn(dataNode);
        }

        Duration ttl;
        if (expiresIn == null || expiresIn <= 0) {
            ttl = DEFAULT_TOKEN_TTL;
        } else {
            Duration half = Duration.ofSeconds(expiresIn / 2);
            Duration buffer = Duration.ofSeconds(Math.min(30, half.toSeconds()));
            ttl = half.minus(buffer);
            if (ttl.toSeconds() <= 0) {
                ttl = DEFAULT_TOKEN_TTL;
            }
        }
        return Instant.now().plus(ttl);
    }

    private Long findExpiresIn(JsonNode node) {
        if (node == null || !node.isObject()) {
            return null;
        }
        for (String field : new String[]{"expires_in", "expiresIn"}) {
            JsonNode value = node.get(field);
            if (value != null && value.isNumber()) {
                return value.asLong();
            }
        }
        return null;
    }

    private WebClient buildAuthWebClient(PerencanaanProperties properties) {
        var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) properties.connectTimeout().toMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(
                                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(
                                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(properties.authBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}