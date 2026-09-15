package cc.kertaskerja.integration.kepegawaian;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OpdClient {

    private static final Logger log = LoggerFactory.getLogger(OpdClient.class);
    private final WebClient webClient;

    public OpdClient(WebClient.Builder webClientBuilder,
                     @Value("${integration.kepegawaian.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public record OpdData(
            Integer id,
            @JsonProperty("kode_lembaga") String kodeLembaga,
            @JsonProperty("kode_opd") String kodeOpd,
            @JsonProperty("nama_opd") String namaOpd,
            @JsonProperty("singkatan_opd") String singkatanOpd,
            @JsonProperty("status_opd") String statusOpd
    ) {}

    public record OpdResponse(
            Integer code,
            String status,
            String message,
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) List<OpdData> data
    ) {}

    public Mono<List<OpdData>> fetchAllOpd() {
        return webClient.get()
                .uri("/opd/all")
                .retrieve()
                .bodyToMono(OpdResponse.class)
                .map(OpdResponse::data)
                .onErrorResume(e -> {
                    log.warn("Failed to fetch OPD list from kepegawaian service", e);
                    return Mono.empty();
                });
    }
}