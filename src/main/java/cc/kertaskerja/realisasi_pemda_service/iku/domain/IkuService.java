package cc.kertaskerja.realisasi_pemda_service.iku.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.SasaranRepository;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.TujuanRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class IkuService {
    private final TujuanRepository tujuanRepository;
    private final SasaranRepository sasaranRepository;

    public IkuService(TujuanRepository tujuanRepository, SasaranRepository sasaranRepository) {
        this.tujuanRepository = tujuanRepository;
        this.sasaranRepository = sasaranRepository;
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
                        s.jenisRealisasi()
                ));

        return Flux.merge(ikuTujuan, ikuSasaran);
    }


    public static Iku buildIkuTujuan(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, JenisRealisasi jenisRealisasi) {
        return Iku.of(indikatorId, indikator, targetId, target, realisasi, satuan, capaian, tahun, jenisRealisasi, JenisIku.TUJUAN);
    }

    public static Iku buildIkuSasaran(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, JenisRealisasi jenisRealisasi) {
        return Iku.of(indikatorId, indikator, targetId, target, realisasi, satuan, capaian, tahun, jenisRealisasi, JenisIku.SASARAN);
    }
}
