package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.web.RenjaPaguIndividuRequest;
import cc.kertaskerja.renja.domain.JenisRenja;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RenjaPaguIndividuService {
    private final RenjaPaguIndividuRepository renjaPaguIndividuRepository;

    public RenjaPaguIndividuService(RenjaPaguIndividuRepository renjaPaguIndividuRepository) {
        this.renjaPaguIndividuRepository = renjaPaguIndividuRepository;
    }

    public Flux<RenjaPaguIndividu> getAllRealisasiRenjaPaguIndividu() {
        return renjaPaguIndividuRepository.findAll();
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByTahunAndNip(String tahun, String nip) {
        return renjaPaguIndividuRepository.findAllByTahunAndNip(tahun, nip);
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByNip(String nip) {
        return renjaPaguIndividuRepository.findAllByNip(nip);
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByRenjaId(String renjaId) {
        return renjaPaguIndividuRepository.findAllByRenjaId(renjaId);
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByPeriodeRpjmd(String tahunAwal, String tahunAkhir, String nip) {
        return renjaPaguIndividuRepository.findAllByTahunBetweenAndNip(tahunAwal, tahunAkhir, nip);
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByTahunAndRenjaIdAndNip(String tahun, String renjaId, String nip) {
        return renjaPaguIndividuRepository.findAllByTahunAndRenjaIdAndNip(tahun, renjaId, nip);
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByTahunAndJenisRenjaAndKodeRenjaAndNip(String tahun, JenisRenja jenisRenja, String kodeRenja, String nip) {
        return renjaPaguIndividuRepository.findAllByTahunAndJenisRenjaAndKodeRenjaAndNip(tahun, jenisRenja, kodeRenja, nip);
    }

    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByJenisRenjaAndKodeRenjaAndNip(JenisRenja jenisRenja, String kodeRenja, String nip) {
        return renjaPaguIndividuRepository.findAllByJenisRenjaAndKodeRenjaAndNip(jenisRenja, kodeRenja, nip);
    }

    public Mono<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuById(Long id) {
        return renjaPaguIndividuRepository.findById(id);
    }

    public Mono<RenjaPaguIndividu> submitRealisasiRenjaPaguIndividu(
            String renjaId,
            String renja,
            String kodeRenja,
            JenisRenja jenisRenja,
            String nip,
            String idIndikator,
            String indikator,
            Integer pagu,
            Integer realisasi,
            String satuan,
            String tahun,
            JenisRealisasi jenisRealisasi) {
        return Mono.just(buildUncheckedRealisasiRenjaPaguIndividu(
                        renjaId,
                        renja,
                        kodeRenja,
                        jenisRenja,
                        nip,
                        idIndikator,
                        indikator,
                        pagu,
                        realisasi,
                        satuan,
                        tahun,
                        jenisRealisasi))
                .flatMap(renjaPaguIndividuRepository::save);
    }

    public static RenjaPaguIndividu buildUncheckedRealisasiRenjaPaguIndividu(
            String renjaId,
            String renja,
            String kodeRenja,
            JenisRenja jenisRenja,
            String nip,
            String idIndikator,
            String indikator,
            Integer pagu,
            Integer realisasi,
            String satuan,
            String tahun,
            JenisRealisasi jenisRealisasi) {
        return RenjaPaguIndividu.of(
                renjaId,
                renja,
                kodeRenja,
                jenisRenja,
                nip,
                idIndikator,
                indikator,
                pagu,
                realisasi,
                satuan,
                tahun,
                jenisRealisasi,
                RenjaPaguIndividuStatus.UNCHECKED
        );
    }

    public Flux<RenjaPaguIndividu> batchSubmitRealisasiRenjaPaguIndividu(@Valid List<RenjaPaguIndividuRequest> renjaPaguIndividuRequests) {
        return Flux.fromIterable(renjaPaguIndividuRequests)
                .flatMap(req -> {
                    if (req.targetRealisasiId() != null) {
                        return renjaPaguIndividuRepository.findById(req.targetRealisasiId())
                                .flatMap(existing -> renjaPaguIndividuRepository.save(buildUpdatedRealisasiRenjaPaguIndividu(existing, req)))
                                .switchIfEmpty(Mono.defer(() -> renjaPaguIndividuRepository.save(buildUncheckedRealisasiRenjaPaguIndividu(
                                        req.renjaId(),
                                        req.renja(),
                                        req.kodeRenja(),
                                        req.jenisRenja(),
                                        req.nip(),
                                        req.idIndikator(),
                                        req.indikator(),
                                        req.pagu(),
                                        req.realisasi(),
                                        req.satuan(),
                                        req.tahun(),
                                        req.jenisRealisasi()
                                ))));
                    }

                    return renjaPaguIndividuRepository
                            .findFirstByNipAndTahunAndJenisRenjaAndKodeRenja(
                                    req.nip(),
                                    req.tahun(),
                                    req.jenisRenja(),
                                    req.kodeRenja())
                            .flatMap(existing -> renjaPaguIndividuRepository.save(buildUpdatedRealisasiRenjaPaguIndividu(existing, req)))
                            .switchIfEmpty(Mono.defer(() -> renjaPaguIndividuRepository.save(buildUncheckedRealisasiRenjaPaguIndividu(
                                    req.renjaId(),
                                    req.renja(),
                                    req.kodeRenja(),
                                    req.jenisRenja(),
                                    req.nip(),
                                    req.idIndikator(),
                                    req.indikator(),
                                    req.pagu(),
                                    req.realisasi(),
                                    req.satuan(),
                                    req.tahun(),
                                    req.jenisRealisasi()
                            ))));
                });
    }

    private static RenjaPaguIndividu buildUpdatedRealisasiRenjaPaguIndividu(RenjaPaguIndividu existing, RenjaPaguIndividuRequest req) {
        return new RenjaPaguIndividu(
                existing.id(),
                existing.renjaId(),
                existing.renja(),
                existing.kodeRenja(),
                existing.jenisRenja(),
                existing.nip(),
                existing.idIndikator(),
                existing.indikator(),
                req.pagu(),
                req.realisasi(),
                req.satuan(),
                req.tahun(),
                req.jenisRealisasi(),
                RenjaPaguIndividuStatus.UNCHECKED,
                existing.createdBy(),
                existing.lastModifiedBy(),
                existing.createdDate(),
                existing.lastModifiedDate(),
                existing.version()
        );
    }

}
