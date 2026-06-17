package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TujuanOpdRepository extends ReactiveCrudRepository<TujuanOpd, Long> {

    Flux<TujuanOpd> findAllByKodeOpdAndTahunAndBulan(String kodeOpd, String tahun, String bulan);

    Mono<TujuanOpd> findFirstByKodeOpdAndKodeTujuanOpdAndKodeIndikatorAndKodeTargetAndTahunAndBulan(
            String kodeOpd,
            String kodeTujuanOpd,
            String kodeIndikator,
            String kodeTarget,
            String tahun,
            String bulan
    );

    Flux<TujuanOpd> findAllByKodeOpdAndTahun(String kodeOpd, String tahun);
}
