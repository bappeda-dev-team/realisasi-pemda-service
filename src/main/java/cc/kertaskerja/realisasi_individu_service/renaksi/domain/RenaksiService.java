package cc.kertaskerja.realisasi_individu_service.renaksi.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_individu_service.renaksi.web.RenaksiRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RenaksiService {
    private final RenaksiRepository renaksiRepository;

    public RenaksiService(RenaksiRepository renaksiRepository) {
        this.renaksiRepository = renaksiRepository;
    }

    public Flux<Renaksi> getAllRealisasiRenaksi() {
        return renaksiRepository.findAll();
    }

    public Mono<Renaksi> getRealisasiRenaksiById(Long id) {
        return renaksiRepository.findById(id);
    }

    public Flux<Renaksi> getRealisasiRenaksiByRenaksiId(String renaksiId) {
        return renaksiRepository.findAllByRenaksiId(renaksiId);
    }

    public Flux<Renaksi> getRealisasiRenaksiByRekinId(String rekinId) {
        return renaksiRepository.findAllByRekinId(rekinId);
    }

    public Flux<Renaksi> getRealisasiRenaksiByTahun(String tahun) {
        return renaksiRepository.findAllByTahun(tahun);
    }

    public Flux<Renaksi> getRealisasiRenaksiByNip(String nip) {
        return renaksiRepository.findAllByNip(nip);
    }

    public Flux<Renaksi> getRealisasiRenaksiByPeriodeRpjmd(String tahunAwal, String tahunAkhir) {
        return renaksiRepository.findAllByTahunBetween(tahunAwal, tahunAkhir);
    }

    public Flux<Renaksi> getRealisasiRenaksiByBulanAndTahunAndRenaksiIdAndTargetId(
            String bulan,
            String tahun,
            String renaksiId,
            String targetId) {
        return renaksiRepository.findAllByBulanAndTahunAndRenaksiIdAndTargetId(bulan, tahun, renaksiId, targetId);
    }

    public Mono<Renaksi> submitRealisasiRenaksi(
            String renaksiId,
            String renaksi,
            String nip,
            String rekinId,
            String rekin,
            String targetId,
            String target,
            Integer realisasi,
            String satuan,
            String bulan,
            String tahun,
            JenisRealisasi jenisRealisasi) {
        return Mono.just(buildUncheckedRealisasiRenaksi(
                        renaksiId,
                        renaksi,
                        nip,
                        rekinId,
                        rekin,
                        targetId,
                        target,
                        realisasi,
                        satuan,
                        bulan,
                        tahun,
                        jenisRealisasi))
                .flatMap(renaksiRepository::save);
    }

    public Mono<Void> deleteRealisasiRenaksi(Long id) {
        return renaksiRepository.deleteById(id);
    }

    public static Renaksi buildUncheckedRealisasiRenaksi(
            String renaksiId,
            String renaksi,
            String nip,
            String rekinId,
            String rekin,
            String targetId,
            String target,
            Integer realisasi,
            String satuan,
            String bulan,
            String tahun,
            JenisRealisasi jenisRealisasi) {
        return Renaksi.of(
                renaksiId,
                renaksi,
                nip,
                rekinId,
                rekin,
                targetId,
                target,
                realisasi,
                satuan,
                bulan,
                tahun,
                jenisRealisasi,
                RenaksiStatus.UNCHECKED);
    }

    public Flux<Renaksi> batchSubmitRealisasiRenaksi(@Valid List<RenaksiRequest> renaksiRequests) {
        return Flux.fromIterable(renaksiRequests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return renaksiRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> renaksiRepository.save(buildUpdatedRealisasiRenaksi(existing, req)))
                                .switchIfEmpty(Mono.defer(() -> renaksiRepository.save(buildUncheckedRealisasiRenaksi(
                                        req.renaksiId(),
                                        req.renaksi(),
                                        req.nip(),
                                        req.rekinId(),
                                        req.rekin(),
                                        req.targetId(),
                                        req.target(),
                                        req.realisasi(),
                                        req.satuan(),
                                        req.bulan(),
                                        req.tahun(),
                                        req.jenisRealisasi()
                                ))));
                    }

                    return renaksiRepository.findFirstByNipAndBulanAndRekinId(
                                    req.nip(),
                                    req.bulan(),
                                    req.rekinId())
                            .flatMap(existing -> renaksiRepository.save(buildUpdatedRealisasiRenaksi(existing, req)))
                            .switchIfEmpty(Mono.defer(() -> renaksiRepository.save(buildUncheckedRealisasiRenaksi(
                                    req.renaksiId(),
                                    req.renaksi(),
                                    req.nip(),
                                    req.rekinId(),
                                    req.rekin(),
                                    req.targetId(),
                                    req.target(),
                                    req.realisasi(),
                                    req.satuan(),
                                    req.bulan(),
                                    req.tahun(),
                                    req.jenisRealisasi()
                            ))));
                });
    }

    private static Renaksi buildUpdatedRealisasiRenaksi(Renaksi existing, RenaksiRequest req) {
        return new Renaksi(
                existing.id(),
                existing.renaksiId(),
                existing.renaksi(),
                existing.nip(),
                existing.rekinId(),
                existing.rekin(),
                existing.targetId(),
                existing.target(),
                req.realisasi(),
                req.satuan(),
                req.bulan(),
                req.tahun(),
                req.jenisRealisasi(),
                RenaksiStatus.UNCHECKED,
                existing.createdBy(),
                existing.lastModifiedBy(),
                existing.createdDate(),
                existing.lastModifiedDate(),
                existing.version());
    }
}
