package cc.kertaskerja.realisasi_opd_service.tujuan.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TujuanOpdPenetapanResponse(
        @JsonProperty("kode_opd")
        String kodeOpd,

        @JsonProperty("kode_tujuan_opd")
        String kodeTujuanOpd,

        @JsonProperty("tujuan_opd")
        String tujuanOpd,

        String periode,

        @JsonProperty("tahun_aktif")
        Integer tahunAktif,

        Integer versi,

        List<IndikatorPenetapan> indikator
) {
    public record IndikatorPenetapan(
            @JsonProperty("kode_indikator")
            String kodeIndikator,

            String indikator,

            @JsonProperty("rumus_perhitungan")
            String rumusPerhitungan,

            @JsonProperty("sumber_data")
            String sumberData,

            @JsonProperty("definisi_operasional")
            String definisiOperasional,

            List<TargetPenetapan> target
    ) {
    }

    public record TargetPenetapan(
            @JsonProperty("kode_target")
            String kodeTarget,

            String satuan,

            Double target,

            Double realisasi,

            Double capaian,

            @JsonProperty("keterangan_capaian")
            String keteranganCapaian
    ) {
    }
}
