package cc.kertaskerja.integration.penetapan;

import cc.kertaskerja.integration.penetapan.renaksi.PenetapanRenaksiOpd;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PenetapanRenaksiOpdClient {

    private static final Logger log = LoggerFactory.getLogger(PenetapanRenaksiOpdClient.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public PenetapanRenaksiOpdClient(
            WebClient penetapanWebClient,
            ObjectMapper objectMapper
    ) {
        this.webClient = penetapanWebClient;
        this.objectMapper = objectMapper;
    }

    public Mono<PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot> fetchRenaksiOpd(String kodeOpd, int tahun) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/opd/renaksi")
                        .queryParam("kodeOpd", kodeOpd)
                        .queryParam("tahun", tahun)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseRenaksiOpdPayload)
                .onErrorResume(e -> {
                    log.warn("Failed to fetch penetapan renaksi OPD for kodeOpd={}, tahun={}", kodeOpd, tahun, e);
                    return Mono.empty();
                });
    }

    public Mono<String> syncRenaksiOpd(String kodeOpd, int tahun) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/opd/renaksi/sync")
                        .queryParam("kodeOpd", kodeOpd)
                        .queryParam("tahun", tahun)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    log.error("Failed to sync penetapan renaksi OPD for kodeOpd={}, tahun={}", kodeOpd, tahun, e);
                    return Mono.empty();
                });
    }

    private PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot parseRenaksiOpdPayload(String payload) {
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode dataNode = rootNode;
            if (rootNode != null && rootNode.isObject() && rootNode.has("data")) {
                dataNode = rootNode.get("data");
            }

            return objectMapper.treeToValue(dataNode, PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot.class);
        } catch (Exception e) {
            log.warn("Failed to parse penetapan renaksi OPD payload", e);
            return null;
        }
    }
}
