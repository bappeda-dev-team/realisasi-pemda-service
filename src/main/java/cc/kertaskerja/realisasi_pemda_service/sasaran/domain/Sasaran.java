package cc.kertaskerja.realisasi_pemda_service.sasaran.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Table("sasarans")
public record Sasaran(
        @Id Long id,

        String sasaranId,
        String sasaran,
        String indikatorId,
        String indikator,
        String targetId,
        String target,
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
            String targetId,
            String target,
            Double realisasi,
            String satuan,
            String tahun,
            JenisRealisasi jenisRealisasi,
            SasaranStatus status
    ) {
        return new Sasaran(null,
                sasaranId, sasaran, indikatorId, indikator,
                targetId, target,
                realisasi, satuan, tahun, jenisRealisasi, status,
                null, null, 0);
    }

    @JsonProperty("capaian")
    public String capaian() {
        return String.format("%.2f%%", capaianSasaran());
    }

    public Double capaianSasaran() {
        var targets = handleTarget();
        if (targets.isEmpty() || realisasi == 0.0) return 0.0;
        if (targets.size() > 1) {
            return handleMultiTarget(targets);
        }
        var singleTarget = targets.get(0);
        return realisasi / singleTarget * 100;
    }

    private Double handleMultiTarget(List<Double> targets) {
        var minTarget = targets.get(0);
        return  realisasi / minTarget * 100;
    }

    private List<Double> handleTarget() {
        var targets = parseTarget(target);
        if (targets.isEmpty()) return List.of(0.0);
        return targets;
    }

    private List<Double> parseTarget(String target) {
        if (target == null || target.isBlank()) return List.of();
        return Arrays.stream(target.split("-"))
                .map(String::trim)
                .map(s -> s.replace(",", "."))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }
}
