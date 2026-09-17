package cc.kertaskerja.integration.perencanaan.laporanrbgeneral;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LaporanRBGeneral {

    public record LaporanRBGeneralData(
            Long id,
            @JsonProperty("jenis_rb") String jenisRb,
            @JsonProperty("kegiatan_utama") String kegiatanUtama,
            String keterangan,
            @JsonProperty("tahun_baseline") Integer tahunBaseline,
            @JsonProperty("tahun_next") Integer tahunNext,
            List<IndikatorRBData> indikator,
            @JsonProperty("rencana_aksis") List<RencanaAksiData> rencanaAksis
    ) {
        public LaporanRBGeneralData {
            if (indikator == null) indikator = List.of();
            if (rencanaAksis == null) rencanaAksis = List.of();
        }
    }

    public record IndikatorRBData(
            String id,
            @JsonProperty("id_rb") Long idRb,
            String indikator,
            List<TargetIndikatorRBData> target
    ) {
        public IndikatorRBData {
            if (target == null) target = List.of();
        }
    }

    public record TargetIndikatorRBData(
            String id,
            @JsonProperty("id_indikator") String idIndikator,
            @JsonProperty("tahun_baseline") Integer tahunBaseline,
            @JsonProperty("target_baseline") String targetBaseline,
            @JsonProperty("realisasi_baseline") String realisasiBaseline,
            @JsonProperty("satuan_baseline") String satuanBaseline,
            @JsonProperty("tahun_next") Integer tahunNext,
            @JsonProperty("target_next") String targetNext,
            @JsonProperty("satuan_next") String satuanNext
    ) {}

    public record RencanaAksiData(
            @JsonProperty("id_rencana_aksi") String idRencanaAksi,
            @JsonProperty("rencana_aksi") String rencanaAksi,
            @JsonProperty("indikator_rencana_aksis") List<IndikatorRencanaAksiData> indikatorRencanaAksis,
            String anggaran,
            @JsonProperty("realisasi_anggaran") String realisasiAnggaran,
            @JsonProperty("capaian_anggaran") String capaianAnggaran,
            @JsonProperty("opd_koordinator") String opdKoordinator,
            @JsonProperty("nip_pelaksana") String nipPelaksana,
            @JsonProperty("nama_pelaksana") String namaPelaksana,
            @JsonProperty("opd_crosscuttings") List<OpdCrosscuttingData> opdCrosscuttings
    ) {
        public RencanaAksiData {
            if (indikatorRencanaAksis == null) indikatorRencanaAksis = List.of();
            if (opdCrosscuttings == null) opdCrosscuttings = List.of();
        }
    }

    public record IndikatorRencanaAksiData(
            String indikator,
            List<TargetRencanaAksiData> targets
    ) {
        public IndikatorRencanaAksiData {
            if (targets == null) targets = List.of();
        }
    }

    public record TargetRencanaAksiData(
            String target,
            String realisasi,
            String satuan,
            String capaian,
            String tahun
    ) {}

    public record OpdCrosscuttingData(
            @JsonProperty("id_pohon") Long idPohon,
            @JsonProperty("kode_opd") String kodeOpd,
            @JsonProperty("nama_opd") String namaOpd,
            @JsonProperty("pelaksana_crosscuttings") List<PelaksanaCrosscuttingData> pelaksanaCrosscuttings
    ) {
        public OpdCrosscuttingData {
            if (pelaksanaCrosscuttings == null) pelaksanaCrosscuttings = List.of();
        }
    }

    public record PelaksanaCrosscuttingData(
            @JsonProperty("nip_pelaksana") String nipPelaksana,
            @JsonProperty("nama_pelaksana") String namaPelaksana
    ) {}
}
