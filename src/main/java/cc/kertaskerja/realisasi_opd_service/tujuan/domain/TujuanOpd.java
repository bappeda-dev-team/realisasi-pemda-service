package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("tujuan_opd")
public record TujuanOpd(
        @Id Long id,

        @Column("kode_opd")
        String kodeOpd,
        @Column("kode_tujuan_opd")
        String kodeTujuanOpd,
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
    public static TujuanOpd of(
            String kodeOpd,
            String kodeTujuanOpd,
            String tahun,
            String bulan
    ) {
        return new TujuanOpd(null,
                kodeOpd, kodeTujuanOpd, tahun, bulan,
                null, null, null, null);
    }
}
