package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.renja.domain.JenisRenja;
import com.fasterxml.jackson.annotation.JsonProperty;

@Table("renja_pagu_individu")
public record RenjaPaguIndividu(
        @Id Long id,

        @Column("renja_id")
        String renjaId,
        String renja,
        @Column("kode_renja")
        String kodeRenja,
        @Column("jenis_renja")
        JenisRenja jenisRenja,

        @Column("nip")
        String nip,
        @Column("id_indikator")
        String idIndikator,
        String indikator,

        Integer pagu,
        Integer realisasi,
        String satuan,
        String tahun,

        @Column("jenis_realisasi")
        JenisRealisasi jenisRealisasi,
        RenjaPaguIndividuStatus status,

        @Column("keterangan_capaian")
        String keteranganCapaian,

        @CreatedBy
        @Column("created_by")
        String createdBy,
        @LastModifiedBy
        @Column("last_modified_by")
        String lastModifiedBy,
        @CreatedDate
        @Column("created_date")
        Instant createdDate,
        @LastModifiedDate
        @Column("last_modified_date")
        Instant lastModifiedDate,

        @Version int version
) {
    public static RenjaPaguIndividu of(
            String renjaId,
            String renja,
            String kodeRenja,
            JenisRenja jenisRenja,
            String nip,
            String idIndikator,
            String indikator,
            Integer pagu,
            Integer realisasi,
            String satuan,
            String tahun,
            JenisRealisasi jenisRealisasi,
            RenjaPaguIndividuStatus status,
            String keteranganCapaian
    ) {
        return new RenjaPaguIndividu(
                null,
                renjaId,
                renja,
                kodeRenja,
                jenisRenja,
                nip,
                idIndikator,
                indikator,
                pagu,
                realisasi,
                satuan,
                tahun,
                jenisRealisasi,
                status,
                keteranganCapaian,
                null,
                null,
                null,
                null,
                0
        );
    }

    @JsonProperty("capaian")
    public String capaian() {
        return String.format("%.2f%%", capaianRenjaPaguIndividu());
    }

    public Double capaianRenjaPaguIndividu() {
        if (pagu == null || pagu == 0 || realisasi == null) {
            return 0.0;
        }

        double paguValue = pagu.doubleValue();
        double realisasiValue = realisasi.doubleValue();
        return (realisasiValue / paguValue) * 100;
    }
}
