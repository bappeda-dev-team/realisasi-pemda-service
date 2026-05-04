package cc.kertaskerja.realisasi_opd_service.renja_pagu.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.renja_pagu.web.RenjaPaguRequest;
import cc.kertaskerja.renja.domain.JenisRenja;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class RenjaPaguService {
    private final RenjaPaguRepository renjaPaguRepository;

    public RenjaPaguService(RenjaPaguRepository renjaPaguRepository) {
        this.renjaPaguRepository = renjaPaguRepository;
    }

    public Flux<RenjaPagu> getAllRealisasiRenjaPagu() {
        return renjaPaguRepository.findAll();
    }

    public Flux<RenjaPagu> getRealisasiRenjaPaguByTahunAndBulanAndKodeOpd(String tahun, String bulan, String kodeOpd) {
        return renjaPaguRepository.findAllByTahunAndBulanAndKodeOpd(tahun, bulan, kodeOpd);
    }

    public Flux<RenjaPagu> getRealisasiRenjaPaguByKodeOpdAndTahunAndBulanAndJenisRenjaAndKodeRenjaAndRenjaId(
            String kodeOpd, String tahun, String bulan, String jenisRenja, String kodeRenja, String jenisRenjaId) {
        return renjaPaguRepository.findAllByKodeOpdAndTahunAndBulanAndJenisRenjaPaguAndKodeRenjaAndJenisRenjaId(
                kodeOpd, tahun, bulan, jenisRenja, kodeRenja, jenisRenjaId);
    }

    public Mono<Void> deleteRealisasiRenjaPaguByRenjaId(String jenisRenjaId) {
        return renjaPaguRepository.deleteByJenisRenjaId(jenisRenjaId);
    }

    public Mono<RenjaPagu> submitRealisasiRenjaPagu(String jenisRenjaId, JenisRenja jenisRenja, Integer pagu, Integer realisasi, String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi, String kodeOpd, String kodeRenja) {
        return Mono.just(buildUncheckedRealisasiRenjaPagu(jenisRenjaId, jenisRenja, pagu, realisasi, satuan, tahun, bulan, jenisRealisasi, kodeOpd, kodeRenja))
                .flatMap(renjaPaguRepository::save);
    }

    public static RenjaPagu buildUncheckedRealisasiRenjaPagu(String jenisRenjaId, JenisRenja jenisRenjaPagu, Integer pagu, Integer realisasi, String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi, String kodeOpd, String kodeRenja) {
        return RenjaPagu.of(
                jenisRenjaId,
                jenisRenjaPagu, pagu, realisasi, satuan, tahun, bulan,
                jenisRealisasi, kodeOpd, kodeRenja,
                RenjaPaguStatus.UNCHECKED
        );
    }

    public Flux<RenjaPagu> batchSubmitRealisasiRenjaPagu(@Valid List<RenjaPaguRequest> renjaPaguRequests) {
        return Flux.fromIterable(renjaPaguRequests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return renjaPaguRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> {
                                    RenjaPagu updated = new RenjaPagu(
                                            existing.id(),
                                            existing.jenisRenjaId(),
                                            existing.jenisRenjaPagu(),
                                            req.pagu(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            existing.kodeOpd(),
                                            existing.kodeRenja(),
                                            RenjaPaguStatus.UNCHECKED,
                                            existing.createdBy(),
                                            existing.createdDate(),
                                            existing.lastModifiedDate(),
                                            existing.lastModifiedBy(),
                                            existing.version()
                                    );
                                    return renjaPaguRepository.save(updated);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    RenjaPagu baru = buildUncheckedRealisasiRenjaPagu(
                                            req.jenisRenjaId(),
                                            req.jenisRenja(),
                                            req.pagu(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            req.kodeOpd(),
                                            req.kodeRenja()
                                    );
                                    return renjaPaguRepository.save(baru);
                                }));
                    }
                    else {
                        RenjaPagu baru = buildUncheckedRealisasiRenjaPagu(
                                req.jenisRenjaId(),
                                req.jenisRenja(),
                                req.pagu(),
                                req.realisasi(),
                                req.satuan(),
                                req.tahun(),
                                req.bulan(),
                                req.jenisRealisasi(),
                                req.kodeOpd(),
                                req.kodeRenja()
                        );
                        return renjaPaguRepository.save(baru);
                    }
                });
    }
}
