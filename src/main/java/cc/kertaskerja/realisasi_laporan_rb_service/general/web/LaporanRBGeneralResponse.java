package cc.kertaskerja.realisasi_laporan_rb_service.general.web;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneral;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record LaporanRBGeneralResponse(
        Long id,

        @JsonProperty("kode_opd")
        String kodeOpd,

        String nip,
        String tahun,
        String bulan,

        @JsonProperty("id_rb_general")
        String idRbGeneral,

        @JsonProperty("id_indikator_rb_general")
        String idIndikatorRbGeneral,

        @JsonProperty("id_target_rb_general")
        String idTargetRbGeneral,

        Double realisasi,

        @JsonProperty("jenis_realisasi")
        JenisRealisasi jenisRealisasi,

        @JsonProperty("faktor_penunjang")
        String faktorPenunjang,

        @JsonProperty("faktor_penghambat")
        String faktorPenghambat,

        @JsonProperty("bukti_pendukung")
        String buktiPendukung,

        @JsonProperty("created_by")
        String createdBy,

        @JsonProperty("last_modified_by")
        String lastModifiedBy,

        @JsonProperty("created_date")
        Instant createdDate,

        @JsonProperty("last_modified_date")
        Instant lastModifiedDate,

        Double target,

        Double capaian,

        @JsonProperty("keterangan_capaian")
        String keteranganCapaian,

        @JsonProperty("keterangan_bukti_pendukung")
        String keteranganBuktiPendukung
) {
    public static LaporanRBGeneralResponse from(LaporanRBGeneral entity, Double target, Double capaian, String keteranganCapaian) {
        return new LaporanRBGeneralResponse(
                entity.id(), entity.kodeOpd(), entity.nip(), entity.tahun(), entity.bulan(),
                entity.idRbGeneral(), entity.idIndikatorRbGeneral(), entity.idTargetRbGeneral(),
                entity.realisasi(), entity.jenisRealisasi(),
                entity.faktorPenunjang(), entity.faktorPenghambat(), entity.buktiPendukung(),
                entity.createdBy(), entity.lastModifiedBy(),
                entity.createdDate(), entity.lastModifiedDate(),
                target, capaian, keteranganCapaian, entity.keteranganBuktiPendukung()
        );
    }
}