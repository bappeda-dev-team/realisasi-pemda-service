package cc.kertaskerja.realisasi_laporan_rb_service.general.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PerencanaanLaporanRBGeneralResponse(
        @JsonProperty("pegawai_id")
        @Schema(description = "NIP pegawai", example = "198012312005011001")
        String pegawaiId,

        @Schema(description = "Nama pegawai", example = "Budi Santoso")
        String nama,

        @JsonProperty("kode_opd")
        @Schema(description = "Kode OPD", example = "1.01.0.00.0.00.01.0000")
        String kodeOpd,

        @JsonProperty("tahun_aktif")
        @Schema(description = "Tahun perencanaan/realisasi", example = "2026")
        Integer tahunAktif,

        @Schema(description = "Bulan realisasi (null jika tidak difilter)", example = "1")
        Integer bulan,

        @Schema(description = "Daftar laporan RB general yang direalisasikan oleh pegawai")
        List<LaporanPerencanaanResponse> laporans
) {
    @Schema(description = "Laporan RB general tingkat laporan")
    public record LaporanPerencanaanResponse(
            @Schema(description = "ID laporan RB", example = "1")
            Long id,

            @JsonProperty("jenis_rb")
            @Schema(description = "Jenis RB", example = "GENERAL")
            String jenisRb,

            @JsonProperty("kegiatan_utama")
            @Schema(description = "Kegiatan utama", example = "Penanganan stunting")
            String kegiatanUtama,

            @Schema(description = "Keterangan laporan", example = "Laporan RB aksi nasional")
            String keterangan,

            @JsonProperty("tahun_baseline")
            @Schema(description = "Tahun baseline", example = "2024")
            Integer tahunBaseline,

            @JsonProperty("tahun_next")
            @Schema(description = "Tahun perencanaan", example = "2026")
            Integer tahunNext,

            @Schema(description = "Daftar indikator laporan")
            List<IndikatorPerencanaanResponse> indikator,

            @JsonProperty("rencana_aksis")
            @Schema(description = "Daftar rencana aksi (dipertahankan apa adanya)", example = "[]")
            List<RencanaAksiPerencanaanResponse> rencanaAksis
    ) {}

    @Schema(description = "Indikator laporan RB general")
    public record IndikatorPerencanaanResponse(
            @Schema(description = "ID indikator", example = "IND-001")
            String id,

            @JsonProperty("id_rb")
            @Schema(description = "ID laporan RB parent", example = "1")
            Long idRb,

            @Schema(description = "Nama indikator", example = "Persentase balita stunting")
            String indikator,

            @Schema(description = "Daftar target indikator")
            List<TargetPerencanaanResponse> target
    ) {}

    @Schema(description = "Target indikator RB general yang sudah digabung dengan realisasi")
    public record TargetPerencanaanResponse(
            @Schema(description = "ID target", example = "TAR-001")
            String id,

            @JsonProperty("id_indikator")
            @Schema(description = "ID indikator parent", example = "IND-001")
            String idIndikator,

            @JsonProperty("tahun_baseline")
            @Schema(description = "Tahun baseline target", example = "2024")
            Integer tahunBaseline,

            @JsonProperty("target_baseline")
            @Schema(description = "Target baseline", example = "14.5")
            String targetBaseline,

            @JsonProperty("satuan_baseline")
            @Schema(description = "Satuan baseline", example = "%")
            String satuanBaseline,

            @JsonProperty("tahun_next")
            @Schema(description = "Tahun perencanaan target", example = "2026")
            Integer tahunNext,

            @JsonProperty("target_next")
            @Schema(description = "Target pada tahun perencanaan", example = "10.0")
            String targetNext,

            @JsonProperty("satuan_next")
            @Schema(description = "Satuan pada tahun perencanaan", example = "%")
            String satuanNext,

            @Schema(description = "Realisasi (dari data realisasi lokal)", example = "8.5")
            Double realisasi,

            @Schema(description = "Capaian dalam persen (dibatasi maksimal 100)", example = "85.0")
            Double capaian,

            @JsonProperty("keterangan_capaian")
            @Schema(description = "Keterangan capaian lebih dari 100%", example = "nilai capaian lebih dari 100% (115.00%)")
            String keteranganCapaian,

            @JsonProperty("faktor_penunjang")
            @Schema(description = "Faktor penunjang realisasi", example = "Kerjasama lintas OPD")
            String faktorPenunjang,

            @JsonProperty("faktor_penghambat")
            @Schema(description = "Faktor penghambat realisasi", example = "Keterbatasan anggaran")
            String faktorPenghambat,

            @JsonProperty("bukti_pendukung")
            @Schema(description = "URL bukti pendukung", example = "https://example.com/bukti.pdf")
            String buktiPendukung,

            @JsonProperty("keterangan_bukti_pendukung")
            @Schema(description = "Keterangan bukti pendukung", example = "Dokumen pendukung")
            String keteranganBuktiPendukung,

            @JsonProperty("jenis_realisasi")
            @Schema(description = "Jenis realisasi", example = "NAIK", allowableValues = {"NAIK", "TURUN"})
            String jenisRealisasi
    ) {}

    @Schema(description = "Rencana aksi laporan RB general (tidak mengandung realisasi)")
    public record RencanaAksiPerencanaanResponse(
            @JsonProperty("id_rencana_aksi")
            @Schema(description = "ID rencana aksi", example = "RA-001")
            String idRencanaAksi,

            @JsonProperty("rencana_aksi")
            @Schema(description = "Nama rencana aksi", example = "Peningkatan capaian balita stunting")
            String rencanaAksi,

            @JsonProperty("indikator_rencana_aksis")
            @Schema(description = "Indikator rencana aksi")
            List<IndikatorRencanaAksiPerencanaanResponse> indikatorRencanaAksis,

            @Schema(description = "Anggaran rencana aksi", example = "500000000")
            String anggaran,

            @JsonProperty("realisasi_anggaran")
            @Schema(description = "Realisasi anggaran", example = "250000000")
            String realisasiAnggaran,

            @JsonProperty("capaian_anggaran")
            @Schema(description = "Capaian anggaran", example = "50")
            String capaianAnggaran,

            @JsonProperty("opd_koordinator")
            @Schema(description = "Kode OPD koordinator", example = "1.01.0.00.0.00.01.0000")
            String opdKoordinator,

            @JsonProperty("nip_pelaksana")
            @Schema(description = "NIP pelaksana", example = "198012312005011001")
            String nipPelaksana,

            @JsonProperty("nama_pelaksana")
            @Schema(description = "Nama pelaksana", example = "Budi Santoso")
            String namaPelaksana,

            @JsonProperty("opd_crosscuttings")
            @Schema(description = "Daftar OPD lintas (crosscutting)")
            List<OpdCrosscuttingPerencanaanResponse> opdCrosscuttings
    ) {}

    @Schema(description = "Indikator rencana aksi")
    public record IndikatorRencanaAksiPerencanaanResponse(
            @Schema(description = "Nama indikator rencana aksi", example = "Cakupan intervensi")
            String indikator,

            @Schema(description = "Daftar target indikator rencana aksi")
            List<TargetRencanaAksiPerencanaanResponse> targets
    ) {}

    @Schema(description = "Target indikator rencana aksi")
    public record TargetRencanaAksiPerencanaanResponse(
            @Schema(description = "Nilai target", example = "100")
            String target,

            @Schema(description = "Nilai realisasi", example = "85")
            String realisasi,

            @Schema(description = "Satuan target", example = "%")
            String satuan,

            @Schema(description = "Capaian target", example = "85")
            String capaian,

            @Schema(description = "Tahun target", example = "2026")
            String tahun
    ) {}

    @Schema(description = "OPD crosscutting rencana aksi")
    public record OpdCrosscuttingPerencanaanResponse(
            @JsonProperty("id_pohon")
            @Schema(description = "ID pohon perencanaan", example = "1")
            Long idPohon,

            @JsonProperty("kode_opd")
            @Schema(description = "Kode OPD crosscutting", example = "2.01.0.00.0.00.01.0000")
            String kodeOpd,

            @JsonProperty("nama_opd")
            @Schema(description = "Nama OPD crosscutting", example = "Dinas Kesehatan")
            String namaOpd,

            @JsonProperty("pelaksana_crosscuttings")
            @Schema(description = "Daftar pelaksana crosscutting")
            List<PelaksanaCrosscuttingPerencanaanResponse> pelaksanaCrosscuttings
    ) {}

    @Schema(description = "Pelaksana crosscutting")
    public record PelaksanaCrosscuttingPerencanaanResponse(
            @JsonProperty("nip_pelaksana")
            @Schema(description = "NIP pelaksana crosscutting", example = "198512312010011001")
            String nipPelaksana,

            @JsonProperty("nama_pelaksana")
            @Schema(description = "Nama pelaksana crosscutting", example = "Siti Rahma")
            String namaPelaksana
    ) {}
}