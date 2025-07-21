package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TujuanOpdRepository extends ReactiveCrudRepository<TujuanOpd, Long> {
    Flux<TujuanOpd> findAllByTujuanId(String tujuanId);

    Flux<TujuanOpd> findAllByTahunAndKodeOpd(String tahun, String kodeOpd);

    Flux<TujuanOpd> findAllByTahunAndTujuanId(String tahun, String tujuanId);

    Flux<TujuanOpd> findAllByIndikatorId(String indikatorId);

    Flux<TujuanOpd> findAllByTahunBetweenAndKodeOpd(String tahunAwal, String tahunAkhir, String kodeOpd);

    Flux<TujuanOpd> findAllByKodeOpd(String kodeOpd);
}
