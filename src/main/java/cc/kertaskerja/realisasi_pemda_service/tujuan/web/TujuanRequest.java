package cc.kertaskerja.realisasi_pemda_service.tujuan.web;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record TujuanRequest(
        @Nullable
        Long targetRealisasiId,

        @NotNull(message = "ID tujuan tidak boleh kosong")
        @NotEmpty(message = "ID tujuan tidak boleh kosong")
        String tujuanId,

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
        JenisRealisasi jenisRealisasi
) {}
