package cc.kertaskerja.realisasi_pemda_service.iku.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;

// IKU Take realisasi tujuan and sasaran
public record Iku(
        String id,
        String indikatorId,
        String indikator,
        String targetId,
        String target,
        Double realisasi,
        String satuan,
        String capaian,
        String tahun,
        JenisRealisasi jenisRealisasi,
        JenisIku jenisIku
) {
    public static Iku of(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, JenisRealisasi jenisRealisasi, JenisIku jenisIku) {
        return new Iku(
                "IKU-" + jenisIku + "-" + indikatorId,
                indikatorId, indikator, targetId,
                target, realisasi, satuan, capaian, tahun,
                jenisRealisasi, jenisIku);
    }
}
