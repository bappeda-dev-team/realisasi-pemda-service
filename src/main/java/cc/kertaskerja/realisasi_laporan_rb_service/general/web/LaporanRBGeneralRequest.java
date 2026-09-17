package cc.kertaskerja.realisasi_laporan_rb_service.general.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "LaporanRBGeneralRequest", description = "Payload untuk membuat/memperbarui realisasi target laporan RB general")
public record LaporanRBGeneralRequest(

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

        @NotNull(message = "ID laporan RB general tidak boleh kosong")
        @NotEmpty(message = "ID laporan RB general tidak boleh kosong")
        @Schema(description = "ID laporan RB general", example = "1")
        String idRbGeneral,

        @NotNull(message = "ID indikator RB general tidak boleh kosong")
        @NotEmpty(message = "ID indikator RB general tidak boleh kosong")
        @Schema(description = "ID indikator RB general", example = "IND-001")
        String idIndikatorRbGeneral,

        @NotNull(message = "ID target RB general tidak boleh kosong")
        @NotEmpty(message = "ID target RB general tidak boleh kosong")
        @Schema(description = "ID target RB general", example = "TAR-001")
        String idTargetRbGeneral,

        @NotNull(message = "Realisasi harus terdefinisi")
        @Schema(description = "Nilai realisasi aktual", example = "8.5")
        Double realisasi,

        @Schema(description = "URL bukti pendukung realisasi", example = "https://example.com/bukti.pdf")
        String buktiPendukung,

        @Schema(description = "Keterangan dari bukti pendukung", example = "Dokumen pendukung")
        String keteranganBuktiPendukung
) {
}