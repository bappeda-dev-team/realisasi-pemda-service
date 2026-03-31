package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.web;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain.RenjaPaguIndividu;
import cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain.RenjaPaguIndividuService;
import cc.kertaskerja.renja.domain.JenisRenja;
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

@WebFluxTest(RenjaPaguIndividuController.class)
public class RenjaPaguIndividuControllerWebFluxTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RenjaPaguIndividuService renjaPaguIndividuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenBatchSubmit_thenReturnsSavedRenjaPaguIndividus() throws Exception {
        RenjaPaguIndividuRequest r1 = new RenjaPaguIndividuRequest(
                null,
                "RENJA-1",
                "Program A",
                "1.02.01",
                JenisRenja.PROGRAM,
                "198012312005011001",
                "IND-1",
                "Indikator A",
                100,
                50,
                "rupiah",
                "2025",
                JenisRealisasi.NAIK
        );
        RenjaPaguIndividuRequest r2 = new RenjaPaguIndividuRequest(
                null,
                "RENJA-2",
                "Kegiatan B",
                "1.02.02",
                JenisRenja.KEGIATAN,
                "198012312005011001",
                "IND-2",
                "Indikator B",
                200,
                75,
                "rupiah",
                "2026",
                JenisRealisasi.NAIK
        );

        RenjaPaguIndividu p1 = RenjaPaguIndividuService.buildUncheckedRealisasiRenjaPaguIndividu(
                r1.renjaId(),
                r1.renja(),
                r1.kodeRenja(),
                r1.jenisRenja(),
                r1.nip(),
                r1.idIndikator(),
                r1.indikator(),
                r1.pagu(),
                r1.realisasi(),
                r1.satuan(),
                r1.tahun(),
                r1.jenisRealisasi()
        );
        RenjaPaguIndividu p2 = RenjaPaguIndividuService.buildUncheckedRealisasiRenjaPaguIndividu(
                r2.renjaId(),
                r2.renja(),
                r2.kodeRenja(),
                r2.jenisRenja(),
                r2.nip(),
                r2.idIndikator(),
                r2.indikator(),
                r2.pagu(),
                r2.realisasi(),
                r2.satuan(),
                r2.tahun(),
                r2.jenisRealisasi()
        );

        when(renjaPaguIndividuService.batchSubmitRealisasiRenjaPaguIndividu(anyList()))
                .thenReturn(Flux.just(p1, p2));

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .post()
                .uri("/renja_pagu_individu/batch")
                .bodyValue(objectMapper.writeValueAsString(List.of(r1, r2)))
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(RenjaPaguIndividu.class)
                .consumeWith(response -> {
                    var body = response.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals(2, body.size());
                    Assertions.assertEquals(p1, body.get(0));
                    Assertions.assertEquals("50.00%", body.get(0).capaian());
                    Assertions.assertEquals(p2, body.get(1));
                    Assertions.assertEquals("37.50%", body.get(1).capaian());
                });
    }
}
