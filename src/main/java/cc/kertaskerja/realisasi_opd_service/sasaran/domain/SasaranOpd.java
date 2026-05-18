package cc.kertaskerja.realisasi_opd_service.sasaran.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("sasaran_opd")
public record SasaranOpd(
        @Id Long id,

        @Column("kode_opd")
        String kodeOpd,
        @Column("kode_sasaran_opd")
        String kodeSasaranOpd,
        String tahun,
        String bulan,

        @CreatedBy
        @Column("created_by")
        String createdBy,
        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @LastModifiedBy
        @Column("last_modified_by")
        String lastModifiedBy
) {
    public static SasaranOpd of(
            String kodeOpd,
            String kodeSasaranOpd,
            String tahun,
            String bulan
    ) {
        return new SasaranOpd(null,
                kodeOpd, kodeSasaranOpd, tahun, bulan,
                null, null, null, null);
    }
}
