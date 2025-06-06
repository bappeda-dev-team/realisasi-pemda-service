package cc.kertaskerja.realisasi_pemda_service.tujuan.domain;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TujuanService {
    private final TujuanRepository tujuanRepository;

    public TujuanService(TujuanRepository tujuanRepository) {
        this.tujuanRepository = tujuanRepository;
    }

    public Flux<Tujuan> getAllRealisasiTujuan() {
        return tujuanRepository.findAll();
    }

    public Flux<Tujuan> getRealisasiTujuanByTahun(String tahun) {
        return tujuanRepository.findAllByTahun(tahun);
    }

    public Flux<Tujuan> getRealisasiTujuanByTahunAndTujuanId(String tahun, String tujuanId) {
        return tujuanRepository.findAllByTahunAndTujuanId(tahun, tujuanId);
    }

    public Flux<Tujuan> getRealisasiTujuanByTujuanId(String tujuanId) {
        return tujuanRepository.findAllByTujuanId(tujuanId);
    }

    public Mono<Tujuan> getRealisasiTujuanById(Long id) {
        return tujuanRepository.findById(id);
    }

    // tujuanId check to tujuanService
    // check target, satuan, tahun, and get tujuan text
    public Mono<Tujuan> submitRealisasiTujuan(String tujuanId, String indikatorId, Double target, Double realisasi, String satuan, String tahun, JenisRealisasi jenisRealisasi) {
        return Mono.just(buildUncheckedRealisasiTujuan(tujuanId, indikatorId, target, realisasi, satuan, tahun, jenisRealisasi))
                .flatMap(tujuanRepository::save);
    }

    public static Tujuan buildUncheckedRealisasiTujuan(String tujuanId, String indikatorId, Double target, Double realisasi, String satuan, String tahun, JenisRealisasi jenisRealisasi) {
        return Tujuan.of(tujuanId,
                "Realisasi Tujuan " + tujuanId,
                indikatorId,
                "Realisasi Indikator " + indikatorId,
                target, realisasi, satuan, tahun, jenisRealisasi,
                TujuanStatus.UNCHECKED);
    }
}
