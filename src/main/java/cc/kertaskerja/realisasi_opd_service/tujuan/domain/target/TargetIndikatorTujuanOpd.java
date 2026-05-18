package cc.kertaskerja.realisasi_opd_service.tujuan.domain.target;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Table("target_indikator_tujuan_opd")
public record TargetIndikatorTujuanOpd(
        @Id Long id,
        Long indikatorTujuanId,
        String kodeTarget,
        BigDecimal realisasi,
        String tahun,
        String bulan,
        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @CreatedBy String createdBy,
        @LastModifiedBy String lastModifiedBy
) {
}
