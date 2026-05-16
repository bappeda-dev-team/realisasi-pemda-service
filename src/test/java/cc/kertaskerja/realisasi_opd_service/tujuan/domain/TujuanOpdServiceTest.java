package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TujuanOpdServiceTest {
    @Mock
    private TujuanOpdRepository tujuanOpdRepository;

    @InjectMocks
    private TujuanOpdService tujuanOpdService;

    @Test
    void submitRealisasiTujuanOpd_ShouldReturnSavedEntity_WhenValidInputProvided() {
        // Arrange
        String tujuanId = UUID.randomUUID().toString();
        String kodeTujuanOpd = "KODE-TUJ-OPD-001";
        String indikatorId = UUID.randomUUID().toString();
        String kodeIndikatorTujuanOpd = "KODE-IND-TUJ-OPD-001";
        String targetId = UUID.randomUUID().toString();
        String kodeTargetTujuanOpd = "KODE-TAR-TUJ-OPD-001";
        String target = "100";
        Double realisasi = 80.0;
        String satuan = "Unit";
        String tahun = "2025";
        String bulan = "1";
        JenisRealisasi jenisRealisasi = JenisRealisasi.NAIK;
        String kodeOpd = "OPD001";
        String rumusPerhitungan = "(realisasi/target)*100";
        String sumberData = "SIMDA";
        String definisiOperational = "Definisi indikator tujuan";

        TujuanOpd expectedTujuanOpd = TujuanOpd.of(
                tujuanId,
                kodeTujuanOpd,
                "Realisasi Tujuan Opd " + tujuanId,
                indikatorId,
                kodeIndikatorTujuanOpd,
                "Realisasi Indikator Opd " + indikatorId,
                targetId,
                kodeTargetTujuanOpd,
                target,
                realisasi,
                satuan,
                tahun,
                bulan,
                jenisRealisasi,
                kodeOpd,
                rumusPerhitungan,
                sumberData,
                definisiOperational,
                TujuanOpdStatus.UNCHECKED
        );

        when(tujuanOpdRepository.save(any(TujuanOpd.class))).thenReturn(Mono.just(expectedTujuanOpd));

        // Act
        Mono<TujuanOpd> result = tujuanOpdService.submitRealisasiTujuanOpd(
                tujuanId, kodeTujuanOpd, indikatorId, kodeIndikatorTujuanOpd, targetId, kodeTargetTujuanOpd, target, realisasi, satuan, tahun, bulan, jenisRealisasi, kodeOpd, rumusPerhitungan, sumberData, definisiOperational);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(tujuanOpd ->
                                tujuanOpd.tujuanId().equals(expectedTujuanOpd.tujuanId()) &&
                                tujuanOpd.kodeTujuanOpd().equals(expectedTujuanOpd.kodeTujuanOpd()) &&
                                tujuanOpd.indikatorId().equals(expectedTujuanOpd.indikatorId()) &&
                                tujuanOpd.kodeIndikatorTujuanOpd().equals(expectedTujuanOpd.kodeIndikatorTujuanOpd()) &&
                                tujuanOpd.target().equals(expectedTujuanOpd.target()) &&
                                tujuanOpd.kodeTargetTujuanOpd().equals(expectedTujuanOpd.kodeTargetTujuanOpd()) &&
                                tujuanOpd.realisasi().equals(expectedTujuanOpd.realisasi()) &&
                                tujuanOpd.capaian().equals(expectedTujuanOpd.capaian()) &&
                                tujuanOpd.satuan().equals(expectedTujuanOpd.satuan()) &&
                                tujuanOpd.tahun().equals(expectedTujuanOpd.tahun()) &&
                                tujuanOpd.bulan().equals(expectedTujuanOpd.bulan()) &&
                                tujuanOpd.jenisRealisasi() == expectedTujuanOpd.jenisRealisasi() &&
                                tujuanOpd.kodeOpd().equals(expectedTujuanOpd.kodeOpd()) &&
                                tujuanOpd.rumusPerhitungan().equals(expectedTujuanOpd.rumusPerhitungan()) &&
                                tujuanOpd.sumberData().equals(expectedTujuanOpd.sumberData()) &&
                                tujuanOpd.definisiOperational().equals(expectedTujuanOpd.definisiOperational()) &&
                                tujuanOpd.status() == TujuanOpdStatus.UNCHECKED)
                .verifyComplete();
    }

    @Test
    void submitRealisasiTujuanOpd_ShouldThrowError_WhenRepositoryFails() {
        // Arrange
        String tujuanId = UUID.randomUUID().toString();
        String kodeTujuanOpd = "KODE-TUJ-OPD-001";
        String indikatorId = UUID.randomUUID().toString();
        String kodeIndikatorTujuanOpd = "KODE-IND-TUJ-OPD-001";
        String targetId = UUID.randomUUID().toString();
        String kodeTargetTujuanOpd = "KODE-TAR-TUJ-OPD-001";
        String target = "100";
        Double realisasi = 80.0;
        String satuan = "Unit";
        String tahun = "2025";
        String bulan = "1";
        JenisRealisasi jenisRealisasi = JenisRealisasi.NAIK;
        String kodeOpd = "OPD001";
        String rumusPerhitungan = "(realisasi/target)*100";
        String sumberData = "SIMDA";
        String definisiOperational = "Definisi indikator tujuan";

        when(tujuanOpdRepository.save(any(TujuanOpd.class))).thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        // Act
        Mono<TujuanOpd> result = tujuanOpdService.submitRealisasiTujuanOpd(
                tujuanId, kodeTujuanOpd, indikatorId, kodeIndikatorTujuanOpd, targetId, kodeTargetTujuanOpd, target, realisasi, satuan, tahun, bulan, jenisRealisasi, kodeOpd, rumusPerhitungan, sumberData, definisiOperational);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Unexpected error"))
                .verify();
    }

    @Test
    void getRealisasiTujuanOpdByDetailFilter_ShouldDelegateToRepositoryWithExactParameters() {
        // Arrange
        String kodeOpd = "1.01.0.00.0.00.01.0000";
        String tahun = "2025";
        String bulan = "1";
        String targetId = "target-1";
        String indikatorId = "indikator-1";
        String tujuanId = "tujuan-1";

        TujuanOpd row = TujuanOpd.of(
                tujuanId,
                "KODE-TUJ-OPD-001",
                "Tujuan OPD",
                indikatorId,
                "KODE-IND-TUJ-OPD-001",
                "Indikator OPD",
                targetId,
                "KODE-TAR-TUJ-OPD-001",
                "100",
                80.0,
                "Unit",
                tahun,
                bulan,
                JenisRealisasi.NAIK,
                kodeOpd,
                "(realisasi/target)*100",
                "SIMDA",
                "Definisi indikator tujuan",
                TujuanOpdStatus.UNCHECKED
        );

        when(tujuanOpdRepository.findAllByKodeOpdAndTahunAndBulanAndTargetIdAndIndikatorIdAndTujuanId(
                kodeOpd, tahun, bulan, targetId, indikatorId, tujuanId
        )).thenReturn(reactor.core.publisher.Flux.fromIterable(List.of(row)));

        // Act
        var result = tujuanOpdService.getRealisasiTujuanOpdByKodeOpdAndTahunAndBulanAndTargetIdAndIndikatorIdAndTujuanId(
                kodeOpd, tahun, bulan, targetId, indikatorId, tujuanId
        );

        // Assert
        StepVerifier.create(result)
                .expectNext(row)
                .verifyComplete();

        verify(tujuanOpdRepository).findAllByKodeOpdAndTahunAndBulanAndTargetIdAndIndikatorIdAndTujuanId(
                kodeOpd, tahun, bulan, targetId, indikatorId, tujuanId
        );
    }
}
