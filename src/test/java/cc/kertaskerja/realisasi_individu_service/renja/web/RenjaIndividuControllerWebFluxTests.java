package cc.kertaskerja.realisasi_individu_service.renja.web;

import cc.kertaskerja.config.SecurityConfig;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_individu_service.renja.domain.RenjaIndividuService;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(RenjaIndividuController.class)
@Import(SecurityConfig.class)
public class RenjaIndividuControllerWebFluxTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RenjaIndividuService renjaIndividuService;

    @Test
    void whenGetPenetapanByNipAndTahun_thenReturnsPenetapanData() {
        var response = new PenetapanRenjaIndividuResponse(
                "198701252015051001", "PAULUS NUGROHO UTOMO S.Sos",
                "8.01.0.00.0.00.01.0000", 2026, null,
                List.of(new PenetapanRenjaIndividuResponse.RenjaPenetapanResponse(
                        4L, "REKIN-PEG-2026-94919", 6, "198701252015051001", "PAULUS NUGROHO UTOMO S.Sos",
                        "8.01.03", "PROGRAM PENINGKATAN PERAN PARTAI POLITIK", "PAGU-PRG-8.01.03-2026-programs", 751383784L,
                        List.of(new PenetapanRenjaIndividuResponse.IndikatorPenetapanResponse(
                                5L, "IND-RENJA-PENETAPAN-8.01.03", "Persentase cakupan pembinaan",
                                List.of(new PenetapanRenjaIndividuResponse.TargetPenetapanResponse(
                                        6L, "TGT-001", 2026, 100.0, "%",
                                        null, null, null, null, null, null, null, null, null, null, null
                                ))
                        )),
                        "8.01.03.2.01", "Kegiatan 1", "PAGU-KEG", 751383784L, List.of(),
                        "8.01.03.2.01.0003", "Subkegiatan 1", "PAGU-SUBKEG", 222630430L, List.of()
                ))
        );

        when(renjaIndividuService.getPenetapanByNip(anyString(), anyString(), anyInt(), eq(null)))
                .thenReturn(Mono.just(response));

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN_OPD")))
                .get()
                .uri("/renja_individu/program/kodeOpd/8.01.0.00.0.00.01.0000/nip/198701252015051001/tahun/2026/penetapan")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PenetapanRenjaIndividuResponse.class)
                .consumeWith(result -> {
                    var body = result.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals("198701252015051001", body.pegawaiId());
                    Assertions.assertEquals("8.01.0.00.0.00.01.0000", body.kodeOpd());
                });
    }

    @Test
    void whenGetPenetapanByNipAndTahunWithBulan_thenReturnsPenetapanData() {
        var response = new PenetapanRenjaIndividuResponse(
                "198701252015051001", "PAULUS NUGROHO UTOMO S.Sos",
                "8.01.0.00.0.00.01.0000", 2026, 1,
                List.of()
        );

        when(renjaIndividuService.getPenetapanByNip(anyString(), anyString(), anyInt(), eq("1")))
                .thenReturn(Mono.just(response));

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN_OPD")))
                .get()
                .uri("/renja_individu/program/kodeOpd/8.01.0.00.0.00.01.0000/nip/198701252015051001/tahun/2026/penetapan?bulan=1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PenetapanRenjaIndividuResponse.class)
                .consumeWith(result -> {
                    var body = result.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals("198701252015051001", body.pegawaiId());
                    Assertions.assertEquals(1, body.bulan());
                });
    }

    @Test
    void whenGetPenetapanKegiatanByNipAndTahun_thenReturnsPenetapanData() {
        var response = new PenetapanRenjaIndividuResponse(
                "198701252015051001", "PAULUS NUGROHO UTOMO S.Sos",
                "8.01.0.00.0.00.01.0000", 2026, 1,
                List.of()
        );

        when(renjaIndividuService.getPenetapanByNip(anyString(), anyString(), anyInt(), eq("1")))
                .thenReturn(Mono.just(response));

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN_OPD")))
                .get()
                .uri("/renja_individu/kegiatan/kodeOpd/8.01.0.00.0.00.01.0000/nip/198701252015051001/tahun/2026/penetapan?bulan=1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PenetapanRenjaIndividuResponse.class)
                .consumeWith(result -> {
                    var body = result.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals("198701252015051001", body.pegawaiId());
                    Assertions.assertEquals(1, body.bulan());
                });
    }

    @Test
    void whenGetPenetapanSubKegiatanByNipAndTahun_thenReturnsPenetapanData() {
        var response = new PenetapanRenjaIndividuResponse(
                "198701252015051001", "PAULUS NUGROHO UTOMO S.Sos",
                "8.01.0.00.0.00.01.0000", 2026, 1,
                List.of()
        );

        when(renjaIndividuService.getPenetapanByNip(anyString(), anyString(), anyInt(), eq("1")))
                .thenReturn(Mono.just(response));

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN_OPD")))
                .get()
                .uri("/renja_individu/subkegiatan/kodeOpd/8.01.0.00.0.00.01.0000/nip/198701252015051001/tahun/2026/penetapan?bulan=1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PenetapanRenjaIndividuResponse.class)
                .consumeWith(result -> {
                    var body = result.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals("198701252015051001", body.pegawaiId());
                    Assertions.assertEquals(1, body.bulan());
                });
    }

    @Test
    void whenSyncRenjaIndividu_thenReturnsPenetapanData() {
        var response = new PenetapanRenjaIndividuResponse(
                "198701252015051001", "PAULUS NUGROHO UTOMO S.Sos",
                "8.01.0.00.0.00.01.0000", 2026, null,
                List.of()
        );

        when(renjaIndividuService.syncPenetapanRenjaIndividu(anyString(), anyString(), anyInt()))
                .thenReturn(Mono.just("SUCCESS"));
        when(renjaIndividuService.getPenetapanByNip(anyString(), anyString(), anyInt(), eq(null)))
                .thenReturn(Mono.just(response));

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("level_2")))
                .post()
                .uri("/renja_individu/nip/198701252015051001/kodeOpd/8.01.0.00.0.00.01.0000/tahun/2026/sync/penetapan")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(PenetapanRenjaIndividuResponse.class)
                .consumeWith(result -> {
                    var body = result.getResponseBody();
                    Assertions.assertNotNull(body);
                    Assertions.assertEquals("198701252015051001", body.pegawaiId());
                });
    }

    @Test
    void whenSubmitProgramWithoutJenisRealisasi_thenBadRequest() {
        String payload = """
                {
                  "kodeOpd": "8.01.0.00.0.00.01.0000",
                  "tahun": "2026",
                  "bulan": "1",
                  "nip": "198701252015051001",
                  "kodeProgram": "8.01.03",
                  "kodeIndikator": "IND-001",
                  "kodeTarget": "TGT-001",
                  "realisasi": 50.0
                }
                """;

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("level_2")))
                .post()
                .uri("/renja_individu/program")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenSubmitKegiatanWithoutJenisRealisasi_thenBadRequest() {
        String payload = """
                {
                  "kodeOpd": "8.01.0.00.0.00.01.0000",
                  "tahun": "2026",
                  "bulan": "1",
                  "nip": "198701252015051001",
                  "kodeKegiatan": "8.01.03.2.01",
                  "kodeIndikator": "IND-002",
                  "kodeTarget": "TGT-002",
                  "realisasi": 75.0
                }
                """;

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("level_3")))
                .post()
                .uri("/renja_individu/kegiatan")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenSubmitSubKegiatanWithoutJenisRealisasi_thenBadRequest() {
        String payload = """
                {
                  "kodeOpd": "8.01.0.00.0.00.01.0000",
                  "tahun": "2026",
                  "bulan": "1",
                  "nip": "198701252015051001",
                  "kodeSubKegiatan": "8.01.03.2.01.0003",
                  "kodeIndikator": "IND-003",
                  "kodeTarget": "TGT-003",
                  "realisasi_target": 80.0,
                  "realisasi_pagu": 5000000.0
                }
                """;

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("level_3")))
                .post()
                .uri("/renja_individu/subkegiatan")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenSubmitKegiatanWithJenisRealisasiTurun_thenPassedToService() {
        var response = new cc.kertaskerja.realisasi_individu_service.renja.web.kegiatan.RenjaIndividuKegiatanResponse(
                1L, "8.01.0.00.0.00.01.0000", "2026", "1", "198701252015051001",
                "8.01.03.2.01", "Kegiatan 1", "IND-002", "Indikator 2", "TGT-002", "PAGU-2",
                1000.0, 100.0, 75.0, "TURUN", 26.0, "keterangan",
                "", "", "bukti.pdf", null, null
        );

        when(renjaIndividuService.submitKegiatan(any())).thenReturn(Mono.just(response));

        String payload = """
                {
                  "kodeOpd": "8.01.0.00.0.00.01.0000",
                  "tahun": "2026",
                  "bulan": "1",
                  "nip": "198701252015051001",
                  "kodeKegiatan": "8.01.03.2.01",
                  "kodeIndikator": "IND-002",
                  "kodeTarget": "TGT-002",
                  "realisasi": 75.0,
                  "jenisRealisasi": "TURUN"
                }
                """;

        webTestClient
                .mutateWith(csrf())
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("level_3")))
                .post()
                .uri("/renja_individu/kegiatan")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(cc.kertaskerja.realisasi_individu_service.renja.web.kegiatan.RenjaIndividuKegiatanResponse.class)
                .isEqualTo(response);

        verify(renjaIndividuService).submitKegiatan(argThat(req -> req.jenisRealisasi() == JenisRealisasi.TURUN));
    }
}
