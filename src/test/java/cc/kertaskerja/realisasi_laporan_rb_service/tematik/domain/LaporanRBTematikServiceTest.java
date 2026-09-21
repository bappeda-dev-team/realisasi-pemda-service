package cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain;

import cc.kertaskerja.integration.perencanaan.LaporanRBTematikClient;
import cc.kertaskerja.integration.perencanaan.laporanrbtematik.LaporanRBTematik;
import cc.kertaskerja.integration.upload.UploadClient;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LaporanRBTematikServiceTest {

    @Mock
    private LaporanRBTematikRepository repository;
    @Mock
    private LaporanRBTematikClient laporanRBTematikClient;
    @Mock
    private UploadClient uploadClient;

    @InjectMocks
    private LaporanRBTematikService service;

    private static final String NIP = "198012312005011001";
    private static final String KODE_OPD = "1.01.0.00.0.00.01.0000";
    private static final String TAHUN = "2026";
    private static final int TAHUN_INT = 2026;

    // Master: dua RB. RB "A" punya 2 target (satu tahun_next 2026, satu 2027 -> harus di-filter).
    // RB "B" punya 1 indikator dengan 2 target pada 2026 (multi target per indikator).
    private final LaporanRBTematik.LaporanRBTematikData laporanA = new LaporanRBTematik.LaporanRBTematikData(
            1L, "TEMATIK", "A Kegiatan", "Ket A", 2025, 2026,
            List.of(new LaporanRBTematik.IndikatorRBData(
                    "IND-A", 1L, "Indikator A",
                    List.of(
                            new LaporanRBTematik.TargetIndikatorRBData(
                                    "TAR-A1", "IND-A", 2025, "75", "70", "%", 2026, "80", "%"),
                            new LaporanRBTematik.TargetIndikatorRBData(
                                    "TAR-A2", "IND-A", 2025, "75", "70", "%", 2027, "80", "%")
                    ))),
            List.of());

    private final LaporanRBTematik.LaporanRBTematikData laporanB = new LaporanRBTematik.LaporanRBTematikData(
            2L, "TEMATIK", "B Kegiatan", "Ket B", 2025, 2026,
            List.of(new LaporanRBTematik.IndikatorRBData(
                    "IND-B", 2L, "Indikator B",
                    List.of(
                            new LaporanRBTematik.TargetIndikatorRBData(
                                    "TAR-B1", "IND-B", 2025, "60", "55", "%", 2026, "80", "%"),
                            new LaporanRBTematik.TargetIndikatorRBData(
                                    "TAR-B2", "IND-B", 2025, "60", "55", "%", 2026, "70", "%")
                    ))),
            List.of());

    private cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik realisasi(
            Long id, String idRb, String idIndikator, String idTarget, Double nilai) {
        return new cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematik(
                id, KODE_OPD, NIP, TAHUN, "9", idRb, idIndikator, idTarget,
                nilai, JenisRealisasi.NAIK,
                "Dukungan anggaran", "Keterbatasan SDM",
                "https://example.com/bukti.pdf", "Dokumen pendukung",
                "user", "user", Instant.now(), Instant.now());
    }

    @Test
    void getLaporanPerTarget_WithoutBulan_ShouldFilterTahunNextAndMergeLatestRealisasi() {
        when(laporanRBTematikClient.fetchLaporanByTahun(TAHUN_INT))
                .thenReturn(Mono.just(List.of(laporanA, laporanB)));
        when(repository.findAllByKodeOpdAndNipAndTahun(KODE_OPD, NIP, TAHUN))
                .thenReturn(Flux.just(
                        realisasi(10L, "2", "IND-B", "TAR-B1", 72.0),
                        // record lebih baru (id lebih besar) untuk target yang sama
                        realisasi(11L, "2", "IND-B", "TAR-B1", 50.0),
                        realisasi(5L, "1", "IND-A", "TAR-A1", 150.0)
                ));

        StepVerifier.create(service.getLaporanPerTarget(NIP, KODE_OPD, TAHUN_INT, null))
                .assertNext(response -> {
                    assertEquals(NIP, response.pegawaiId());
                    assertEquals(KODE_OPD, response.kodeOpd());
                    assertEquals(TAHUN_INT, response.tahunAktif());
                    assertNull(response.bulan());

                    // target TAR-A2 (tahun_next 2027) harus tidak ikut
                    assertEquals(3, response.data().size());

                    // sorting: kegiatan_utama lalu indikator
                    var barisA = response.data().get(0);
                    assertEquals("TAR-A1", barisA.idTarget());
                    assertEquals("A Kegiatan", barisA.kegiatanUtama());
                    assertEquals("Indikator A", barisA.indikator());
                    assertEquals(2025, barisA.tahunBaseline());
                    assertEquals("75", barisA.targetBaseline());
                    assertEquals("%", barisA.satuanBaseline());
                    assertEquals(2026, barisA.tahunBerjalan());
                    assertEquals("80", barisA.target());
                    assertEquals("%", barisA.satuan());

                    // realisasi 150 dengan target 80 -> capaian dibatasi 100 + keterangan
                    assertNotNull(barisA.realisasi());
                    assertEquals(150.0, barisA.realisasi().nilai());
                    assertEquals(100.0, barisA.realisasi().capaian());
                    assertNotNull(barisA.realisasi().keteranganCapaian());
                    assertTrue(barisA.realisasi().keteranganCapaian().contains("lebih dari 100%"));
                    assertEquals("Dukungan anggaran", barisA.realisasi().faktorPenunjang());
                    assertEquals("https://example.com/bukti.pdf", barisA.realisasi().buktiPendukung());

                    // realisasi terakhir (id 11, nilai 50) yang dipakai, bukan id 10 (nilai 72)
                    var barisB1 = response.data().get(1);
                    assertEquals("TAR-B1", barisB1.idTarget());
                    assertNotNull(barisB1.realisasi());
                    assertEquals(11L, barisB1.realisasi().id());
                    assertEquals(50.0, barisB1.realisasi().nilai());
                    assertEquals(62.5, barisB1.realisasi().capaian());

                    // target tanpa realisasi tetap muncul dengan realisasi null
                    var barisB2 = response.data().get(2);
                    assertEquals("TAR-B2", barisB2.idTarget());
                    assertNull(barisB2.realisasi());
                    assertEquals("70", barisB2.target());
                })
                .verifyComplete();
    }

    @Test
    void getLaporanPerTarget_WithBulan_ShouldQueryByBulan() {
        when(laporanRBTematikClient.fetchLaporanByTahun(TAHUN_INT))
                .thenReturn(Mono.just(List.of(laporanB)));
        when(repository.findAllByKodeOpdAndNipAndTahunAndBulan(KODE_OPD, NIP, TAHUN, "9"))
                .thenReturn(Flux.just(realisasi(10L, "2", "IND-B", "TAR-B1", 72.0)));

        StepVerifier.create(service.getLaporanPerTarget(NIP, KODE_OPD, TAHUN_INT, "9"))
                .assertNext(response -> {
                    assertEquals(9, response.bulan());
                    assertEquals(2, response.data().size());
                    assertEquals(72.0, response.data().get(0).realisasi().nilai());
                    assertNull(response.data().get(1).realisasi());
                })
                .verifyComplete();
    }

    @Test
    void getLaporanPerTarget_WhenMasterEmpty_ShouldReturnEmptyData() {
        when(laporanRBTematikClient.fetchLaporanByTahun(TAHUN_INT))
                .thenReturn(Mono.just(List.of()));
        when(repository.findAllByKodeOpdAndNipAndTahun(KODE_OPD, NIP, TAHUN))
                .thenReturn(Flux.empty());

        StepVerifier.create(service.getLaporanPerTarget(NIP, KODE_OPD, TAHUN_INT, null))
                .assertNext(response -> {
                    assertTrue(response.data().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void getLaporanPerTarget_WhenRealisasiZero_ShouldHaveNullCapaian() {
        when(laporanRBTematikClient.fetchLaporanByTahun(TAHUN_INT))
                .thenReturn(Mono.just(List.of(laporanB)));
        when(repository.findAllByKodeOpdAndNipAndTahun(KODE_OPD, NIP, TAHUN))
                .thenReturn(Flux.just(realisasi(10L, "2", "IND-B", "TAR-B1", 0.0)));

        StepVerifier.create(service.getLaporanPerTarget(NIP, KODE_OPD, TAHUN_INT, null))
                .assertNext(response -> {
                    var baris = response.data().get(0);
                    assertNotNull(baris.realisasi());
                    assertEquals(0.0, baris.realisasi().nilai());
                    assertNull(baris.realisasi().capaian());
                })
                .verifyComplete();
    }
}
