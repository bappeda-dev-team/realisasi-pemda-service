package cc.kertaskerja.realisasi_pemda_service.sasaran.domain;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("sasarans")
public record Sasaran(
        @Id Long id,

        String sasaranId,
        String sasaran,
        String indikatorId,
        String indikator,
        Double target,
        Double realisasi,
        String satuan,
        String tahun,
        JenisRealisasi jenisRealisasi,
        SasaranStatus status,

        @CreatedDate Instant createdDate,
        @LastModifiedDate Instant lastModifiedDate,
        @Version int version
) {
    public static Sasaran of(
            String sasaranId,
            String sasaran,
            String indikatorId,
            String indikator,
            Double target,
            Double realisasi,
            String satuan,
            String tahun,
            JenisRealisasi jenisRealisasi,
            SasaranStatus status
    ) {
        return new Sasaran(null,
                sasaranId, sasaran, indikatorId, indikator, target, realisasi, satuan, tahun, jenisRealisasi, status,
                null, null, 0);
    }
}
