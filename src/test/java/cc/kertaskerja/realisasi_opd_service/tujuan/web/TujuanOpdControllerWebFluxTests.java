package cc.kertaskerja.realisasi_opd_service.tujuan.web;

import cc.kertaskerja.config.SecurityConfig;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.tujuan.domain.TujuanOpdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(TujuanOpdController.class)
@Import(SecurityConfig.class)
class TujuanOpdControllerWebFluxTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TujuanOpdService tujuanOpdService;

    @Test
    void whenLevel1GetsTujuanOpdByKodeOpdTahunBulan_thenMergedResponseReturned() {
        PenetapanTujuanOpdListResponse result = new PenetapanTujuanOpdListResponse(
                "5.01.5.05.0.00.01.0000",
                2026,
                3,
                List.of(new TujuanOpdPenetapanResponse(
                        12L,
                        "TUJ-OPD-193",
                        null,
                        List.of(new TujuanOpdPenetapanResponse.IndikatorPenetapan(
                                "IND-59",
                                null,
                                null,
                                null,
                                null,
                                List.of(new TujuanOpdPenetapanResponse.TargetPenetapan(
                                        "TGT-TRG-TJN-1bdac",
                                        null,
                                        null,
                                        80.0,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                ))
                        ))
                ))
        );

        when(tujuanOpdService.getPenetapanWithRealisasi("5.01.5.05.0.00.01.0000", 2026, "3"))
                .thenReturn(Mono.just(result));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("level_1")))
                .get()
                .uri("/tujuan_opd/5.01.5.05.0.00.01.0000/tahun/2026/penetapan?bulan=3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.kode_opd").isEqualTo("5.01.5.05.0.00.01.0000")
                .jsonPath("$.tahun").isEqualTo(2026)
                .jsonPath("$.tujuanOpds[0].kode_tujuan_opd").isEqualTo("TUJ-OPD-193")
                .jsonPath("$.tujuanOpds[0].indikators[0].kode_indikator").isEqualTo("IND-59")
                .jsonPath("$.tujuanOpds[0].indikators[0].targets[0].realisasi").isEqualTo(80.0);
    }

    @Test
    void whenSubmitTujuanOpdWithoutJenisRealisasi_thenBadRequest() {
        String payload = """
                {
                  "kodeTujuanOpd": "TUJ-1",
                  "kodeIndikator": "IND-1",
                  "kodeTarget": "TGT-1",
                  "realisasi": 75.5,
                  "tahun": "2026",
                  "bulan": "1",
                  "kodeOpd": "5.01.5.05.0.00.01.0000"
                }
                """;

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("super_admin")))
                .post()
                .uri("/tujuan_opd")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenSubmitTujuanOpdWithJenisRealisasiTurun_thenPassedToService() {
        TujuanOpdResponse response = new TujuanOpdResponse(
                1L, "5.01.5.05.0.00.01.0000", 2026, 1, "TUJ-1", "IND-1", "TGT-1",
                75.5, null, null, null, null, null, null, null, 100.0, "%", 26.0, null,
                JenisRealisasi.TURUN, null, null, null, null
        );

        when(tujuanOpdService.submitRealisasiTujuanOpd(any())).thenReturn(Mono.just(response));

        String payload = """
                {
                  "kodeTujuanOpd": "TUJ-1",
                  "kodeIndikator": "IND-1",
                  "kodeTarget": "TGT-1",
                  "realisasi": 75.5,
                  "tahun": "2026",
                  "bulan": "1",
                  "kodeOpd": "5.01.5.05.0.00.01.0000",
                  "jenisRealisasi": "TURUN"
                }
                """;

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("super_admin")))
                .post()
                .uri("/tujuan_opd")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TujuanOpdResponse.class)
                .isEqualTo(response);

        verify(tujuanOpdService).submitRealisasiTujuanOpd(argThat(req -> req.jenisRealisasi() == JenisRealisasi.TURUN));
    }
}
