package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import cc.kertaskerja.renja.domain.JenisRenja;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RenjaPaguIndividuRepository extends ReactiveCrudRepository<RenjaPaguIndividu, Long> {
    Flux<RenjaPaguIndividu> findAllByRenjaId(String renjaId);

    Flux<RenjaPaguIndividu> findAllByTahunAndNip(String tahun, String nip);

    Flux<RenjaPaguIndividu> findAllByTahunBetweenAndNip(String tahunAwal, String tahunAkhir, String nip);

    Flux<RenjaPaguIndividu> findAllByNip(String nip);

    Flux<RenjaPaguIndividu> findAllByTahunAndRenjaIdAndNip(String tahun, String renjaId, String nip);

    Flux<RenjaPaguIndividu> findAllByTahunAndJenisRenjaAndKodeRenjaAndNip(String tahun, JenisRenja jenisRenja, String kodeRenja, String nip);

    Flux<RenjaPaguIndividu> findAllByJenisRenjaAndKodeRenjaAndNip(JenisRenja jenisRenja, String kodeRenja, String nip);

    Mono<RenjaPaguIndividu> findFirstByNipAndTahunAndJenisRenjaAndKodeRenja(String nip, String tahun, JenisRenja jenisRenja, String kodeRenja);
}
