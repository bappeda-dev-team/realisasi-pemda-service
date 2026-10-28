package cc.kertaskerja.realisasi_individu_service.renaksi.domain;

import cc.kertaskerja.integration.kepegawaian.PegawaiClient;
import cc.kertaskerja.integration.penetapan.PenetapanRenaksiOpdClient;
import cc.kertaskerja.integration.penetapan.PenetapanRekinIndividuClient;
import cc.kertaskerja.integration.penetapan.renaksi.PenetapanRenaksiOpd;
import cc.kertaskerja.integration.upload.UploadClient;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_individu_service.renaksi.web.RenaksiIndividuRequest;
import cc.kertaskerja.realisasi_opd_service.renaksi.domain.RenaksiOpd;
import cc.kertaskerja.realisasi_opd_service.renaksi.domain.RenaksiOpdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RenaksiServiceTest {

    private static final String KODE_OPD = "8.01.0.00.0.00.01.0000";
    private static final String NIP = "198607022010011013";
    private static final String KODE_PK = "REKIN-PEG-2026-19928";
    private static final String KODE_RENAKSI = "RENAKSI-REKIN-a1306";
    private static final String KODE_PELAKSANAAN = "PLKSN-RENAKSI-28281";
    private static final String KODE_RENCANA_AKSI_OPD = "REN-OPD-1";

    @Mock
    private RenaksiIndividuRepository renaksiIndividuRepository;
    @Mock
    private UploadClient uploadClient;
    @Mock
    private PegawaiClient pegawaiClient;
    @Mock
    private PenetapanRekinIndividuClient penetapanClient;
    @Mock
    private PenetapanRenaksiOpdClient penetapanRenaksiOpdClient;
    @Mock
    private RenaksiOpdService renaksiOpdService;

    @InjectMocks
    private RenaksiService renaksiService;

    private RenaksiIndividuRequest request() {
        return new RenaksiIndividuRequest(
                null, KODE_OPD, NIP, KODE_PK, KODE_RENAKSI, KODE_PELAKSANAAN,
                BigDecimal.valueOf(4), "2026", "3", "%", JenisRealisasi.NAIK, null, null
        );
    }

    private PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot penetapanOpdRoot(String kodePk, String pegawaiId) {
        return new PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot(
                KODE_OPD, 2026, 4, true,
                List.of(new PenetapanRenaksiOpd.RenaksiPenetapanData(
                        KODE_RENCANA_AKSI_OPD, "", "SAS-OPD-5975", kodePk,
                        "Terlaksananya Koordinasi antar Pimpinan Daerah di Kabupaten",
                        pegawaiId, "", "", 168340222L, 2026, 25, 25, 35, 15
                ))
        );
    }

    private RenaksiIndividu individuRealisasi(String kodePelaksanaan, String bulan, double nilai) {
        return RenaksiIndividu.of(
                KODE_OPD, NIP, KODE_PK, KODE_RENAKSI, kodePelaksanaan,
                BigDecimal.valueOf(nilai), "2026", bulan, "%", RenaksiStatus.UNCHECKED,
                JenisRealisasi.NAIK, "", "", null, null
        );
    }

    private void mockSubmitSucceeds(String bulan) {
        // Penetapan rekin kosong -> defaultIfEmpty -> simpan realisasi individu tetap berjalan.
        when(penetapanClient.fetchRekinIndividu(NIP, KODE_OPD, 2026)).thenReturn(Mono.empty());
        when(renaksiIndividuRepository
                .findFirstByKodeOpdAndNipAndKodeRekinAndKodeRenaksiAndKodePelaksanaanAndTahunAndBulan(
                        KODE_OPD, NIP, KODE_PK, KODE_RENAKSI, KODE_PELAKSANAAN, "2026", bulan))
                .thenReturn(Mono.empty());
        when(renaksiIndividuRepository.save(any(RenaksiIndividu.class)))
                .thenAnswer(invocation -> {
                    RenaksiIndividu saved = invocation.getArgument(0);
                    return Mono.just(new RenaksiIndividu(
                            saved.id(), saved.kodeOpd(), saved.nip(), saved.kodeRekin(),
                            saved.kodeRenaksi(), saved.kodePelaksanaan(), saved.realisasi(),
                            saved.tahun(), saved.bulan(), saved.satuan(), saved.status(),
                            saved.jenisRealisasi(), saved.faktorPenunjang(), saved.faktorPenghambat(),
                            saved.buktiPendukung(), saved.keteranganBuktiPendukung(),
                            null, null, null, null));
                });
    }

    @Test
    void submitRealisasiTarget_ShouldSyncSumOfSameMonthToRenaksiOpd() {
        mockSubmitSucceeds("3");
        when(penetapanRenaksiOpdClient.fetchRenaksiOpd(KODE_OPD, 2026))
                .thenReturn(Mono.just(penetapanOpdRoot(KODE_PK, NIP)));
        when(renaksiIndividuRepository.findAllByKodeOpdAndNipAndKodeRekinAndTahunAndBulan(
                KODE_OPD, NIP, KODE_PK, "2026", "3"))
                .thenReturn(Flux.just(
                        individuRealisasi("PLKSN-RENAKSI-28281", "3", 4),
                        individuRealisasi("PLKSN-RENAKSI-50516", "3", 8)
                ));
        when(renaksiOpdService.syncRealisasiFromRenaksiIndividu(any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(RenaksiOpd.of(
                        KODE_OPD, "2026", "3", KODE_RENCANA_AKSI_OPD,
                        BigDecimal.valueOf(12), JenisRealisasi.NAIK, "", "")));

        StepVerifier.create(renaksiService.submitRealisasiTarget(request()))
                .assertNext(response -> assertEquals(BigDecimal.valueOf(4), response.realisasi()))
                .verifyComplete();

        verify(renaksiIndividuRepository).findAllByKodeOpdAndNipAndKodeRekinAndTahunAndBulan(
                KODE_OPD, NIP, KODE_PK, "2026", "3");
        verify(renaksiOpdService).syncRealisasiFromRenaksiIndividu(
                eq(KODE_OPD), eq("2026"), eq("3"), eq(KODE_RENCANA_AKSI_OPD),
                org.mockito.ArgumentMatchers.argThat(
                        nilai -> nilai != null && nilai.compareTo(BigDecimal.valueOf(12)) == 0));
    }

    @Test
    void submitRealisasiTarget_WhenNoMatchingPenetapanOpd_ShouldNotSync() {
        mockSubmitSucceeds("3");
        // kode_pk / NIP pada penetapan OPD tidak cocok.
        when(penetapanRenaksiOpdClient.fetchRenaksiOpd(KODE_OPD, 2026))
                .thenReturn(Mono.just(penetapanOpdRoot("REKIN-PEG-2026-99999", "199999999999999999")));

        StepVerifier.create(renaksiService.submitRealisasiTarget(request()))
                .assertNext(response -> assertEquals(BigDecimal.valueOf(4), response.realisasi()))
                .verifyComplete();

        verify(renaksiOpdService, never()).syncRealisasiFromRenaksiIndividu(any(), any(), any(), any(), any());
    }

    @Test
    void submitRealisasiTarget_WhenPenetapanOpdFails_ShouldStillSucceed() {
        mockSubmitSucceeds("3");
        when(penetapanRenaksiOpdClient.fetchRenaksiOpd(KODE_OPD, 2026))
                .thenReturn(Mono.error(new RuntimeException("penetapan OPD tidak tersedia")));

        StepVerifier.create(renaksiService.submitRealisasiTarget(request()))
                .assertNext(response -> assertEquals(BigDecimal.valueOf(4), response.realisasi()))
                .verifyComplete();

        verify(renaksiOpdService, never()).syncRealisasiFromRenaksiIndividu(any(), any(), any(), any(), any());
    }

    @Test
    void submitRealisasiTarget_WhenSyncFails_ShouldStillSucceed() {
        mockSubmitSucceeds("3");
        when(penetapanRenaksiOpdClient.fetchRenaksiOpd(KODE_OPD, 2026))
                .thenReturn(Mono.just(penetapanOpdRoot(KODE_PK, NIP)));
        when(renaksiIndividuRepository.findAllByKodeOpdAndNipAndKodeRekinAndTahunAndBulan(
                KODE_OPD, NIP, KODE_PK, "2026", "3"))
                .thenReturn(Flux.just(individuRealisasi(KODE_PELAKSANAAN, "3", 4)));
        when(renaksiOpdService.syncRealisasiFromRenaksiIndividu(any(), any(), any(), any(), any()))
                .thenReturn(Mono.error(new RuntimeException("gagal simpan renaksi OPD")));

        StepVerifier.create(renaksiService.submitRealisasiTarget(request()))
                .assertNext(response -> assertEquals(BigDecimal.valueOf(4), response.realisasi()))
                .verifyComplete();
    }

    @Test
    void submitRealisasiTarget_WhenNoIndividuRowInMonth_ShouldSyncZero() {
        mockSubmitSucceeds("3");
        when(penetapanRenaksiOpdClient.fetchRenaksiOpd(KODE_OPD, 2026))
                .thenReturn(Mono.just(penetapanOpdRoot(KODE_PK, NIP)));
        when(renaksiIndividuRepository.findAllByKodeOpdAndNipAndKodeRekinAndTahunAndBulan(
                KODE_OPD, NIP, KODE_PK, "2026", "3"))
                .thenReturn(Flux.empty());
        when(renaksiOpdService.syncRealisasiFromRenaksiIndividu(any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(RenaksiOpd.of(
                        KODE_OPD, "2026", "3", KODE_RENCANA_AKSI_OPD,
                        BigDecimal.ZERO, JenisRealisasi.NAIK, "", "")));

        StepVerifier.create(renaksiService.submitRealisasiTarget(request()))
                .assertNext(response -> assertEquals(BigDecimal.valueOf(4), response.realisasi()))
                .verifyComplete();

        verify(renaksiOpdService).syncRealisasiFromRenaksiIndividu(
                eq(KODE_OPD), eq("2026"), eq("3"), eq(KODE_RENCANA_AKSI_OPD), eq(BigDecimal.ZERO));
    }

    @Test
    void submitRealisasiTarget_WhenBulanBlank_ShouldNotSync() {
        mockSubmitSucceeds(" ");
        RenaksiIndividuRequest withoutBulan = new RenaksiIndividuRequest(
                null, KODE_OPD, NIP, KODE_PK, KODE_RENAKSI, KODE_PELAKSANAAN,
                BigDecimal.valueOf(4), "2026", " ", "%", JenisRealisasi.NAIK, null, null
        );

        StepVerifier.create(renaksiService.submitRealisasiTarget(withoutBulan))
                .assertNext(response -> assertEquals(BigDecimal.valueOf(4), response.realisasi()))
                .verifyComplete();

        verify(penetapanRenaksiOpdClient, never()).fetchRenaksiOpd(anyString(), org.mockito.ArgumentMatchers.anyInt());
        verify(renaksiOpdService, never()).syncRealisasiFromRenaksiIndividu(any(), any(), any(), any(), any());
    }
}
