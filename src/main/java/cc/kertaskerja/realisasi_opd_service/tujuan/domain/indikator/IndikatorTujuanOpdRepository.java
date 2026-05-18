package cc.kertaskerja.realisasi_opd_service.tujuan.domain.indikator;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndikatorTujuanOpdRepository extends ReactiveCrudRepository<IndikatorTujuanOpd, Long> {
    Mono<IndikatorTujuanOpd> findFirstByTujuanOpdIdAndKodeIndikatorAndKodeOpdAndTahunAndBulan(
            Long tujuanOpdId,
            String kodeIndikator,
            String kodeOpd,
            String tahun,
            String bulan
    );

    Flux<IndikatorTujuanOpd> findAllByTujuanOpdId(Long tujuanOpdId);
}
