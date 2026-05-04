package cc.kertaskerja.realisasi_opd_service.renja_pagu.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RenjaPaguRepository extends ReactiveCrudRepository<RenjaPagu, Long> {
    Flux<RenjaPagu> findAllByJenisRenjaId(String jenisRenjaId);

    Flux<RenjaPagu> findAllByTahunAndKodeOpd(String tahun, String kodeOpd);

    Flux<RenjaPagu> findAllByTahunAndBulanAndKodeOpd(String tahun, String bulan, String kodeOpd);

    Flux<RenjaPagu> findAllByTahunBetweenAndKodeOpd(String tahunAwal, String tahunAkhir, String kodeOpd);

    Flux<RenjaPagu> findAllByKodeOpd(String kodeOpd);

    Flux<RenjaPagu> findAllByTahunAndJenisRenjaIdAndKodeOpd(String tahun, String jenisRenjaId, String kodeOpd);

    Flux<RenjaPagu> findAllByKodeOpdAndTahunAndBulanAndJenisRenjaPaguAndKodeRenjaAndJenisRenjaId(
            String kodeOpd, String tahun, String bulan, String jenisRenja, String kodeRenja, String jenisRenjaId);

    Mono<Void> deleteByJenisRenjaId(String jenisRenjaId);
}
