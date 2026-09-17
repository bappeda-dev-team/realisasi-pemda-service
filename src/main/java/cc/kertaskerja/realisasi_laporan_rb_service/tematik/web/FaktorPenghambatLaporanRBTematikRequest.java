package cc.kertaskerja.realisasi_laporan_rb_service.tematik.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FaktorPenghambatLaporanRBTematikRequest", description = "Payload untuk memperbarui faktor penghambat pada realisasi laporan RB tematik")
public record FaktorPenghambatLaporanRBTematikRequest(
        @NotNull(message = "Kode OPD tidak boleh kosong")
        @NotEmpty(message = "Kode OPD tidak boleh kosong")
        @Schema(description = "Kode OPD", example = "8.01.0.00.0.00.01.0000")
        String kodeOpd,

        @NotNull(message = "NIP tidak boleh kosong")
        @NotEmpty(message = "NIP tidak boleh kosong")
        @Schema(description = "NIP pelaksana", example = "196909212007012018")
        String nip,

        @NotNull(message = "Tahun harus terdefinisi")
        @NotEmpty(message = "Tahun tidak boleh kosong")
        @Schema(description = "Tahun realisasi", example = "2026")
        String tahun,

        @NotNull(message = "Bulan harus terdefinisi")
        @NotEmpty(message = "Bulan tidak boleh kosong")
        @Schema(description = "Bulan realisasi", example = "1")
        String bulan,

        @NotNull(message = "ID laporan RB tematik tidak boleh kosong")
        @NotEmpty(message = "ID laporan RB tematik tidak boleh kosong")
        @Schema(description = "ID laporan RB tematik", example = "1")
        String idRbTematik,

        @NotNull(message = "ID indikator RB tematik tidak boleh kosong")
        @NotEmpty(message = "ID indikator RB tematik tidak boleh kosong")
        @Schema(description = "ID indikator RB tematik", example = "IND-001")
        String idIndikatorRbTematik,

        @NotNull(message = "ID target RB tematik tidak boleh kosong")
        @NotEmpty(message = "ID target RB tematik tidak boleh kosong")
        @Schema(description = "ID target RB tematik", example = "TAR-001")
        String idTargetRbTematik,

        @Schema(description = "Faktor penghambat laporan RB tematik", example = "Keterbatasan anggaran")
        String faktorPenghambat
) {}