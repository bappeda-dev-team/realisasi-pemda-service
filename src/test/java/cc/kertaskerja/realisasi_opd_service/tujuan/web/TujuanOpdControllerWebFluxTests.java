package cc.kertaskerja.realisasi_opd_service.tujuan.web;

import cc.kertaskerja.config.SecurityConfig;
import cc.kertaskerja.realisasi_opd_service.tujuan.domain.TujuanOpdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

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
        TujuanOpdResponse result = new TujuanOpdResponse(
                12L,
                "5.01.5.05.0.00.01.0000",
                "TUJ-OPD-193",
                null,
                2026,
                3,
                List.of(new TujuanOpdResponse.IndikatorResponse(
                        13L,
                        "IND-59",
                        null,
                        null,
                        null,
                        null,
                        2026,
                        3,
                        List.of(new TujuanOpdResponse.TargetResponse(
                                14L,
                                "TGT-TRG-TJN-1bdac",
                                null,
                                null,
                                2026,
                                3,
                                80.0,
                                null,
                                null
                        ))
                ))
        );

        when(tujuanOpdService.getRealisasiTujuanOpdByTahunAndKodeOpdAndBulan("2026", "5.01.5.05.0.00.01.0000", "3"))
                .thenReturn(Flux.just(result));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("level_1")))
                .get()
                .uri("/tujuan_opd/5.01.5.05.0.00.01.0000/tahun/2026/bulan/3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.kode_opd").isEqualTo("5.01.5.05.0.00.01.0000")
                .jsonPath("$.tahun").isEqualTo(2026)
                .jsonPath("$.tujuan_opds[0].kode_tujuan_opd").isEqualTo("TUJ-OPD-193")
                .jsonPath("$.tujuan_opds[0].indikators[0].kode_indikator").isEqualTo("IND-59")
                .jsonPath("$.tujuan_opds[0].indikators[0].targets[0].realisasi").isEqualTo(80.0);
    }
}
