package cc.kertaskerja.realisasi_individu_service.renja.web.program;

public record RenjaIndividuProgramResponse(
        Long id,
        String kodeOpd,
        String tahun,
        String bulan,
        String nip,
        String kodeProgram,
        String kodeIndikator,
        String kodeTarget,
        String kodePagu,
        Double target,
        Double realisasi,
        String jenisRealisasi,
        Double capaian,
        String keteranganCapaian,
        String faktorPenunjang,
        String faktorPenghambat,
        String createdBy,
        String lastModifiedBy
) {}
