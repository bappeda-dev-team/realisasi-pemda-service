package cc.kertaskerja.integration.penetapan;

import cc.kertaskerja.integration.penetapan.sasaran_opd.PenetapanSasaranOpd;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class PenetapanSasaranOpdClient {

    private static final Logger log = LoggerFactory.getLogger(PenetapanTujuanOpdClient.class);
    private final WebClient webClient;
    private final PenetapanProperties properties;
    private final ObjectMapper objectMapper;

    public PenetapanSasaranOpdClient(
            WebClient penetapanWebClient,
            PenetapanProperties properties,
            ObjectMapper objectMapper
    ) {
        this.webClient = penetapanWebClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public Mono<List<PenetapanSasaranOpd.SasaranPenetapanData>> fetchSasaranOpd(String kodeOpd, int tahun) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/opd/sasaran")
                        .queryParam("kodeOpd", kodeOpd)
                        .queryParam("tahun", tahun)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseSasaranOpdPayload)
                .onErrorResume(e -> {
                    log.warn("Failed to fetch penetapan sasaran OPD for kodeOpd={}, tahun={}: {}",
                            kodeOpd, tahun, e.getMessage());
                    return Mono.just(List.of());
                });
    }

    private List<PenetapanSasaranOpd.SasaranPenetapanData> parseSasaranOpdPayload(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode dataNode = root;
            if (root != null && root.isObject() && root.has("data")) {
                dataNode = root.get("data");
            }

            if (dataNode == null || !dataNode.isArray()) {
                return List.of();
            }

            PenetapanSasaranOpd.SasaranPenetapanData[] rows = objectMapper.treeToValue(dataNode, PenetapanSasaranOpd.SasaranPenetapanData[].class);
            return Arrays.asList(rows);
        } catch (Exception e) {
            log.warn("Failed to parse penetapan sasaran OPD payload: {}", e.getMessage());
            return List.of();
        }
    }
}
