package cc.kertaskerja.realisasi_opd_service.tujuan.domain.indikator;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("indikator_tujuan_opd")
public record IndikatorTujuanOpd(
        @Id Long id,
        Long tujuanOpdId,
        String kodeIndikator,
        String kodeOpd,
        String tahun,
        String bulan,
        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @CreatedBy String createdBy,
        @LastModifiedBy String lastModifiedBy
) {
}
