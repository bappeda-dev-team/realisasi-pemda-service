package cc.kertaskerja.realisasi_pemda_service.iku.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.SasaranRepository;
import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.SasaranService;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.TujuanRepository;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.TujuanService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IkuService {
    private final TujuanRepository tujuanRepository;
    private final SasaranRepository sasaranRepository;
    private final TujuanService tujuanService;
    private final SasaranService sasaranService;

    public IkuService(TujuanRepository tujuanRepository, SasaranRepository sasaranRepository, TujuanService tujuanService, SasaranService sasaranService) {
        this.tujuanRepository = tujuanRepository;
        this.sasaranRepository = sasaranRepository;
        this.tujuanService = tujuanService;
        this.sasaranService = sasaranService;
    }

    public Flux<Iku> getAllIku() {
        Flux<Iku> ikuTujuan = tujuanRepository.findAll()
                .map(t -> buildIkuTujuan(
                        t.indikatorId(),
                        t.indikator(),
                        t.targetId(),
                        t.target(),
                        t.realisasi(),
                        t.satuan(),
                        t.capaian(),
                        t.tahun(),
                        t.faktorPenunjang(),
                        t.faktorPenghambat(),
                        t.jenisRealisasi()
                ));

        Flux<Iku> ikuSasaran = sasaranRepository.findAll()
                .map(s -> buildIkuSasaran(
                        s.indikatorId(),
                        s.indikator(),
                        s.targetId(),
                        s.target(),
                        s.realisasi(),
                        s.satuan(),
                        s.capaian(),
                        s.tahun(),
                        s.faktorPenunjang(),
                        s.faktorPenghambat(),
                        s.jenisRealisasi()
                ));

        return Flux.merge(ikuTujuan, ikuSasaran);
    }

    public Flux<Iku> getAllIkuByTahun(String tahun) {
        Flux<Iku> ikuTujuan = tujuanRepository.findAllByTahun(tahun)
                .map(t -> buildIkuTujuan(
                        t.indikatorId(),
                        t.indikator(),
                        t.targetId(),
                        t.target(),
                        t.realisasi(),
                        t.satuan(),
                        t.capaian(),
                        t.tahun(),
                        t.faktorPenunjang(),
                        t.faktorPenghambat(),
                        t.jenisRealisasi()
                ));

        Flux<Iku> ikuSasaran = sasaranRepository.findAllByTahun(tahun)
                .map(s -> buildIkuSasaran(
                        s.indikatorId(),
                        s.indikator(),
                        s.targetId(),
                        s.target(),
                        s.realisasi(),
                        s.satuan(),
                        s.capaian(),
                        s.tahun(),
                        s.faktorPenunjang(),
                        s.faktorPenghambat(),
                        s.jenisRealisasi()
                ));

        return Flux.merge(ikuTujuan, ikuSasaran);
    }


    public static Iku buildIkuTujuan(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, String faktorPenunjang, String faktorPenghambat, JenisRealisasi jenisRealisasi) {
        return Iku.of(indikatorId, indikator, targetId, target, realisasi, satuan, capaian, tahun, faktorPenunjang, faktorPenghambat, jenisRealisasi, JenisIku.TUJUAN);
    }

    public static Iku buildIkuSasaran(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, String faktorPenunjang, String faktorPenghambat, JenisRealisasi jenisRealisasi) {
        return Iku.of(indikatorId, indikator, targetId, target, realisasi, satuan, capaian, tahun, faktorPenunjang, faktorPenghambat, jenisRealisasi, JenisIku.SASARAN);
    }

    public Mono<Iku> updateFaktorPenunjang(JenisIku jenisIku, String entityId, String indikatorId, String targetId, String tahun, String bulan, String faktorPenunjang) {
        if (jenisIku == JenisIku.TUJUAN) {
            return tujuanService.updateFaktorPenunjang(entityId, indikatorId, targetId, tahun, bulan, faktorPenunjang)
                    .map(t -> buildIkuTujuan(
                            t.indikatorId(), t.indikator(), t.targetId(), t.target(),
                            t.realisasi(), t.satuan(), t.capaian(), t.tahun(),
                            t.faktorPenunjang(), t.faktorPenghambat(), t.jenisRealisasi()
                    ));
        }
        return sasaranService.updateFaktorPenunjang(entityId, indikatorId, targetId, tahun, bulan, faktorPenunjang)
                .map(s -> buildIkuSasaran(
                        s.indikatorId(), s.indikator(), s.targetId(), s.target(),
                        s.realisasi(), s.satuan(), s.capaian(), s.tahun(),
                        s.faktorPenunjang(), s.faktorPenghambat(), s.jenisRealisasi()
                ));
    }

    public Mono<Iku> updateFaktorPenghambat(JenisIku jenisIku, String entityId, String indikatorId, String targetId, String tahun, String bulan, String faktorPenghambat) {
        if (jenisIku == JenisIku.TUJUAN) {
            return tujuanService.updateFaktorPenghambat(entityId, indikatorId, targetId, tahun, bulan, faktorPenghambat)
                    .map(t -> buildIkuTujuan(
                            t.indikatorId(), t.indikator(), t.targetId(), t.target(),
                            t.realisasi(), t.satuan(), t.capaian(), t.tahun(),
                            t.faktorPenunjang(), t.faktorPenghambat(), t.jenisRealisasi()
                    ));
        }
        return sasaranService.updateFaktorPenghambat(entityId, indikatorId, targetId, tahun, bulan, faktorPenghambat)
                .map(s -> buildIkuSasaran(
                        s.indikatorId(), s.indikator(), s.targetId(), s.target(),
                        s.realisasi(), s.satuan(), s.capaian(), s.tahun(),
                        s.faktorPenunjang(), s.faktorPenghambat(), s.jenisRealisasi()
                ));
    }
}
