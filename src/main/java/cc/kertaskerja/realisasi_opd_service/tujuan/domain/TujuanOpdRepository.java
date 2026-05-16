package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TujuanOpdRepository extends ReactiveCrudRepository<TujuanOpd, Long> {

    Flux<TujuanOpd> findAllByTahunAndTujuanIdAndKodeOpd(String tahun, String tujuanId, String kodeOpd);

    Flux<TujuanOpd> findAllByTahunAndKodeOpdAndBulan(String tahun, String kodeOpd, String bulan);

    Flux<TujuanOpd> findAllByKodeOpdAndTahunAndBulanAndTargetIdAndIndikatorIdAndTujuanId(
            String kodeOpd,
            String tahun,
            String bulan,
            String targetId,
            String indikatorId,
            String tujuanId
    );
}
