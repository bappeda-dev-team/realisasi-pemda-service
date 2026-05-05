package cc.kertaskerja.realisasi_opd_service.sasaran.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.sasaran.web.SasaranOpdRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SasaranOpdService {
    private final SasaranOpdRepository sasaranOpdRepository;

    public SasaranOpdService(SasaranOpdRepository sasaranOpdRepository) {
        this.sasaranOpdRepository = sasaranOpdRepository;
    }

    public Flux<SasaranOpd> getAllRealisasiSasaranOpd() {
        return sasaranOpdRepository.findAll();
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByTahunAndKodeOpd(String tahun, String kodeOpd) {
        return sasaranOpdRepository.findAllByTahunAndKodeOpd(tahun, kodeOpd);
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByKodeOpd(String kodeOpd) {
        return sasaranOpdRepository.findAllByKodeOpd(kodeOpd);
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByRenjaId(String renjaId) {
        return sasaranOpdRepository.findAllByRenjaId(renjaId);
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByIndikatorId(String indikatorId) {
        return sasaranOpdRepository.findAllByIndikatorId(indikatorId);
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByPeriodeRpjmd(String tahunAwal, String tahunAkhir, String kodeOpd) {
        return sasaranOpdRepository.findAllByTahunBetweenAndKodeOpd(tahunAwal, tahunAkhir, kodeOpd);
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByTahunAndRenjaIdAndKodeOpd(String tahun, String renjaId, String kodeOpd) {
        return sasaranOpdRepository.findAllByTahunAndRenjaIdAndKodeOpd(tahun, renjaId, kodeOpd);
    }

    public Flux<SasaranOpd> getRealisasiSasaranOpdByTahunAndBulanAndKodeOpd(String tahun, String bulan, String kodeOpd) {
        return sasaranOpdRepository.findAllByTahunAndBulanAndKodeOpd(tahun, bulan, kodeOpd);
    }

    public Mono<SasaranOpd> getRealisasiSasaranOpdById(Long id) {
        return sasaranOpdRepository.findById(id);
    }

    public Mono<SasaranOpd> submitRealisasiSasaranOpd(String renjaId, String indikatorId, String targetId, String target, Double realisasi, String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi, String kodeOpd, String rumusPerhitungan, String sumberData) {
        return Mono.just(buildUncheckedRealisasiSasaranOpd(renjaId, indikatorId, targetId, target, realisasi, satuan, tahun, bulan, jenisRealisasi, kodeOpd, rumusPerhitungan, sumberData))
                .flatMap(sasaranOpdRepository::save);
    }

    public static SasaranOpd buildUncheckedRealisasiSasaranOpd(String renjaId, String indikatorId, String targetId, String target, Double realisasi, String satuan, String tahun, String bulan, JenisRealisasi jenisRealisasi, String kodeOpd, String rumusPerhitungan, String sumberData) {
        return SasaranOpd.of(
                renjaId,
                "Realisasi Renja Opd " + renjaId,
                indikatorId,
                "Realisasi Indikator Opd " + indikatorId,
                targetId, target, realisasi, satuan, tahun,
                bulan, jenisRealisasi, kodeOpd, rumusPerhitungan, sumberData,
                SasaranOpdStatus.UNCHECKED
        );
    }

    public Flux<SasaranOpd> batchSubmitRealisasiSasaranOpd(@Valid List<SasaranOpdRequest> sasaranOpdRequests) {
        return Flux.fromIterable(sasaranOpdRequests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return sasaranOpdRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> {
                                    SasaranOpd updated = new SasaranOpd(
                                            existing.id(),
                                            existing.renjaId(),
                                            existing.renja(),
                                            existing.indikatorId(),
                                            existing.indikator(),
                                            existing.targetId(),
                                            req.target(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            req.kodeOpd(),
                                            req.rumusPerhitungan(),
                                            req.sumberData(),
                                            SasaranOpdStatus.UNCHECKED,
                                            existing.createdBy(),
                                            existing.createdDate(),
                                            existing.lastModifiedDate(),
                                            existing.lastModifiedBy(),
                                            existing.version()
                                    );
                                    return sasaranOpdRepository.save(updated);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    SasaranOpd baru = buildUncheckedRealisasiSasaranOpd(
                                            req.renjaId(),
                                            req.indikatorId(),
                                            req.targetId(),
                                            req.target(),
                                            req.realisasi(),
                                            req.satuan(),
                                            req.tahun(),
                                            req.bulan(),
                                            req.jenisRealisasi(),
                                            req.kodeOpd(),
                                            req.rumusPerhitungan(),
                                            req.sumberData()
                                    );
                                    return sasaranOpdRepository.save(baru);
                                }));
                    }
                    else {
                        SasaranOpd baru = buildUncheckedRealisasiSasaranOpd(
                                req.renjaId(),
                                req.indikatorId(),
                                req.targetId(),
                                req.target(),
                                req.realisasi(),
                                req.satuan(),
                                req.tahun(),
                                req.bulan(),
                                req.jenisRealisasi(),
                                req.kodeOpd(),
                                req.rumusPerhitungan(),
                                req.sumberData()
                        );
                        return sasaranOpdRepository.save(baru);
                    }
                });
    }
}
