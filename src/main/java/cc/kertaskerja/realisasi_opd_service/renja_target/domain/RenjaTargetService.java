package cc.kertaskerja.realisasi_opd_service.renja_target.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.renja_target.web.RenjaTargetRequest;
import cc.kertaskerja.renja.domain.JenisRenja;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RenjaTargetService {
    private final RenjaTargetRepository renjaTargetRepository;

    public RenjaTargetService(RenjaTargetRepository renjaTargetRepository) {
        this.renjaTargetRepository = renjaTargetRepository;
    }

    public Flux<RenjaTarget> getAllRealisasiRenjaTarget() {
        return renjaTargetRepository.findAll();
    }

public Mono<RenjaTarget> submitRealisasiRenjaTarget(String renjaTargetId, String renjaTarget,
            JenisRenja jenisRenjaTarget,
            String indikatorId, String indikator,
            String targetId, String target, Integer realisasi,
            String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi,
            String kodeOpd, String kodeRenja) {
        return Mono.just(buildUncheckedRealisasiRenjaTarget(
                renjaTargetId, renjaTarget, jenisRenjaTarget, indikatorId, indikator, targetId, target,
                realisasi, satuan, tahun, bulan, jenisRealisasi, kodeOpd, kodeRenja))
                .flatMap(renjaTargetRepository::save);
    }

    public static RenjaTarget buildUncheckedRealisasiRenjaTarget(String renjaTargetId, String renjaTarget,
            JenisRenja jenisRenjaTarget,
            String indikatorId, String indikator,
            String targetId, String target, Integer realisasi,
            String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi,
            String kodeOpd, String kodeRenja) {
        return RenjaTarget.of(
                renjaTargetId,
                renjaTarget,
                jenisRenjaTarget,
                indikatorId,
                indikator,
                targetId,
                target,
                realisasi,
                satuan,
                tahun,
                bulan,
                jenisRealisasi,
                kodeOpd,
                kodeRenja,
                RenjaTargetStatus.UNCHECKED);
    }

public Flux<RenjaTarget> getRealisasiRenjaTargetByFilters(String kodeOpd, String tahun, String bulan) {
        return renjaTargetRepository.findAllByTahunAndBulanAndKodeOpd(tahun, bulan, kodeOpd);
    }

    public Flux<RenjaTarget> batchSubmitRealisasiRenjaTarget(@Valid List<RenjaTargetRequest> renjaTargetRequests) {
        return Flux.fromIterable(renjaTargetRequests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return renjaTargetRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> {
                                    RenjaTarget updated = new RenjaTarget(
                                            existing.id(),
                                            existing.renjaTargetId(),
                                            existing.renjaTarget(),
                                            existing.jenisRenjaTarget(),
                                            existing.indikatorId(),
                                            existing.indikator(),
                                            existing.targetId(),
                                            req.target(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            existing.kodeOpd(),
                                            existing.kodeRenja(),
                                            RenjaTargetStatus.UNCHECKED,
                                            existing.createdBy(),
                                            existing.createdDate(),
                                            existing.lastModifiedDate(),
                                            existing.lastModifiedBy(),
                                            existing.version());
                                    return renjaTargetRepository.save(updated);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    RenjaTarget baru = buildUncheckedRealisasiRenjaTarget(
                                            req.renjaTargetId(),
                                            req.renjaTarget(),
                                            req.jenisRenjaTarget(),
                                            req.indikatorId(),
                                            req.indikator(),
                                            req.targetId(),
                                            req.target(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            req.kodeOpd(),
                                            req.kodeRenja());
                                    return renjaTargetRepository.save(baru);
                                }));
                    } else {
                        RenjaTarget baru = buildUncheckedRealisasiRenjaTarget(
                                req.renjaTargetId(),
                                req.renjaTarget(),
                                req.jenisRenjaTarget(),
                                req.indikatorId(),
                                req.indikator(),
                                req.targetId(),
                                req.target(),
                                req.realisasi(),
                                req.satuan(),
                                req.tahun(),
                                req.bulan(),
                                req.jenisRealisasi(),
                                req.kodeOpd(),
                                req.kodeRenja());
                        return renjaTargetRepository.save(baru);
                    }
                });
    }

    public Mono<Void> deleteRealisasiRenjaTarget(String renjaId) {
        return renjaTargetRepository.deleteByRenjaTargetId(renjaId);
    }

    public Mono<RenjaTarget> getRealisasiRenjaTargetByFilters(
            String kodeOpd, String tahun, String bulan, 
            JenisRenja jenisRenja, String kodeRenja, String renjaId) {
        return renjaTargetRepository.findFirstByKodeOpdAndTahunAndBulanAndJenisRenjaTargetAndKodeRenjaAndRenjaTargetId(
                kodeOpd, tahun, bulan, jenisRenja, kodeRenja, renjaId);
    }
}
