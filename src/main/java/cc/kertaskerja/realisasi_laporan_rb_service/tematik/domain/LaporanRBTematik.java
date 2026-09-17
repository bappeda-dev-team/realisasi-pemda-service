package cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain;

import cc.kertaskerja.capaian.domain.Capaian;
import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("realisasi_target_laporan_rb_tematik")
public record LaporanRBTematik(
        @Id Long id,

        @Column("kode_opd")
        String kodeOpd,

        String nip,
        String tahun,
        String bulan,

        @Column("id_rb_tematik")
        String idRbTematik,

        @Column("id_indikator_rb_tematik")
        String idIndikatorRbTematik,

        @Column("id_target_rb_tematik")
        String idTargetRbTematik,

        Double realisasi,

        @Column("jenis_realisasi")
        JenisRealisasi jenisRealisasi,

        @Column("faktor_penunjang")
        String faktorPenunjang,

        @Column("faktor_penghambat")
        String faktorPenghambat,

        @Column("bukti_pendukung")
        String buktiPendukung,

        @Column("keterangan_bukti_pendukung")
        String keteranganBuktiPendukung,

        @CreatedBy
        @Column("created_by")
        String createdBy,

        @LastModifiedBy
        @Column("last_modified_by")
        String lastModifiedBy,

        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate
) {
    public static LaporanRBTematik of(
            String kodeOpd,
            String nip,
            String tahun,
            String bulan,
            String idRbTematik,
            String idIndikatorRbTematik,
            String idTargetRbTematik,
            Double realisasi,
            JenisRealisasi jenisRealisasi,
            String faktorPenunjang,
            String faktorPenghambat,
            String buktiPendukung,
            String keteranganBuktiPendukung
    ) {
        return new LaporanRBTematik(null,
                kodeOpd, nip, tahun, bulan, idRbTematik, idIndikatorRbTematik, idTargetRbTematik,
                realisasi, jenisRealisasi, faktorPenunjang, faktorPenghambat, buktiPendukung, keteranganBuktiPendukung,
                null, null, null, null);
    }

    public Double hitungCapaian(Double targetPenetapan) {
        if (realisasi == null || targetPenetapan == null || targetPenetapan == 0 || realisasi == 0) {
            return null;
        }
        Capaian capaianObj = new Capaian(realisasi, String.valueOf(targetPenetapan), jenisRealisasi);
        Double calculatedCapaian = capaianObj.hasilCapaian();
        return calculatedCapaian > 100 ? 100.0 : calculatedCapaian;
    }

    public String keteranganCapaian(Double targetPenetapan) {
        if (realisasi == null || targetPenetapan == null || targetPenetapan == 0 || realisasi == 0) {
            return null;
        }
        Capaian capaianObj = new Capaian(realisasi, String.valueOf(targetPenetapan), jenisRealisasi);
        Double calculatedCapaian = capaianObj.hasilCapaian();
        return calculatedCapaian > 100 ? "nilai capaian lebih dari 100% (" + String.format("%.2f%%", calculatedCapaian) + ")" : null;
    }
}