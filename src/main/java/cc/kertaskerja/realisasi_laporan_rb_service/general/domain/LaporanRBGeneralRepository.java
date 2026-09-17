package cc.kertaskerja.realisasi_laporan_rb_service.general.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LaporanRBGeneralRepository extends ReactiveCrudRepository<LaporanRBGeneral, Long> {
    Flux<LaporanRBGeneral> findAllByKodeOpdAndNipAndTahun(String kodeOpd, String nip, String tahun);

    Flux<LaporanRBGeneral> findAllByKodeOpdAndNipAndTahunAndBulan(String kodeOpd, String nip, String tahun, String bulan);

    Mono<LaporanRBGeneral> findFirstByKodeOpdAndNipAndTahunAndBulanAndIdRbGeneralAndIdIndikatorRbGeneralAndIdTargetRbGeneral(
            String kodeOpd, String nip, String tahun, String bulan,
            String idRbGeneral, String idIndikatorRbGeneral, String idTargetRbGeneral);
}