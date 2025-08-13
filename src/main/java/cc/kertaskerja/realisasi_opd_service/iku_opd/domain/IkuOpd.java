package cc.kertaskerja.realisasi_opd_service.iku_opd.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;

public record IkuOpd(
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
        JenisIkuOpd jenisIkuOpd,
        String kodeOpd
) {
   public static IkuOpd of(String indikatorId, String indikator, String targetId, String target,
                           Double realisasi, String satuan, String capaian, String tahun,
                           JenisRealisasi jenisRealisasi, JenisIkuOpd jenisIkuOpd,
                           String kodeOpd) {
       return new IkuOpd(
                         "IKU-OPD-" + jenisIkuOpd + "-" + indikatorId,
                         indikatorId, indikator, targetId,
                         target, realisasi, satuan, capaian, tahun,
                         jenisRealisasi, jenisIkuOpd, kodeOpd);
   }
}
