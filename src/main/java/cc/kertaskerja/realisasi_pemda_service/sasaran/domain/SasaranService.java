package cc.kertaskerja.realisasi_pemda_service.sasaran.domain;

import cc.kertaskerja.realisasi_pemda_service.realisasi.domain.JenisRealisasi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SasaranService {
    private final SasaranRepository sasaranRepository;

    public SasaranService(SasaranRepository sasaranRepository) {
        this.sasaranRepository = sasaranRepository;
    }

    public Flux<Sasaran> getAllRealisasiSasaran() {
        return sasaranRepository.findAll();
    }

    public Flux<Sasaran> getAllRealisasiSasaranByTahun(String tahun) {
        return sasaranRepository.findAllByTahun(tahun);
    }

    public Flux<Sasaran> getAllRealisasiSasaranBySasaranId(String sasaranId) {
        return sasaranRepository.findAllBySasaranId(sasaranId);
    }

    public Mono<Sasaran> getSasaranById(Long id) {
        return sasaranRepository.findById(id);
    }

    public Mono<Sasaran> submitRealisasiSasaran(String sasaranId, String indikatorId, Double target, Double realisasi, String satuan, String tahun, JenisRealisasi jenisRealisasi) {
        return Mono.just(buildUnchekcedRealisasiSasaran(sasaranId, indikatorId, target, realisasi, satuan, tahun, jenisRealisasi))
                .flatMap(sasaranRepository::save);
    }

    // sasaranId check to sasaranService
    // and modify sasaran, check target, satuan, and change status to CHECKED
    public static Sasaran buildUnchekcedRealisasiSasaran(String sasaranId, String indikatorId, Double target, Double realisasi, String satuan, String tahun, JenisRealisasi jenisRealisasi) {
        return Sasaran.of(sasaranId,
                "Realisasi Sasaran " + sasaranId,
                indikatorId,
                "Realisasi Indikator " + indikatorId,
                target, realisasi, satuan, tahun,
                jenisRealisasi,
                SasaranStatus.UNCHECKED);
    }
}
