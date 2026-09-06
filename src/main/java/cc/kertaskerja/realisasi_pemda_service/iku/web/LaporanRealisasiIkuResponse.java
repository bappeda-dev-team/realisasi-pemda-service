package cc.kertaskerja.realisasi_pemda_service.iku.web;

import cc.kertaskerja.realisasi.domain.JenisLaporan;
import cc.kertaskerja.realisasi_pemda_service.iku.domain.JenisIku;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(name = "LaporanRealisasiIkuResponse", description = "Response laporan realisasi IKU pemda per periode")
public record LaporanRealisasiIkuResponse(
        @Schema(description = "Tahun laporan", example = "2026")
        String tahun,

        @Schema(description = "Jenis IKU (TUJUAN atau SASARAN)", example = "TUJUAN")
        JenisIku jenisIku,

        @Schema(description = "Nama tujuan atau sasaran pemda", example = "Meningkatkan kualitas pendidikan")
        String namaInduk,

        @Schema(description = "Nama indikator", example = "Angka partisipasi murni")
        String namaIndikator,

        @Schema(description = "Target laporan", example = "100")
        String target,

        @Schema(description = "Jenis periode laporan", example = "TAHUNAN")
        JenisLaporan jenisLaporan,

        @Schema(description = "Data realisasi per periode. Key = nomor periode (bulan/triwulan), Value = total realisasi",
                example = "{\"1\": 120.0, \"2\": 95.0}")
        Map<String, Double> listData,

        @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
        @Schema(description = "Total realisasi (hanya untuk TRIWULAN dan TAHUNAN)", example = "215.0")
        Double totalRealisasi
) {}
