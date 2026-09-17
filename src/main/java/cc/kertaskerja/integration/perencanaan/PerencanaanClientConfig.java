package cc.kertaskerja.integration.perencanaan;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class PerencanaanClientConfig {

    @Bean("perencanaanWebClient")
    public WebClient perencanaanWebClient(
            PerencanaanProperties properties,
            PerencanaanTokenProvider tokenProvider
    ) {
        var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        (int) properties.connectTimeout().toMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(
                                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(
                                properties.readTimeout().toMillis(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(properties.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(sessionAuthFilter(tokenProvider))
                .build();
    }

    private ExchangeFilterFunction sessionAuthFilter(PerencanaanTokenProvider tokenProvider) {
        return (request, next) -> tokenProvider.getToken()
                .flatMap(token -> next.exchange(ClientRequest.from(request)
                        .headers(headers -> headers.set("X-Session-Id", token))
                        .build()));
    }
}
