package cc.kertaskerja.realisasi_individu_service.renja.web.subkegiatan;

public record RenjaIndividuSubKegiatanResponse(
        Long id,
        String kodeOpd,
        String tahun,
        String bulan,
        String nip,
        String kodeSubKegiatan,
        String kodeIndikator,
        String kodeTarget,
        String kodePagu,
        Double targetRealisasi,
        Double targetPagu,
        Double realisasiTarget,
        Double realisasiPagu,
        String jenisRealisasi,
        Double capaian,
        String keteranganCapaian,
        Double capaianPagu,
        String keteranganCapaianPagu,
        String faktorPenunjang,
        String faktorPenghambat,
        String createdBy,
        String lastModifiedBy
) {}
