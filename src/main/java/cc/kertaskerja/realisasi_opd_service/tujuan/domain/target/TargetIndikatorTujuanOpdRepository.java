package cc.kertaskerja.realisasi_opd_service.tujuan.domain.target;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TargetIndikatorTujuanOpdRepository extends ReactiveCrudRepository<TargetIndikatorTujuanOpd, Long> {
    Mono<TargetIndikatorTujuanOpd> findFirstByIndikatorTujuanIdAndKodeTargetAndTahunAndBulan(
            Long indikatorTujuanId,
            String kodeTarget,
            String tahun,
            String bulan
    );

    Flux<TargetIndikatorTujuanOpd> findAllByIndikatorTujuanId(Long indikatorTujuanId);
}
