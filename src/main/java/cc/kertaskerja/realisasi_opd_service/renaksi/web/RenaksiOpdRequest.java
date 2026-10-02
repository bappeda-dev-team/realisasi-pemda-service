package cc.kertaskerja.realisasi_opd_service.renaksi.web;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "RenaksiOpdRequest", description = "Payload realisasi rencana aksi OPD")
public record RenaksiOpdRequest(
        @Schema(description = "ID realisasi target yang akan di-update (opsional)")
        @Nullable Long targetRealisasiId,

        @NotNull(message = "Kode OPD tidak boleh kosong")
        @NotEmpty(message = "Kode OPD tidak boleh kosong")
        @Schema(description = "Kode OPD", example = "4.01.01.")
        String kodeOpd,

        @NotNull(message = "Tahun tidak boleh kosong")
        @NotEmpty(message = "Tahun tidak boleh kosong")
        @Schema(description = "Tahun realisasi", example = "2026")
        String tahun,

        @NotNull(message = "Bulan tidak boleh kosong")
        @NotEmpty(message = "Bulan tidak boleh kosong")
        @Schema(description = "Bulan realisasi", example = "1")
        String bulan,

        @NotNull(message = "Kode rencana aksi OPD tidak boleh kosong")
        @NotEmpty(message = "Kode rencana aksi OPD tidak boleh kosong")
        @Schema(description = "Kode rencana aksi OPD", example = "REN-OPD-1")
        String kodeRencanaAksiOpd,

        @NotNull(message = "Realisasi tidak boleh kosong")
        @Schema(description = "Nilai realisasi", example = "30")
        BigDecimal realisasi,

        @NotNull(message = "Jenis realisasi tidak boleh kosong")
        @Schema(description = "Jenis realisasi", example = "NAIK")
        JenisRealisasi jenisRealisasi,

        @Schema(description = "Faktor penunjang", example = "Kerjasama tim")
        String faktorPenunjang,

        @Schema(description = "Faktor penghambat", example = "Perubahan prioritas")
        String faktorPenghambat
) {
}
