package cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LaporanRBTematikRepository extends ReactiveCrudRepository<LaporanRBTematik, Long> {
    Flux<LaporanRBTematik> findAllByKodeOpdAndNipAndTahun(String kodeOpd, String nip, String tahun);

    Flux<LaporanRBTematik> findAllByKodeOpdAndNipAndTahunAndBulan(String kodeOpd, String nip, String tahun, String bulan);

    Mono<LaporanRBTematik> findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbTematikAndIdIndikatorRbTematikAndIdTargetRbTematik(
            String kodeOpd, String nip, String tahun, String bulan,
            String idRbTematik, String idIndikatorRbTematik, String idTargetRbTematik);
}