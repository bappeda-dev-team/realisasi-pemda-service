package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.tujuan.web.TujuanOpdRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TujuanOpdService {
    private final TujuanOpdRepository tujuanOpdRepository;

    public TujuanOpdService(TujuanOpdRepository tujuanOpdRepository) {
        this.tujuanOpdRepository = tujuanOpdRepository;
    }

    public Flux<TujuanOpd> getAllRealisasiTujuanOpd() {
        return tujuanOpdRepository.findAll();
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByTahunAndTujuanIdAndKodeOpd(String tahun, String tujuanId, String kodeOpd) {
        return tujuanOpdRepository.findAllByTahunAndTujuanIdAndKodeOpd(tahun, tujuanId, kodeOpd);
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByTahunAndKodeOpdAndBulan(String tahun, String kodeOpd, String bulan) {
        return tujuanOpdRepository.findAllByTahunAndKodeOpdAndBulan(tahun, kodeOpd, bulan);
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByKodeOpdAndTahunAndBulanAndTargetIdAndIndikatorIdAndTujuanId(
            String kodeOpd,
            String tahun,
            String bulan,
            String targetId,
            String indikatorId,
            String tujuanId
    ) {
        return tujuanOpdRepository.findAllByKodeOpdAndTahunAndBulanAndTargetIdAndIndikatorIdAndTujuanId(
                kodeOpd,
                tahun,
                bulan,
                targetId,
                indikatorId,
                tujuanId
        );
    }

    public Mono<TujuanOpd> submitRealisasiTujuanOpd(String tujuanId, String kodeTujuanOpd, String indikatorId, String kodeIndikatorTujuanOpd, String targetId, String kodeTargetTujuanOpd, String target, Double realisasi, String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi, String kodeOpd, String rumusPerhitungan, String sumberData, String definisiOperational) {
        return Mono.just(buildUncheckedRealisasiTujuanOpd(tujuanId, kodeTujuanOpd, indikatorId, kodeIndikatorTujuanOpd, targetId, kodeTargetTujuanOpd, target, realisasi, satuan, tahun, bulan, jenisRealisasi, kodeOpd, rumusPerhitungan, sumberData, definisiOperational))
                .flatMap(tujuanOpdRepository::save);
    }

    public Flux<TujuanOpd> batchSubmitRealisasiTujuanOpd(List<TujuanOpdRequest> tujuanOpdRequests) {
        return Flux.fromIterable(tujuanOpdRequests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return tujuanOpdRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> {
                                    TujuanOpd updated = new TujuanOpd(
                                            existing.id(),
                                            req.tujuanId(),
                                            req.kodeTujuanOpd(),
                                            existing.tujuan(),
                                            req.indikatorId(),
                                            req.kodeIndikatorTujuanOpd(),
                                            existing.indikator(),
                                            req.targetId(),
                                            req.kodeTargetTujuanOpd(),
                                            req.target(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            req.kodeOpd(),
                                            req.rumusPerhitungan(),
                                            req.sumberData(),
                                            req.definisiOperational(),
                                            TujuanOpdStatus.UNCHECKED,
                                            existing.createdBy(),
                                            existing.createdDate(),
                                            existing.lastModifiedDate(),
                                            existing.lastModifiedBy(),
                                            existing.version()
                                    );
                                    return tujuanOpdRepository.save(updated);
                                })
                                .switchIfEmpty(Mono.defer(() -> tujuanOpdRepository.save(
                                        buildUncheckedRealisasiTujuanOpd(
                                                req.tujuanId(),
                                                req.kodeTujuanOpd(),
                                                req.indikatorId(),
                                                req.kodeIndikatorTujuanOpd(),
                                                req.targetId(),
                                                req.kodeTargetTujuanOpd(),
                                                req.target(),
                                                req.realisasi(),
                                                req.satuan(),
                                                req.tahun(),
                                                req.bulan(),
                                                req.jenisRealisasi(),
                                                req.kodeOpd(),
                                                req.rumusPerhitungan(),
                                                req.sumberData(),
                                                req.definisiOperational()
                                        ))));
                    }
                    return tujuanOpdRepository.save(
                            buildUncheckedRealisasiTujuanOpd(
                                    req.tujuanId(),
                                    req.kodeTujuanOpd(),
                                    req.indikatorId(),
                                    req.kodeIndikatorTujuanOpd(),
                                    req.targetId(),
                                    req.kodeTargetTujuanOpd(),
                                    req.target(),
                                    req.realisasi(),
                                    req.satuan(),
                                    req.tahun(),
                                    req.bulan(),
                                    req.jenisRealisasi(),
                                    req.kodeOpd(),
                                    req.rumusPerhitungan(),
                                    req.sumberData(),
                                    req.definisiOperational()
                            ));
                });
    }

    public static TujuanOpd buildUncheckedRealisasiTujuanOpd(String tujuanId, String kodeTujuanOpd, String indikatorId, String kodeIndikatorTujuanOpd, String targetId, String kodeTargetTujuanOpd, String target, Double realisasi, String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi, String kodeOpd, String rumusPerhitungan, String sumberData, String definisiOperational) {
        return TujuanOpd.of(
                tujuanId,
                kodeTujuanOpd,
                "Realisasi Tujuan Opd " + tujuanId,
                indikatorId,
                kodeIndikatorTujuanOpd,
                "Realisasi Indikator Opd " + indikatorId,
                targetId,
                kodeTargetTujuanOpd,
                target, realisasi, satuan, tahun, bulan,
                jenisRealisasi, kodeOpd, rumusPerhitungan, sumberData, definisiOperational,
                TujuanOpdStatus.UNCHECKED
        );
    }
}
