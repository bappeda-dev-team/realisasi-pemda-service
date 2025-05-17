package cc.kertaskerja.realisasi_pemda_service.tujuan.domain;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("tujuans")
public record Tujuan(
        @Id Long id,

        String tujuanId,
        String tujuan,
        String indikatorId,
        String indikator,
        Double target,
        Double realisasi,
        String satuan,
        String tahun,
        JenisRealisasi jenisRealisasi,
        TujuanStatus status,

        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,

        @Version int version
) {
    public static Tujuan of(
            String tujuanId,
            String tujuan,
            String indikatorId,
            String indikator,
            Double target,
            Double realisasi,
            String satuan,
            String tahun,
            JenisRealisasi jenisRealisasi,
            TujuanStatus status
    ) {
        return new Tujuan(null,
                tujuanId, tujuan, indikatorId, indikator, target, realisasi, satuan, tahun, jenisRealisasi, status,
                null, null, 0);
    }

    public Double capaian() {
        if (realisasi == null) {
            return 0.0;
        }
        return realisasi / target * 100;
    }

    public String capaianString() {
        return String.format("%.2f", capaian());
    }
}
