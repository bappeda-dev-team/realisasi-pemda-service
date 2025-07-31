package cc.kertaskerja.capaian.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Capaian {
    private final Double realisasi;
    private final String target;
    private final JenisRealisasi jenisRealisasi;

    public Capaian(Double realisasi, String target, JenisRealisasi jenisRealisasi) {
        this.realisasi = realisasi;
        this.target = target;
        this.jenisRealisasi = jenisRealisasi;
    }

    public Double hasilCapaian() {
        var targets = handleTarget();
        if (targets.isEmpty() || realisasi == 0.0) return 0.0;

        if (targets.size() > 1) {
            if (jenisRealisasi == JenisRealisasi.TURUN) {
                return capaianNegatif(targets);
            }
            return handleMultiTarget(targets);
        }

        // single Target
        var singleTarget = targets.get(0);
        if (singleTarget == 0.0) return 0.0;

        if (jenisRealisasi == JenisRealisasi.TURUN) {
            return 1 + (1 - realisasi / singleTarget) * 100;
        }
        return realisasi / singleTarget * 100;
    }

    private Double capaianNegatif(List<Double> targets) {
        Double realisasiTarget = handleMultiTarget(targets);
        return 1 + (1 - realisasiTarget) * 100;
    }

    private Double handleMultiTarget(List<Double> targets) {
        var minTarget = Collections.min(targets);
        var maxTarget = Collections.max(targets);

        if (realisasi >= maxTarget) return 100.0;
        else if (realisasi > minTarget) {
            return realisasi / maxTarget * 100;
        }
        return  realisasi / minTarget * 100;
    }

    private List<Double> handleTarget() {
        var targets = parseTarget(target);
        if (targets.isEmpty()) return List.of(0.0);
        return targets;
    }

    private List<Double> parseTarget(String target) {
        if (target == null || target.isBlank()) return List.of();
        return Arrays.stream(target.split("[,\\-]"))
                .map(String::trim)
                .map(s -> s.replace(",", "."))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }
}
