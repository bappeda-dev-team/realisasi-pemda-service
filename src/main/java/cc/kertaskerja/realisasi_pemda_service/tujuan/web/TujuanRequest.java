package cc.kertaskerja.realisasi_pemda_service.tujuan.web;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import jakarta.validation.constraints.*;

public record TujuanRequest(
        @NotNull(message = "ID tujuan tidak boleh kosong")
        @NotEmpty(message = "ID tujuan tidak boleh kosong")
        String tujuanId,

        @NotNull(message = "Target harus terdefinisi")
        @Positive(message = "Target harus positif")
        Double target,

        @NotNull(message = "Realisasi harus terdefinisi")
        @PositiveOrZero(message = "Realisasi tidak boleh negatif")
        Double realisasi,

        @NotEmpty(message = "Satuan tidak boleh kosong")
        String satuan,

        @NotEmpty(message = "Tahun tidak boleh kosong")
        String tahun,

        @NotNull(message = "Pilih jenis NAIK atau TURUN")
        JenisRealisasi jenisRealisasi
) {}
