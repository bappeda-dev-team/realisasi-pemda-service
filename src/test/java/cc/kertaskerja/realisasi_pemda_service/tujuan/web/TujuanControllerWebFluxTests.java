package cc.kertaskerja.realisasi_pemda_service.tujuan.web;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.Tujuan;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.TujuanService;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.TujuanStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@WebFluxTest(TujuanController.class)
public class TujuanControllerWebFluxTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TujuanService tujuanService;

    @Test
    void whenTujuanIdParamsExistsInByTahunShouldReturnRealisasiTujuanByTahunAndTujuanId() {
        List<Tujuan> mockTujuans = List.of(
                Tujuan.of("TUJ-123", "Test-Tujuan",
                        "IND-TUJ-123", "Produk-A",
                        100.0, 100.0, "%", "2025",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED));
        Mockito.when(tujuanService.getRealisasiTujuanByTahunAndTujuanId("2025", "TUJ-123"))
                .thenReturn(Flux.fromIterable(mockTujuans));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tujuans/by-tahun/2025")
                        .queryParam("tujuanId", "TUJ-123")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tujuan.class)
                .hasSize(1)
                .contains(mockTujuans.get(0));
    }

    @Test
    void whenNoTujuanIdShouldReutrnRealisasiTujuanByTahun() {
        List<Tujuan> mockTujuans = List.of(
                Tujuan.of("TUJ-123", "Test-Tujuan",
                        "IND-TUJ-123", "Produk-A",
                        100.0, 100.0, "%", "2025",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED),
                Tujuan.of("TUJ-123", "Test-Tujuan",
                        "IND-TUJ-123", "Produk-A",
                        100.0, 100.0, "%", "2026",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED)
                );
        Mockito.when(tujuanService.getRealisasiTujuanByTahun("2025"))
                .thenReturn(Flux.just(mockTujuans.get(0)));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .get()
                .uri("/tujuans/by-tahun/2025")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tujuan.class)
                .hasSize(1)
                .contains(mockTujuans.get(0));
    }

    @Test
    void whenByIndikatorIdShouldReturnRealisasiTujuanByIndikatorId() {
        List<Tujuan> mockTujuans = List.of(
                Tujuan.of("TUJ-123", "Test-Tujuan",
                        "IND-TUJ-123", "Produk-A",
                        100.0, 100.0, "%", "2025",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED));
        Mockito.when(tujuanService.getRealisasiTujuanByIndikatorId("IND-TUJ-123"))
                .thenReturn(Flux.fromIterable(mockTujuans));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .get()
                .uri("/tujuans/by-indikator/IND-TUJ-123")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tujuan.class)
                .hasSize(1)
                .contains(mockTujuans.get(0));
    }

    @Test
    void whenByPeriodeRpjmdShouldReturnRealisasiTujuanInTahunRpjmd() {
        List<Tujuan> mockTujuans = List.of(
                Tujuan.of("TUJ-123", "Test-Tujuan",
                        "IND-TUJ-123", "Produk-A",
                        100.0, 100.0, "%", "2025",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED),
                Tujuan.of("TUJ-123", "Test-Tujuan",
                        "IND-TUJ-123", "Produk-A",
                        100.0, 100.0, "%", "2026",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED),
                Tujuan.of("TUJ-12", "Test-Tujuan",
                        "IND-TUJ-124", "Produk-B",
                        150.0, 150.0, "%", "2025",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED),
                Tujuan.of("TUJ-12", "Test-Tujuan",
                        "IND-TUJ-124", "Produk-B",
                        180.0, 180.0, "%", "2027",
                        JenisRealisasi.NAIK, TujuanStatus.UNCHECKED)
        );
        Mockito.when(tujuanService.getRealisasiTujuanByPeriodeRpjmd("2025", "2030"))
                .thenReturn(Flux.fromIterable(mockTujuans));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .get()
                .uri("/tujuans/by-periode/2025/2030/rpjmd")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Tujuan.class)
                .hasSize(4)
                .contains(mockTujuans.get(0));
    }
}
