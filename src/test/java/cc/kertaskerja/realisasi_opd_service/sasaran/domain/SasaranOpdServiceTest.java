package cc.kertaskerja.realisasi_opd_service.sasaran.domain;

import cc.kertaskerja.integration.penetapan.PenetapanSasaranOpdClient;
import cc.kertaskerja.realisasi_opd_service.sasaran.domain.indikator.IndikatorSasaranOpd;
import cc.kertaskerja.realisasi_opd_service.sasaran.domain.indikator.IndikatorSasaranOpdRepository;
import cc.kertaskerja.realisasi_opd_service.sasaran.domain.target.TargetIndikatorSasaranOpd;
import cc.kertaskerja.realisasi_opd_service.sasaran.domain.target.TargetIndikatorSasaranOpdRepository;
import cc.kertaskerja.realisasi_opd_service.sasaran.web.SasaranOpdRequest;
import cc.kertaskerja.realisasi_opd_service.sasaran.web.SasaranOpdResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SasaranOpdServiceTest {
    @Mock
    private SasaranOpdRepository sasaranOpdRepository;
    @Mock
    private IndikatorSasaranOpdRepository indikatorSasaranOpdRepository;
    @Mock
    private TargetIndikatorSasaranOpdRepository targetIndikatorSasaranOpdRepository;
    @Mock
    private PenetapanSasaranOpdClient penetapanSasaranOpdClient;

    @InjectMocks
    private SasaranOpdService sasaranOpdService;

    @Test
    void getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan_ShouldReturnMappedResponses() {
        SasaranOpd sasaran = new SasaranOpd(1L, "5.01.5.05.0.00.01.0000", "SAS-OPD-001",
                "2026", "3", "tester", Instant.now(), Instant.now(), "tester");
        IndikatorSasaranOpd indikator = new IndikatorSasaranOpd(2L, 1L, "IND-01", "5.01.5.05.0.00.01.0000",
                "2026", "3", Instant.now(), Instant.now(), "tester", null);
        TargetIndikatorSasaranOpd target = new TargetIndikatorSasaranOpd(3L, 2L, "TGT-001", BigDecimal.valueOf(75),
                "2026", "3", Instant.now(), Instant.now(), "tester", null);

        when(sasaranOpdRepository.findAllByTahunAndKodeOpdAndBulan("2026", "5.01.5.05.0.00.01.0000", "3"))
                .thenReturn(Flux.just(sasaran));
        when(indikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(indikator));
        when(targetIndikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(target));
        when(penetapanSasaranOpdClient.fetchSasaranOpd("5.01.5.05.0.00.01.0000", 2026)).thenReturn(Mono.just(List.of()));

        StepVerifier.create(sasaranOpdService.getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan("2026", "5.01.5.05.0.00.01.0000", "3"))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1L, response.id());
                    org.junit.jupiter.api.Assertions.assertEquals(75.0, response.indikator().getFirst().target().getFirst().realisasi());
                })
                .verifyComplete();
    }

    @Test
    void submitRealisasiSasaranOpd_ShouldCreateNewAndReturnResponse() {
        SasaranOpdRequest request = new SasaranOpdRequest(
                "KODE-SAS-OPD-001", "KODE-IND-SAS-OPD-001",
                "KODE-TAR-SAS-OPD-001", 75.5, "2026", "1",
                "1.01.0.00.0.00.01.0000"
        );
        SasaranOpd saved = new SasaranOpd(1L, "1.01.0.00.0.00.01.0000", "KODE-SAS-OPD-001",
                "2026", "1", null, Instant.now(), Instant.now(), null);
        IndikatorSasaranOpd savedIndikator = new IndikatorSasaranOpd(2L, 1L, "KODE-IND-SAS-OPD-001",
                "1.01.0.00.0.00.01.0000", "2026", "1",
                Instant.now(), Instant.now(), null, null);
        TargetIndikatorSasaranOpd savedTarget = new TargetIndikatorSasaranOpd(3L, 2L, "KODE-TAR-SAS-OPD-001",
                BigDecimal.valueOf(76), "2026", "1", Instant.now(), Instant.now(), null, null);

        when(sasaranOpdRepository.findFirstByKodeOpdAndKodeSasaranOpdAndTahunAndBulan(
                "1.01.0.00.0.00.01.0000", "KODE-SAS-OPD-001", "2026", "1"))
                .thenReturn(Mono.empty());
        when(sasaranOpdRepository.save(any(SasaranOpd.class))).thenReturn(Mono.just(saved));
        when(indikatorSasaranOpdRepository.findFirstBySasaranOpdIdAndKodeIndikatorAndKodeOpdAndTahunAndBulan(
                1L, "KODE-IND-SAS-OPD-001", "1.01.0.00.0.00.01.0000", "2026", "1"))
                .thenReturn(Mono.empty());
        when(indikatorSasaranOpdRepository.save(any(IndikatorSasaranOpd.class))).thenReturn(Mono.just(savedIndikator));
        when(targetIndikatorSasaranOpdRepository.findFirstByIndikatorSasaranIdAndKodeTargetAndTahunAndBulan(
                2L, "KODE-TAR-SAS-OPD-001", "2026", "1"))
                .thenReturn(Mono.empty());
        when(targetIndikatorSasaranOpdRepository.save(any(TargetIndikatorSasaranOpd.class))).thenReturn(Mono.just(savedTarget));
        when(indikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(savedIndikator));
        when(targetIndikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(savedTarget));
        when(penetapanSasaranOpdClient.fetchSasaranOpd("1.01.0.00.0.00.01.0000", 2026)).thenReturn(Mono.just(List.of()));

        StepVerifier.create(sasaranOpdService.submitRealisasiSasaranOpd(request))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1L, response.id());
                    org.junit.jupiter.api.Assertions.assertEquals("KODE-SAS-OPD-001", response.kodeSasaranOpd());
                    org.junit.jupiter.api.Assertions.assertEquals(1, response.indikator().size());
                    org.junit.jupiter.api.Assertions.assertEquals(76.0, response.indikator().getFirst().target().getFirst().realisasi());
                })
                .verifyComplete();
    }

    @Test
    void submitRealisasiSasaranOpd_ShouldUpdateExistingTarget() {
        SasaranOpdRequest request = new SasaranOpdRequest(
                "KODE-SAS-OPD-001", "KODE-IND-SAS-OPD-001",
                "KODE-TAR-SAS-OPD-001", 90.0, "2026", "1",
                "1.01.0.00.0.00.01.0000"
        );
        SasaranOpd existingSasaran = new SasaranOpd(1L, "1.01.0.00.0.00.01.0000", "KODE-SAS-OPD-001",
                "2026", "1", "admin", Instant.now(), Instant.now(), "admin");
        IndikatorSasaranOpd existingIndikator = new IndikatorSasaranOpd(2L, 1L, "KODE-IND-SAS-OPD-001",
                "1.01.0.00.0.00.01.0000", "2026", "1", Instant.now(), Instant.now(), "admin", null);
        TargetIndikatorSasaranOpd existingTarget = new TargetIndikatorSasaranOpd(3L, 2L, "KODE-TAR-SAS-OPD-001",
                BigDecimal.valueOf(50), "2026", "1", Instant.now(), Instant.now(), "admin", null);
        TargetIndikatorSasaranOpd updatedTarget = new TargetIndikatorSasaranOpd(3L, 2L, "KODE-TAR-SAS-OPD-001",
                BigDecimal.valueOf(90), "2026", "1", Instant.now(), null, "admin", null);

        when(sasaranOpdRepository.findFirstByKodeOpdAndKodeSasaranOpdAndTahunAndBulan(
                "1.01.0.00.0.00.01.0000", "KODE-SAS-OPD-001", "2026", "1"))
                .thenReturn(Mono.just(existingSasaran));
        when(indikatorSasaranOpdRepository.findFirstBySasaranOpdIdAndKodeIndikatorAndKodeOpdAndTahunAndBulan(
                1L, "KODE-IND-SAS-OPD-001", "1.01.0.00.0.00.01.0000", "2026", "1"))
                .thenReturn(Mono.just(existingIndikator));
        when(targetIndikatorSasaranOpdRepository.findFirstByIndikatorSasaranIdAndKodeTargetAndTahunAndBulan(
                2L, "KODE-TAR-SAS-OPD-001", "2026", "1"))
                .thenReturn(Mono.just(existingTarget));
        when(targetIndikatorSasaranOpdRepository.save(any(TargetIndikatorSasaranOpd.class))).thenReturn(Mono.just(updatedTarget));
        when(indikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(existingIndikator));
        when(targetIndikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(updatedTarget));
        when(penetapanSasaranOpdClient.fetchSasaranOpd("1.01.0.00.0.00.01.0000", 2026)).thenReturn(Mono.just(List.of()));

        StepVerifier.create(sasaranOpdService.submitRealisasiSasaranOpd(request))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals(90.0, response.indikator().getFirst().target().getFirst().realisasi());
                })
                .verifyComplete();
    }

    @Test
    void batchSubmitRealisasiSasaranOpd_ShouldSubmitAll() {
        SasaranOpdRequest req1 = new SasaranOpdRequest(
                "KODE-1", "KODE-IND-1",
                "KODE-TAR-1", 25.0, "2026", "1",
                "OPD-001"
        );
        SasaranOpdRequest req2 = new SasaranOpdRequest(
                "KODE-2", "KODE-IND-2",
                "KODE-TAR-2", 50.0, "2026", "1",
                "OPD-001"
        );

        SasaranOpd saved1 = new SasaranOpd(1L, "OPD-001", "KODE-1",
                "2026", "1", null, Instant.now(), Instant.now(), null);
        SasaranOpd saved2 = new SasaranOpd(2L, "OPD-001", "KODE-2",
                "2026", "1", null, Instant.now(), Instant.now(), null);

        when(sasaranOpdRepository.findFirstByKodeOpdAndKodeSasaranOpdAndTahunAndBulan(
                "OPD-001", "KODE-1", "2026", "1")).thenReturn(Mono.empty());
        when(sasaranOpdRepository.findFirstByKodeOpdAndKodeSasaranOpdAndTahunAndBulan(
                "OPD-001", "KODE-2", "2026", "1")).thenReturn(Mono.empty());

        when(sasaranOpdRepository.save(any(SasaranOpd.class)))
                .thenReturn(Mono.just(saved1), Mono.just(saved2));

        when(indikatorSasaranOpdRepository.findFirstBySasaranOpdIdAndKodeIndikatorAndKodeOpdAndTahunAndBulan(
                any(), any(), any(), any(), any())).thenReturn(Mono.empty());
        IndikatorSasaranOpd ind1 = new IndikatorSasaranOpd(3L, 1L, "KODE-IND-1", "OPD-001",
                "2026", "1", null, null, null, null);
        IndikatorSasaranOpd ind2 = new IndikatorSasaranOpd(4L, 2L, "KODE-IND-2", "OPD-001",
                "2026", "1", null, null, null, null);
        when(indikatorSasaranOpdRepository.save(any(IndikatorSasaranOpd.class)))
                .thenReturn(Mono.just(ind1), Mono.just(ind2));

        when(targetIndikatorSasaranOpdRepository.findFirstByIndikatorSasaranIdAndKodeTargetAndTahunAndBulan(
                any(), any(), any(), any())).thenReturn(Mono.empty());
        TargetIndikatorSasaranOpd tgt1 = new TargetIndikatorSasaranOpd(5L, 3L, "KODE-TAR-1",
                BigDecimal.valueOf(25), "2026", "1", null, null, null, null);
        TargetIndikatorSasaranOpd tgt2 = new TargetIndikatorSasaranOpd(6L, 4L, "KODE-TAR-2",
                BigDecimal.valueOf(50), "2026", "1", null, null, null, null);
        when(targetIndikatorSasaranOpdRepository.save(any(TargetIndikatorSasaranOpd.class)))
                .thenReturn(Mono.just(tgt1), Mono.just(tgt2));

        when(indikatorSasaranOpdRepository.findAll())
                .thenReturn(Flux.just(ind1), Flux.just(ind2));
        when(targetIndikatorSasaranOpdRepository.findAll())
                .thenReturn(Flux.just(tgt1), Flux.just(tgt2));
        when(penetapanSasaranOpdClient.fetchSasaranOpd("OPD-001", 2026)).thenReturn(Mono.just(List.of()));

        StepVerifier.create(sasaranOpdService.batchSubmitRealisasiSasaranOpd(List.of(req1, req2)))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan_ShouldReturnEmpty_WhenNoData() {
        when(sasaranOpdRepository.findAllByTahunAndKodeOpdAndBulan("2026", "1.01.0.00.0.00.01.0000", "1"))
                .thenReturn(Flux.empty());

        StepVerifier.create(sasaranOpdService.getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan("2026", "1.01.0.00.0.00.01.0000", "1"))
                .verifyComplete();
    }

    @Test
    void getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan_ShouldHideOrphanData() {
        SasaranOpd sasaran = new SasaranOpd(1L, "5.01.5.05.0.00.01.0000", "SAS-ORPHAN",
                "2026", "3", "tester", Instant.now(), Instant.now(), "tester");
        IndikatorSasaranOpd indikator = new IndikatorSasaranOpd(2L, 1L, "IND-ORPHAN", "5.01.5.05.0.00.01.0000",
                "2026", "3", Instant.now(), Instant.now(), "tester", null);
        TargetIndikatorSasaranOpd target = new TargetIndikatorSasaranOpd(3L, 2L, "TGT-ORPHAN", BigDecimal.valueOf(75),
                "2026", "3", Instant.now(), Instant.now(), "tester", null);

        when(sasaranOpdRepository.findAllByTahunAndKodeOpdAndBulan("2026", "5.01.5.05.0.00.01.0000", "3"))
                .thenReturn(Flux.just(sasaran));
        when(indikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(indikator));
        when(targetIndikatorSasaranOpdRepository.findAll()).thenReturn(Flux.just(target));
        when(penetapanSasaranOpdClient.fetchSasaranOpd("5.01.5.05.0.00.01.0000", 2026)).thenReturn(Mono.just(List.of()));

        StepVerifier.create(sasaranOpdService.getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan("2026", "5.01.5.05.0.00.01.0000", "3"))
                .assertNext(response -> {
                    org.junit.jupiter.api.Assertions.assertEquals("SAS-ORPHAN", response.kodeSasaranOpd());
                    org.junit.jupiter.api.Assertions.assertTrue(response.indikator().getFirst().target().stream()
                            .anyMatch(t -> "TGT-ORPHAN".equals(t.kodeTarget())));
                })
                .verifyComplete();
    }
}
