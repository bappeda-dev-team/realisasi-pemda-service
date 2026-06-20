package cc.kertaskerja.realisasi_individu_service.renja.domain;

import cc.kertaskerja.realisasi_individu_service.renja.domain.kegiatan.RenjaKegiatanIndividu;
import cc.kertaskerja.realisasi_individu_service.renja.domain.kegiatan.RenjaKegiatanIndividuRepository;
import cc.kertaskerja.realisasi_individu_service.renja.domain.program.RenjaProgramIndividu;
import cc.kertaskerja.realisasi_individu_service.renja.domain.program.RenjaProgramIndividuRepository;
import cc.kertaskerja.realisasi_individu_service.renja.domain.subkegiatan.RenjaSubKegiatanIndividu;
import cc.kertaskerja.realisasi_individu_service.renja.domain.subkegiatan.RenjaSubKegiatanIndividuRepository;
import cc.kertaskerja.realisasi_individu_service.renja.web.kegiatan.FaktorPenghambatTargetRenjaKegiatanRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.kegiatan.FaktorPenunjangTargetRenjaKegiatanRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.kegiatan.RenjaIndividuKegiatanRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.kegiatan.RenjaIndividuKegiatanResponse;
import cc.kertaskerja.realisasi_individu_service.renja.web.program.FaktorPenghambatTargetRenjaProgramRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.program.FaktorPenunjangTargetRenjaProgramRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.program.RenjaIndividuProgramRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.program.RenjaIndividuProgramResponse;
import cc.kertaskerja.realisasi_individu_service.renja.web.subkegiatan.FaktorPenghambatTargetRenjaSubKegiatanRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.subkegiatan.FaktorPenunjangTargetRenjaSubKegiatanRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.subkegiatan.RenjaIndividuSubKegiatanRequest;
import cc.kertaskerja.realisasi_individu_service.renja.web.subkegiatan.RenjaIndividuSubKegiatanResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class RenjaIndividuService {
    private final RenjaProgramIndividuRepository programRepo;
    private final RenjaKegiatanIndividuRepository kegiatanRepo;
    private final RenjaSubKegiatanIndividuRepository subKegiatanRepo;

    record CapaianResult(Double capaian, String keteranganCapaian) {}

    public RenjaIndividuService(
            RenjaProgramIndividuRepository programRepo,
            RenjaKegiatanIndividuRepository kegiatanRepo,
            RenjaSubKegiatanIndividuRepository subKegiatanRepo
    ) {
        this.programRepo = programRepo;
        this.kegiatanRepo = kegiatanRepo;
        this.subKegiatanRepo = subKegiatanRepo;
    }

    @Transactional
    public Mono<RenjaIndividuProgramResponse> submitProgram(RenjaIndividuProgramRequest req) {
        return upsertProgram(req)
                .flatMap(this::enrichProgramResponse);
    }

    @Transactional
    public Mono<RenjaIndividuKegiatanResponse> submitKegiatan(RenjaIndividuKegiatanRequest req) {
        return upsertKegiatan(req)
                .flatMap(this::enrichKegiatanResponse);
    }

    @Transactional
    public Mono<RenjaIndividuSubKegiatanResponse> submitSubKegiatan(RenjaIndividuSubKegiatanRequest req) {
        return upsertSubKegiatan(req)
                .flatMap(this::enrichSubKegiatanResponse);
    }

    public Mono<RenjaProgramIndividu> updateFaktorPenunjangProgram(FaktorPenunjangTargetRenjaProgramRequest req) {
        return programRepo.findByKodeOpdAndKodeProgramAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeProgram(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target program individu tidak ditemukan")))
                .flatMap(existing -> programRepo.save(existing.withFaktorPenunjang(req.faktorPenunjang())));
    }

    public Mono<RenjaProgramIndividu> updateFaktorPenghambatProgram(FaktorPenghambatTargetRenjaProgramRequest req) {
        return programRepo.findByKodeOpdAndKodeProgramAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeProgram(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target program individu tidak ditemukan")))
                .flatMap(existing -> programRepo.save(existing.withFaktorPenghambat(req.faktorPenghambat())));
    }

    public Mono<RenjaKegiatanIndividu> updateFaktorPenunjangKegiatan(FaktorPenunjangTargetRenjaKegiatanRequest req) {
        return kegiatanRepo.findByKodeOpdAndKodeKegiatanAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeKegiatan(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target kegiatan individu tidak ditemukan")))
                .flatMap(existing -> kegiatanRepo.save(existing.withFaktorPenunjang(req.faktorPenunjang())));
    }

    public Mono<RenjaKegiatanIndividu> updateFaktorPenghambatKegiatan(FaktorPenghambatTargetRenjaKegiatanRequest req) {
        return kegiatanRepo.findByKodeOpdAndKodeKegiatanAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeKegiatan(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target kegiatan individu tidak ditemukan")))
                .flatMap(existing -> kegiatanRepo.save(existing.withFaktorPenghambat(req.faktorPenghambat())));
    }

    public Mono<RenjaSubKegiatanIndividu> updateFaktorPenunjangSubKegiatan(FaktorPenunjangTargetRenjaSubKegiatanRequest req) {
        return subKegiatanRepo.findByKodeOpdAndKodeSubKegiatanAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeSubKegiatan(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target subkegiatan individu tidak ditemukan")))
                .flatMap(existing -> subKegiatanRepo.save(existing.withFaktorPenunjang(req.faktorPenunjang())));
    }

    public Mono<RenjaSubKegiatanIndividu> updateFaktorPenghambatSubKegiatan(FaktorPenghambatTargetRenjaSubKegiatanRequest req) {
        return subKegiatanRepo.findByKodeOpdAndKodeSubKegiatanAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeSubKegiatan(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Target subkegiatan individu tidak ditemukan")))
                .flatMap(existing -> subKegiatanRepo.save(existing.withFaktorPenghambat(req.faktorPenghambat())));
    }

    public Flux<RenjaIndividuProgramResponse> getProgramByKodeOpdAndNipAndTahunAndBulan(
            String kodeOpd, String nip, String tahun, String bulan) {
        return programRepo
                .findAllByKodeOpdAndNipAndTahunAndBulan(kodeOpd, nip, tahun, bulan)
                .flatMap(this::enrichProgramResponse);
    }

    public Flux<RenjaIndividuKegiatanResponse> getKegiatanByKodeOpdAndNipAndTahunAndBulan(
            String kodeOpd, String nip, String tahun, String bulan) {
        return kegiatanRepo
                .findAllByKodeOpdAndNipAndTahunAndBulan(kodeOpd, nip, tahun, bulan)
                .flatMap(this::enrichKegiatanResponse);
    }

    public Flux<RenjaIndividuSubKegiatanResponse> getSubKegiatanByKodeOpdAndNipAndTahunAndBulan(
            String kodeOpd, String nip, String tahun, String bulan) {
        return subKegiatanRepo
                .findAllByKodeOpdAndNipAndTahunAndBulan(kodeOpd, nip, tahun, bulan)
                .flatMap(this::enrichSubKegiatanResponse);
    }

    private Mono<RenjaProgramIndividu> upsertProgram(RenjaIndividuProgramRequest req) {
        String kodePagu = req.kodePagu() != null ? req.kodePagu() : "";
        String jenisRealisasi = req.jenisRealisasi() != null ? req.jenisRealisasi() : "NAIK";
        return programRepo.findByKodeOpdAndKodeProgramAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeProgram(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .flatMap(existing -> programRepo.save(new RenjaProgramIndividu(
                        existing.id(), existing.kodeOpd(), existing.nip(),
                        existing.tahun(), existing.bulan(),
                        existing.kodeProgram(), existing.kodeIndikator(), existing.kodeTarget(),
                        existing.kodePagu(), BigDecimal.valueOf(req.target()), BigDecimal.valueOf(req.realisasi()), jenisRealisasi,
                        existing.faktorPenunjang(), existing.faktorPenghambat(),
                        existing.createdDate(), null, existing.createdBy(), null
                )))
                .switchIfEmpty(Mono.defer(() -> programRepo.save(new RenjaProgramIndividu(
                        null, req.kodeOpd(), req.nip(),
                        req.tahun(), req.bulan(),
                        req.kodeProgram(), req.kodeIndikator(), req.kodeTarget(),
                        kodePagu, BigDecimal.valueOf(req.target()), BigDecimal.valueOf(req.realisasi()), jenisRealisasi,
                        "", "",
                        null, null, null, null
                ))));
    }

    private Mono<RenjaKegiatanIndividu> upsertKegiatan(RenjaIndividuKegiatanRequest req) {
        String kodePagu = req.kodePagu() != null ? req.kodePagu() : "";
        String jenisRealisasi = req.jenisRealisasi() != null ? req.jenisRealisasi() : "NAIK";
        return kegiatanRepo.findByKodeOpdAndKodeKegiatanAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeKegiatan(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .flatMap(existing -> kegiatanRepo.save(new RenjaKegiatanIndividu(
                        existing.id(), existing.kodeOpd(), existing.nip(),
                        existing.tahun(), existing.bulan(),
                        existing.kodeKegiatan(), existing.kodeIndikator(), existing.kodeTarget(),
                        existing.kodePagu(), BigDecimal.valueOf(req.target()), BigDecimal.valueOf(req.realisasi()), jenisRealisasi,
                        existing.faktorPenunjang(), existing.faktorPenghambat(),
                        existing.createdDate(), null, existing.createdBy(), null
                )))
                .switchIfEmpty(Mono.defer(() -> kegiatanRepo.save(new RenjaKegiatanIndividu(
                        null, req.kodeOpd(), req.nip(),
                        req.tahun(), req.bulan(),
                        req.kodeKegiatan(), req.kodeIndikator(), req.kodeTarget(),
                        kodePagu, BigDecimal.valueOf(req.target()), BigDecimal.valueOf(req.realisasi()), jenisRealisasi,
                        "", "",
                        null, null, null, null
                ))));
    }

    private Mono<RenjaSubKegiatanIndividu> upsertSubKegiatan(RenjaIndividuSubKegiatanRequest req) {
        String kodePagu = req.kodePagu() != null ? req.kodePagu() : "";
        String jenisRealisasi = req.jenisRealisasi() != null ? req.jenisRealisasi() : "NAIK";
        return subKegiatanRepo.findByKodeOpdAndKodeSubKegiatanAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
                        req.kodeOpd(), req.kodeSubKegiatan(), req.kodeIndikator(), req.kodeTarget(), req.tahun(), req.bulan())
                .flatMap(existing -> subKegiatanRepo.save(new RenjaSubKegiatanIndividu(
                        existing.id(), existing.kodeOpd(), existing.nip(),
                        existing.tahun(), existing.bulan(),
                        existing.kodeSubKegiatan(), existing.kodeIndikator(), existing.kodeTarget(),
                        existing.kodePagu(), BigDecimal.valueOf(req.targetRealisasi()), BigDecimal.valueOf(req.targetPagu()), BigDecimal.valueOf(req.realisasiTarget()), BigDecimal.valueOf(req.realisasiPagu()), jenisRealisasi,
                        existing.faktorPenunjang(), existing.faktorPenghambat(),
                        existing.createdDate(), null, existing.createdBy(), null
                )))
                .switchIfEmpty(Mono.defer(() -> subKegiatanRepo.save(new RenjaSubKegiatanIndividu(
                        null, req.kodeOpd(), req.nip(),
                        req.tahun(), req.bulan(),
                        req.kodeSubKegiatan(), req.kodeIndikator(), req.kodeTarget(),
                        kodePagu, BigDecimal.valueOf(req.targetRealisasi()), BigDecimal.valueOf(req.targetPagu()), BigDecimal.valueOf(req.realisasiTarget()), BigDecimal.valueOf(req.realisasiPagu()), jenisRealisasi,
                        "", "",
                        null, null, null, null
                ))));
    }

    private Mono<RenjaIndividuProgramResponse> enrichProgramResponse(RenjaProgramIndividu saved) {
        var capaianResult = hitungCapaian(
                saved.realisasi() != null ? saved.realisasi().doubleValue() : null,
                saved.target() != null ? saved.target().doubleValue() : null);
        return Mono.just(new RenjaIndividuProgramResponse(
                saved.id(), saved.kodeOpd(), saved.tahun(), saved.bulan(), saved.nip(),
                saved.kodeProgram(), saved.kodeIndikator(), saved.kodeTarget(),
                saved.kodePagu(), saved.target() != null ? saved.target().doubleValue() : null,
                saved.realisasi() != null ? saved.realisasi().doubleValue() : null,
                "NAIK",
                capaianResult.capaian(), capaianResult.keteranganCapaian(),
                saved.faktorPenunjang(), saved.faktorPenghambat(),
                saved.createdBy(), saved.lastModifiedBy()
        ));
    }

    private Mono<RenjaIndividuKegiatanResponse> enrichKegiatanResponse(RenjaKegiatanIndividu saved) {
        var capaianResult = hitungCapaian(
                saved.realisasi() != null ? saved.realisasi().doubleValue() : null,
                saved.target() != null ? saved.target().doubleValue() : null);
        return Mono.just(new RenjaIndividuKegiatanResponse(
                saved.id(), saved.kodeOpd(), saved.tahun(), saved.bulan(), saved.nip(),
                saved.kodeKegiatan(), saved.kodeIndikator(), saved.kodeTarget(),
                saved.kodePagu(), saved.target() != null ? saved.target().doubleValue() : null,
                saved.realisasi() != null ? saved.realisasi().doubleValue() : null,
                "NAIK",
                capaianResult.capaian(), capaianResult.keteranganCapaian(),
                saved.faktorPenunjang(), saved.faktorPenghambat(),
                saved.createdBy(), saved.lastModifiedBy()
        ));
    }

    private Mono<RenjaIndividuSubKegiatanResponse> enrichSubKegiatanResponse(RenjaSubKegiatanIndividu saved) {
        var capaianFisik = hitungCapaian(
                saved.realisasiTarget() != null ? saved.realisasiTarget().doubleValue() : null,
                saved.targetRealisasi() != null ? saved.targetRealisasi().doubleValue() : null);
        var capaianPagu = hitungCapaian(
                saved.realisasiPagu() != null ? saved.realisasiPagu().doubleValue() : null,
                saved.targetPagu() != null ? saved.targetPagu().doubleValue() : null);
        return Mono.just(new RenjaIndividuSubKegiatanResponse(
                saved.id(), saved.kodeOpd(), saved.tahun(), saved.bulan(), saved.nip(),
                saved.kodeSubKegiatan(), saved.kodeIndikator(), saved.kodeTarget(),
                saved.kodePagu(), saved.targetRealisasi() != null ? saved.targetRealisasi().doubleValue() : null,
                saved.targetPagu() != null ? saved.targetPagu().doubleValue() : null,
                saved.realisasiTarget() != null ? saved.realisasiTarget().doubleValue() : null,
                saved.realisasiPagu() != null ? saved.realisasiPagu().doubleValue() : null,
                "NAIK",
                capaianFisik.capaian(), capaianFisik.keteranganCapaian(),
                capaianPagu.capaian(), capaianPagu.keteranganCapaian(),
                saved.faktorPenunjang(), saved.faktorPenghambat(),
                saved.createdBy(), saved.lastModifiedBy()
        ));
    }

    static CapaianResult hitungCapaian(Double realisasi, Double target) {
        if (realisasi == null || target == null || target == 0) {
            return new CapaianResult(null, null);
        }
        double calculated = realisasi / target * 100;
        String keterangan = null;
        if (calculated > 100) {
            keterangan = "nilai capaian lebih dari 100% (" + String.format("%.2f%%", calculated) + ")";
        }
        return new CapaianResult(Math.min(calculated, 100), keterangan);
    }
}
