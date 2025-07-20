package cc.kertaskerja.realisasi_pemda_service.sasaran.web;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.Sasaran;
import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.SasaranService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@WebFluxTest(SasaranController.class)
public class SasaranControllerWebFluxTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SasaranService sasaranService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenBatchSubmit_thenReturnSavedSasarans() throws Exception {
        // prepare data
        SasaranRequest s1 = new SasaranRequest("S-1", "IS-1", "TIS-1",
                "10", 10.0, "%", "2025", JenisRealisasi.NAIK);
        SasaranRequest s2 = new SasaranRequest("S-12", "IS-12", "TIS-12",
                "10", 5.0, "%", "2025", JenisRealisasi.NAIK);

        Sasaran ss1 = SasaranService.buildUnchekcedRealisasiSasaran(
                s1.sasaranId(), s1.indikatorId(), s1.targetId(),
                s1.target(), s1.realisasi(), s1.satuan(), s1.tahun(), s1.jenisRealisasi()
        );

        Sasaran ss2 = SasaranService.buildUnchekcedRealisasiSasaran(
                s2.sasaranId(), s2.indikatorId(), s2.targetId(),
                s2.target(), s2.realisasi(), s2.satuan(), s1.tahun(), s2.jenisRealisasi()
        );

        when(sasaranService.batchSubmitRealisasiSasaran(anyList()))
                .thenReturn(Flux.just(ss1, ss2));

        // execute
        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
                .post()
                .uri("/sasarans/batch")
                .header("Content-Type", "application/json")
                .bodyValue(objectMapper.writeValueAsString(List.of(ss1, ss2)))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Sasaran.class)
                .consumeWith(response -> {
                    var body = response.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals(2, body.size());
                    Assertions.assertEquals(ss1, body.get(0));
                    Assertions.assertEquals(ss2, body.get(1));
                });

    }
}
