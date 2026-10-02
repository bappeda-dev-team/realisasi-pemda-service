package cc.kertaskerja.realisasi_opd_service.renaksi.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FaktorPenghambatRenaksiOpdRequest", description = "Payload untuk memperbarui faktor penghambat pada realisasi renaksi OPD")
public record FaktorPenghambatRenaksiOpdRequest(
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

        @Schema(description = "Faktor penghambat renaksi", example = "Perubahan prioritas")
        String faktorPenghambat
) {}
