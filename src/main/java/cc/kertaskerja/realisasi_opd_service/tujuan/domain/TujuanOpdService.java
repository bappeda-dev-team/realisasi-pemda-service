package cc.kertaskerja.realisasi_opd_service.tujuan.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TujuanOpdService {
    private final TujuanOpdRepository tujuanOpdRepository;

    public TujuanOpdService(TujuanOpdRepository tujuanOpdRepository) {
        this.tujuanOpdRepository = tujuanOpdRepository;
    }

    public Flux<TujuanOpd> getAllRealisasiTujuanOpd() {
        return tujuanOpdRepository.findAll();
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByTahunAndKodeOpd(String tahun, String kodeOpd) {
        return tujuanOpdRepository.findAllByTahunAndKodeOpd(tahun, kodeOpd);
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByKodeOpd(String kodeOpd) {
        return tujuanOpdRepository.findAllByKodeOpd(kodeOpd);
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByTujuanId(String tujuanId) {
        return tujuanOpdRepository.findAllByTujuanId(tujuanId);
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByIndikatorId(String indikatorId) {
        return tujuanOpdRepository.findAllByIndikatorId(indikatorId);
    }

    public Flux<TujuanOpd> getRealisasiTujuanOpdByPeriodeRpjmd(String tahunAwal, String tahunAkhir, String kodeOpd) {
        return tujuanOpdRepository.findAllByTahunBetweenAndKodeOpd(tahunAwal, tahunAkhir, kodeOpd);
    }

    public Mono<TujuanOpd> getRealisasiTujuanOpdById(Long id) {
        return tujuanOpdRepository.findById(id);
    }

    public Mono<TujuanOpd> submitRealisasiTujuanOpd(String tujuanId, String indikatorId, String targetId, String target, Double realisasi, String satuan, String tahun, JenisRealisasi jenisRealisasi, String kodeOpd) {
        return Mono.just(buildUncheckedRealisasiTujuanOpd(tujuanId, indikatorId, targetId, target, realisasi, satuan, tahun, jenisRealisasi, kodeOpd))
                .flatMap(tujuanOpdRepository::save);
    }

    private TujuanOpd buildUncheckedRealisasiTujuanOpd(String tujuanId, String indikatorId, String targetId, String target, Double realisasi, String satuan, String tahun, JenisRealisasi jenisRealisasi, String kodeOpd) {
        return TujuanOpd.of(
                tujuanId,
                "Realisasi Tujuan Opd " + tujuanId,
                indikatorId,
                "Realisasi Indikator Opd " + indikatorId,
                targetId, target, realisasi, satuan, tahun,
                jenisRealisasi, kodeOpd,
                TujuanOpdStatus.UNCHECKED
        );
    }
}
