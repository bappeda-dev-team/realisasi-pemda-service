package cc.kertaskerja.integration.perencanaan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cc.kertaskerja.integration.perencanaan.laporanrbtematik.LaporanRBTematik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class LaporanRBTematikClient {

    private static final Logger log = LoggerFactory.getLogger(LaporanRBTematikClient.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public LaporanRBTematikClient(
            WebClient perencanaanWebClient,
            ObjectMapper objectMapper
    ) {
        this.webClient = perencanaanWebClient;
        this.objectMapper = objectMapper;
    }

    public Mono<List<LaporanRBTematik.LaporanRBTematikData>> fetchLaporanByTahun(int tahun) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/datamaster/rb/laporanByTahun/{tahun}/TEMATIK")
                        .build(tahun))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseLaporanPayload)
                .onErrorResume(e -> {
                    log.warn("Failed to fetch laporan RB tematik for tahun={}", tahun, e);
                    return Mono.just(List.of());
                });
    }

    private List<LaporanRBTematik.LaporanRBTematikData> parseLaporanPayload(String payload) {
        try {
            JsonNode rootNode = objectMapper.readTree(payload);
            JsonNode dataNode = rootNode;
            if (rootNode != null && rootNode.isObject() && rootNode.has("data")) {
                dataNode = rootNode.get("data");
            }

            if (dataNode == null || !dataNode.isArray()) {
                log.warn("Laporan RB tematik data node is not an array");
                return List.of();
            }

            return Arrays.asList(objectMapper.treeToValue(dataNode, LaporanRBTematik.LaporanRBTematikData[].class));
        } catch (Exception e) {
            log.warn("Failed to parse laporan RB tematik payload", e);
            return List.of();
        }
    }
}