package cc.kertaskerja.realisasi_opd_service.sasaran.web;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SasaranOpdRequest(
        @Nullable
        Long targetRealisasiId,

        @NotNull(message = "ID sasaran tidak boleh kosong")
        @NotEmpty(message = "ID sasaran tidak boleh kosong")
        String sasaranId,

        @NotNull(message = "ID indikator tidak boleh kosong")
        @NotEmpty(message = "ID indikator tidak boleh kosong")
        String indikatorId,

        @NotNull(message = "Target harus terdefinisi")
        @NotEmpty(message = "ID target tidak boleh kosong")
        String targetId,

        @NotNull(message = "Target harus terdefinisi")
        String target,

        @NotNull(message = "Realisasi harus terdefinisi")
        @PositiveOrZero(message = "Realisasi tidak boleh negatif")
        Double realisasi,

        @NotEmpty(message = "Satuan tidak boleh kosong")
        String satuan,

        @NotNull(message = "Tahun harus terdefinisi")
        @NotEmpty(message = "Tahun tidak boleh kosong")
        String tahun,

        @NotNull(message = "Pilih jenis NAIK atau TURUN")
        JenisRealisasi jenisRealisasi,

        @NotEmpty(message = "Kode opd tidak boleh kosong")
        @NotNull(message = "Kode opd tidak boleh kosong")
        String kodeOpd
) {
}
