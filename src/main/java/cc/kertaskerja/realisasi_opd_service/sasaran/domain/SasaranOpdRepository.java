package cc.kertaskerja.realisasi_opd_service.sasaran.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SasaranOpdRepository extends ReactiveCrudRepository<SasaranOpd, Long> {
    Flux<SasaranOpd> findAllByRenjaId(String renjaId);

    Flux<SasaranOpd> findAllByTahunAndKodeOpd(String tahun, String kodeOpd);

    Flux<SasaranOpd> findAllByIndikatorId(String indikatorId);

    Flux<SasaranOpd> findAllByTahunBetweenAndKodeOpd(String tahunAwal, String tahunAkhir, String kodeOpd);

    Flux<SasaranOpd> findAllByKodeOpd(String kodeOpd);

    Flux<SasaranOpd> findAllByTahunAndRenjaIdAndKodeOpd(String tahun, String renjaId, String kodeOpd);

    Flux<SasaranOpd> findAllByTahunAndBulanAndKodeOpd(String tahun, String bulan, String kodeOpd);
}
