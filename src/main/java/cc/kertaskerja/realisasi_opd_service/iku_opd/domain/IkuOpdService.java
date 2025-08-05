package cc.kertaskerja.realisasi_opd_service.iku_opd.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.realisasi_opd_service.sasaran.domain.SasaranOpdRepository;
import cc.kertaskerja.realisasi_opd_service.tujuan.domain.TujuanOpdRepository;
import reactor.core.publisher.Flux;

@Service
public class IkuOpdService {
    private final TujuanOpdRepository tujuanOpdRepository;
    private final SasaranOpdRepository sasaranOpdRepository;

    public IkuOpdService(TujuanOpdRepository tujuanOpdRepository, SasaranOpdRepository sasaranOpdRepository) {
        this.tujuanOpdRepository= tujuanOpdRepository;
        this.sasaranOpdRepository = sasaranOpdRepository;
    }

    public Flux<IkuOpd> getAllIkuOpd(String kodeOpd) {
        Flux<IkuOpd> ikuTujuanOpd = tujuanOpdRepository.findAllByKodeOpd(kodeOpd)
            .map(t -> buildIkuTujuanOpd(
                                        t.indikatorId(),
                                        t.indikator(),
                                        t.targetId(),
                                        t.target(),
                                        t.realisasi(),
                                        t.satuan(),
                                        t.capaian(),
                                        t.tahun(),
                                        t.jenisRealisasi(),
                                        t.kodeOpd()
                                        ));

        Flux<IkuOpd> ikuSasaranOpd = sasaranOpdRepository.findAllByKodeOpd(kodeOpd)
            .map(s -> buildIkuSasaranOpd(
                                        s.indikatorId(),
                                        s.indikator(),
                                        s.targetId(),
                                        s.target(),
                                        s.realisasi(),
                                        s.satuan(),
                                        s.capaian(),
                                        s.tahun(),
                                        s.jenisRealisasi(),
                                        s.kodeOpd()
                                         ));

        return Flux.merge(ikuTujuanOpd, ikuSasaranOpd);
    }

    public Flux<IkuOpd> getAllIkuOpdByTahunAndKodeOpd(String tahun, String kodeOpd) {
        Flux<IkuOpd> ikuTujuanOpd = tujuanOpdRepository.findAllByTahunAndKodeOpd(tahun, kodeOpd)
            .map(t -> buildIkuTujuanOpd(
                                        t.indikatorId(),
                                        t.indikator(),
                                        t.targetId(),
                                        t.target(),
                                        t.realisasi(),
                                        t.satuan(),
                                        t.capaian(),
                                        t.tahun(),
                                        t.jenisRealisasi(),
                                        t.kodeOpd()
                                        ));

        Flux<IkuOpd> ikuSasaranOpd = sasaranOpdRepository.findAllByTahunAndKodeOpd(tahun, kodeOpd)
            .map(s -> buildIkuSasaranOpd(
                                        s.indikatorId(),
                                        s.indikator(),
                                        s.targetId(),
                                        s.target(),
                                        s.realisasi(),
                                        s.satuan(),
                                        s.capaian(),
                                        s.tahun(),
                                        s.jenisRealisasi(),
                                        s.kodeOpd()
                                         ));

        return Flux.merge(ikuTujuanOpd, ikuSasaranOpd);
    }

    public static IkuOpd buildIkuTujuanOpd(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, JenisRealisasi jenisRealisasi, String kodeOpd) {
        return IkuOpd.of(indikatorId, indikator, targetId, target, realisasi, satuan, capaian, tahun, jenisRealisasi, JenisIkuOpd.TUJUANOPD, kodeOpd);
    }

    public static IkuOpd buildIkuSasaranOpd(String indikatorId, String indikator, String targetId, String target, Double realisasi, String satuan, String capaian, String tahun, JenisRealisasi jenisRealisasi, String kodeOpd) {
        return IkuOpd.of(indikatorId, indikator, targetId, target, realisasi, satuan, capaian, tahun, jenisRealisasi, JenisIkuOpd.SASARANOPD, kodeOpd);
    }
}
