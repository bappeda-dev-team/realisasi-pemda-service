package cc.kertaskerja.realisasi_opd_service.renaksi.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RenaksiOpdPenetapanResponse(
        @JsonProperty("kode_rencana_aksi_opd")
        String kodeRencanaAksiOpd,

        @JsonProperty("nama_renaksi")
        String namaRenaksi,

        @JsonProperty("kode_opd")
        String kodeOpd,

        @JsonProperty("kode_sasaran_opd")
        String kodeSasaranOpd,

        @JsonProperty("kode_pk")
        String kodePk,

        @JsonProperty("nama_pk")
        String namaPk,

        @JsonProperty("pegawai_id")
        String pegawaiId,

        @JsonProperty("kode_subkegiatan")
        String kodeSubkegiatan,

        @JsonProperty("nama_subkegiatan")
        String namaSubkegiatan,

        @JsonProperty("anggaran_renaksi")
        Long anggaranRenaksi,

        Integer tahun,

        Integer tw1,
        Integer tw2,
        Integer tw3,
        Integer tw4,

        Realisasi realisasi
) {
    public record Realisasi(
            Integer tahun,

            Integer bulan,

            Double target,

            Double realisasi,

            Double capaian,

            @JsonProperty("keterangan_capaian")
            String keteranganCapaian,

            @JsonProperty("faktor_penunjang")
            String faktorPenunjang,

            @JsonProperty("faktor_penghambat")
            String faktorPenghambat
    ) {}
}
