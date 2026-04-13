package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import cc.kertaskerja.renja.domain.JenisRenja;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RenjaPaguIndividuRepository extends ReactiveCrudRepository<RenjaPaguIndividu, Long> {
    Flux<RenjaPaguIndividu> findAllByNipAndTahun(String nip, String tahun);

    Flux<RenjaPaguIndividu> findAllByTahunAndNipAndJenisRenjaAndKodeRenjaAndRenjaId(
            String tahun, String nip, JenisRenja jenisRenja, String kodeRenja, String renjaId);

    Mono<RenjaPaguIndividu> findFirstByNipAndTahunAndJenisRenjaAndKodeRenja(String nip, String tahun, JenisRenja jenisRenja, String kodeRenja);

    Mono<Void> deleteByRenjaId(String renjaId);
}
