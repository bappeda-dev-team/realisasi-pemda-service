package cc.kertaskerja.realisasi_opd_service.sasaran.web;

import cc.kertaskerja.realisasi_opd_service.sasaran.domain.SasaranOpd;
import cc.kertaskerja.realisasi_opd_service.sasaran.domain.SasaranOpdService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("sasaran_opd")
public class SasaranOpdController {
    private final SasaranOpdService sasaranOpdService;

    public SasaranOpdController(SasaranOpdService sasaranOpdService) {
        this.sasaranOpdService = sasaranOpdService;
    }

    @GetMapping
    public Flux<SasaranOpd> getAllRealisasiSasaranOpd() {
        return sasaranOpdService.getAllRealisasiSasaranOpd();
    }
    
    @GetMapping("/find/{id}")
    public Mono<SasaranOpd> getRealisasiSasaranOpd(@PathVariable("id") Long id) {
        return sasaranOpdService.getRealisasiSasaranOpdById(id);
    }
    
    @GetMapping("/by-sasaran/{sasaranId}")
    public Flux<SasaranOpd> getRealisasiSasaranOpdBySasaranOpdId(@PathVariable String sasaranId) {
        return sasaranOpdService.getRealisasiSasaranOpdBySasaranId(sasaranId);
    }
    
    @GetMapping("/{kodeOpd}")
    public Flux<SasaranOpd> getRealisasiSasaranOpdByKodeOpd(@PathVariable String kodeOpd) {
        return sasaranOpdService.getRealisasiSasaranOpdByKodeOpd(kodeOpd);
    }
    
    @GetMapping("/{kodeOpd}/by-tahun/{tahun}")
    public Flux<SasaranOpd> getRealisasiSasaranOpdByTahunAndKodeOpd(
            @PathVariable String kodeOpd,
            @PathVariable String tahun,
            @RequestParam(required = false) String sasaranId) {
        if (sasaranId != null && !sasaranId.isBlank()) {
            return sasaranOpdService.getRealisasiSasaranOpdByTahunAndSasaranIdAndKodeOpd(tahun, sasaranId, kodeOpd);
        }
        return sasaranOpdService.getRealisasiSasaranOpdByTahunAndKodeOpd(tahun, kodeOpd);
    }
    
    @GetMapping("/{kodeOpd}/by-periode/{tahunAwal}/{tahunAkhir}/rpjmd")
    public Flux<SasaranOpd> getRealisasiSasaranOpdByPeriodeRpjmd(@PathVariable String kodeOpd, @PathVariable String tahunAwal, @PathVariable String tahunAkhir) {
        return sasaranOpdService.getRealisasiSasaranOpdByPeriodeRpjmd(tahunAwal, tahunAkhir, kodeOpd);
    }

    @GetMapping("/by-indikator/{indikatorId}")
    public Flux<SasaranOpd> getRealisasiSasaranOpdByIndikatorId(@PathVariable String indikatorId) {
        return sasaranOpdService.getRealisasiSasaranOpdByIndikatorId(indikatorId);
    }

    @PostMapping
    public Mono<SasaranOpd> submitRealisasiSasaranOpd(@RequestBody @Valid SasaranOpdRequest sasaranOpdRequest) {
        return sasaranOpdService.submitRealisasiSasaranOpd(
                sasaranOpdRequest.sasaranId(),
                sasaranOpdRequest.indikatorId(),
                sasaranOpdRequest.targetId(),
                sasaranOpdRequest.target(),
                sasaranOpdRequest.realisasi(),
                sasaranOpdRequest.satuan(),
                sasaranOpdRequest.tahun(),
                sasaranOpdRequest.jenisRealisasi(),
                sasaranOpdRequest.kodeOpd()
        );
    }

    @PostMapping("/batch")
    public Flux<SasaranOpd> batchSubmitRealisasiSasaranOpd(@RequestBody @Valid List<SasaranOpdRequest> sasaranOpdRequests) {
        return sasaranOpdService.batchSubmitRealisasiSasaranOpd(sasaranOpdRequests);
    }
}
