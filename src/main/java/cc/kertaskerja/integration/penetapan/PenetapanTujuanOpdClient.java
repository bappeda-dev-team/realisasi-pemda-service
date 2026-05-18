package cc.kertaskerja.integration.penetapan;

import cc.kertaskerja.integration.penetapan.tujuan_opd.PenetapanTujuanOpd;
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
public class PenetapanTujuanOpdClient {

    private static final Logger log = LoggerFactory.getLogger(PenetapanTujuanOpdClient.class);
    private final WebClient webClient;
    private final PenetapanProperties properties;
    private final ObjectMapper objectMapper;

    public PenetapanTujuanOpdClient(
            WebClient penetapanWebClient,
            PenetapanProperties properties,
            ObjectMapper objectMapper
    ) {
        this.webClient = penetapanWebClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public Mono<List<PenetapanTujuanOpd.TujuanPenetapanData>> fetchTujuanOpd(String kodeOpd, int tahun) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/opd/tujuan")
                        .queryParam("kodeOpd", kodeOpd)
                        .queryParam("tahun", tahun)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseTujuanOpdPayload)
                .onErrorResume(e -> {
                    log.warn("Failed to fetch penetapan tujuan OPD for kodeOpd={}, tahun={}: {}",
                            kodeOpd, tahun, e.getMessage());
                    return Mono.just(List.of());
                });
    }

    private List<PenetapanTujuanOpd.TujuanPenetapanData> parseTujuanOpdPayload(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode dataNode = root;
            if (root != null && root.isObject() && root.has("data")) {
                dataNode = root.get("data");
            }

            if (dataNode == null || !dataNode.isArray()) {
                return List.of();
            }

            PenetapanTujuanOpd.TujuanPenetapanData[] rows = objectMapper.treeToValue(dataNode, PenetapanTujuanOpd.TujuanPenetapanData[].class);
            return Arrays.asList(rows);
        } catch (Exception e) {
            log.warn("Failed to parse penetapan tujuan OPD payload: {}", e.getMessage());
            return List.of();
        }
    }
}
