package cc.kertaskerja.realisasi_opd_service.sasaran.domain.indikator;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndikatorSasaranOpdRepository extends ReactiveCrudRepository<IndikatorSasaranOpd, Long> {
    Mono<IndikatorSasaranOpd> findFirstBySasaranOpdIdAndKodeIndikatorAndKodeOpdAndTahunAndBulan(
            Long sasaranOpdId,
            String kodeIndikator,
            String kodeOpd,
            String tahun,
            String bulan
    );

    Flux<IndikatorSasaranOpd> findAllBySasaranOpdId(Long sasaranOpdId);
}
