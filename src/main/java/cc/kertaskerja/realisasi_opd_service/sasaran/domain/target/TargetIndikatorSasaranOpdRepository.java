package cc.kertaskerja.realisasi_opd_service.sasaran.domain.target;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TargetIndikatorSasaranOpdRepository extends ReactiveCrudRepository<TargetIndikatorSasaranOpd, Long> {
    Mono<TargetIndikatorSasaranOpd> findFirstByIndikatorSasaranIdAndKodeTargetAndTahunAndBulan(
            Long indikatorSasaranId,
            String kodeTarget,
            String tahun,
            String bulan
    );

    Flux<TargetIndikatorSasaranOpd> findAllByIndikatorSasaranId(Long indikatorSasaranId);
}
