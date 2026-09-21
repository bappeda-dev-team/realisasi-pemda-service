package cc.kertaskerja.realisasi_laporan_rb_service.general.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Laporan realisasi RB general per target (baris flat, siap render tabel laporan)")
public record LaporanRBGeneralPerTargetResponse(
        @JsonProperty("pegawai_id")
        @Schema(description = "NIP pegawai pelaksana", example = "198012312005011001")
        String pegawaiId,

        @JsonProperty("kode_opd")
        @Schema(description = "Kode OPD", example = "1.01.0.00.0.00.01.0000")
        String kodeOpd,

        @JsonProperty("tahun_aktif")
        @Schema(description = "Tahun laporan", example = "2026")
        Integer tahunAktif,

        @Schema(description = "Bulan realisasi (null jika tidak difilter)", example = "1")
        Integer bulan,

        @Schema(description = "Daftar baris laporan; satu baris per target indikator dengan tahun_next = tahun")
        List<BarisLaporanResponse> data
) {
    @Schema(description = "Baris laporan per target indikator RB general")
    public record BarisLaporanResponse(
            @JsonProperty("id_rb")
            @Schema(description = "ID laporan RB", example = "1")
            Long idRb,

            @JsonProperty("id_indikator")
            @Schema(description = "ID indikator RB", example = "IND-001")
            String idIndikator,

            @JsonProperty("id_target")
            @Schema(description = "ID target indikator (kunci baris)", example = "TAR-001")
            String idTarget,

            @JsonProperty("kegiatan_utama")
            @Schema(description = "Kegiatan utama", example = "Peningkatan Kualitas Pelayanan Publik")
            String kegiatanUtama,

            @Schema(description = "Nama indikator", example = "Persentase kepuasan masyarakat")
            String indikator,

            @Schema(description = "Keterangan laporan RB", example = "Laporan RB aksi nasional")
            String keterangan,

            @JsonProperty("tahun_baseline")
            @Schema(description = "Tahun baseline target", example = "2025")
            Integer tahunBaseline,

            @JsonProperty("target_baseline")
            @Schema(description = "Target baseline", example = "75")
            String targetBaseline,

            @JsonProperty("satuan_baseline")
            @Schema(description = "Satuan baseline", example = "%")
            String satuanBaseline,

            @JsonProperty("tahun_berjalan")
            @Schema(description = "Tahun berjalan (tahun_next target)", example = "2026")
            Integer tahunBerjalan,

            @Schema(description = "Target pada tahun berjalan", example = "80")
            String target,

            @Schema(description = "Satuan pada tahun berjalan", example = "%")
            String satuan,

            @Schema(description = "Realisasi terakhir pada target ini (null jika belum ada realisasi)")
            RealisasiTargetResponse realisasi
    ) {}

    @Schema(description = "Realisasi pada satu target RB general")
    public record RealisasiTargetResponse(
            @Schema(description = "ID record realisasi", example = "345")
            Long id,

            @Schema(description = "Bulan realisasi", example = "9")
            String bulan,

            @Schema(description = "Nilai realisasi", example = "72")
            Double nilai,

            @Schema(description = "Capaian dalam persen (dibatasi maksimal 100)", example = "90.0")
            Double capaian,

            @JsonProperty("keterangan_capaian")
            @Schema(description = "Keterangan capaian lebih dari 100%", example = "nilai capaian lebih dari 100% (115.00%)")
            String keteranganCapaian,

            @JsonProperty("jenis_realisasi")
            @Schema(description = "Jenis realisasi", example = "NAIK", allowableValues = {"NAIK", "TURUN"})
            String jenisRealisasi,

            @JsonProperty("faktor_penunjang")
            @Schema(description = "Faktor penunjang realisasi", example = "Dukungan anggaran OPD")
            String faktorPenunjang,

            @JsonProperty("faktor_penghambat")
            @Schema(description = "Faktor penghambat realisasi", example = "Keterbatasan SDM")
            String faktorPenghambat,

            @JsonProperty("bukti_pendukung")
            @Schema(description = "URL bukti pendukung", example = "https://example.com/bukti.pdf")
            String buktiPendukung,

            @JsonProperty("keterangan_bukti_pendukung")
            @Schema(description = "Keterangan bukti pendukung", example = "Laporan bulan September")
            String keteranganBuktiPendukung
    ) {}
}
