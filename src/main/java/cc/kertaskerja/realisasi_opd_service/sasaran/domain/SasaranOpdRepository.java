package cc.kertaskerja.realisasi_opd_service.sasaran.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SasaranOpdRepository extends ReactiveCrudRepository<SasaranOpd, Long> {

    Mono<SasaranOpd> findFirstByKodeOpdAndKodeSasaranOpdAndTahunAndBulan(
            String kodeOpd,
            String kodeSasaranOpd,
            String tahun,
            String bulan
    );

    Flux<SasaranOpd> findAllByTahunAndKodeOpd(String tahun, String kodeOpd);

    Flux<SasaranOpd> findAllByTahunAndKodeOpdAndBulan(String tahun, String kodeOpd, String bulan);
}
