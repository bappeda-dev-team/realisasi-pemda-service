package cc.kertaskerja.realisasi_opd_service.renaksi.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Table("realisasi_target_renaksi_opd")
public record RenaksiOpd(
        @Id Long id,

        @Column("kode_opd")
        String kodeOpd,

        String tahun,

        String bulan,

        @Column("kode_rencana_aksi_opd")
        String kodeRencanaAksiOpd,

        BigDecimal realisasi,

        @Column("jenis_realisasi")
        JenisRealisasi jenisRealisasi,

        @Column("faktor_penunjang")
        String faktorPenunjang,

        @Column("faktor_penghambat")
        String faktorPenghambat,

        @CreatedBy
        @Column("created_by")
        String createdBy,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @LastModifiedBy
        @Column("last_modified_by")
        String lastModifiedBy
) {
    public static RenaksiOpd of(
            String kodeOpd,
            String tahun,
            String bulan,
            String kodeRencanaAksiOpd,
            BigDecimal realisasi,
            JenisRealisasi jenisRealisasi,
            String faktorPenunjang,
            String faktorPenghambat
    ) {
        return new RenaksiOpd(null, kodeOpd, tahun, bulan, kodeRencanaAksiOpd, realisasi,
                jenisRealisasi, faktorPenunjang, faktorPenghambat, null, null, null, null);
    }

    public record CapaianResult(Double capaian, String keteranganCapaian) {}

    public static CapaianResult hitungCapaian(Double realisasi, Double target) {
        if (realisasi == null || target == null || target == 0 || realisasi == 0) {
            return new CapaianResult(null, null);
        }
        double calculatedCapaian = realisasi / target * 100;
        String keteranganCapaian = null;
        if (calculatedCapaian > 100) {
            keteranganCapaian = "nilai capaian lebih dari 100% (" + String.format("%.2f%%", calculatedCapaian) + ")";
        }
        return new CapaianResult(Math.min(calculatedCapaian, 100), keteranganCapaian);
    }

    public RenaksiOpd withFaktorPenunjang(String faktorPenunjang) {
        return new RenaksiOpd(id, kodeOpd, tahun, bulan, kodeRencanaAksiOpd, realisasi,
                jenisRealisasi, faktorPenunjang, faktorPenghambat, createdBy, createdDate, lastModifiedDate, lastModifiedBy);
    }

    public RenaksiOpd withFaktorPenghambat(String faktorPenghambat) {
        return new RenaksiOpd(id, kodeOpd, tahun, bulan, kodeRencanaAksiOpd, realisasi,
                jenisRealisasi, faktorPenunjang, faktorPenghambat, createdBy, createdDate, lastModifiedDate, lastModifiedBy);
    }
}
