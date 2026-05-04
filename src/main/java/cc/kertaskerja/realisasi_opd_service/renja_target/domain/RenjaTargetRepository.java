package cc.kertaskerja.realisasi_opd_service.renja_target.domain;

import cc.kertaskerja.renja.domain.JenisRenja;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RenjaTargetRepository extends ReactiveCrudRepository<RenjaTarget, Long> {
    Flux<RenjaTarget> findAllByJenisRenjaId(String jenisRenjaId);

    Flux<RenjaTarget> findAllByTahunAndKodeOpd(String tahun, String kodeOpd);

    Flux<RenjaTarget> findAllByIndikatorId(String indikatorId);

    Flux<RenjaTarget> findAllByTahunBetweenAndKodeOpd(String tahunAwal, String tahunAkhir, String kodeOpd);

    Flux<RenjaTarget> findAllByKodeOpd(String kodeOpd);

    Flux<RenjaTarget> findAllByTahunAndJenisRenjaIdAndKodeOpd(String tahun, String jenisRenjaId, String kodeOpd);

    Flux<RenjaTarget> findAllByTahunAndBulanAndKodeOpd(String tahun, String bulan, String kodeOpd);

    Mono<RenjaTarget> findFirstByKodeOpdAndTahunAndBulanAndJenisRenjaTargetAndKodeRenjaAndJenisRenjaId(
            String kodeOpd, String tahun, String bulan, JenisRenja jenisRenja, String kodeRenja, String jenisRenjaId);

    Mono<Void> deleteByJenisRenjaId(String jenisRenjaId);
}
