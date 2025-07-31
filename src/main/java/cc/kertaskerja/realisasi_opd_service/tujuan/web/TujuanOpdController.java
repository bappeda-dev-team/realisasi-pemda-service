package cc.kertaskerja.realisasi_opd_service.tujuan.web;

import cc.kertaskerja.realisasi_opd_service.tujuan.domain.TujuanOpd;
import cc.kertaskerja.realisasi_opd_service.tujuan.domain.TujuanOpdService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("tujuan_opd")
public class TujuanOpdController {
    private final TujuanOpdService tujuanOpdService;
    
    public TujuanOpdController(TujuanOpdService tujuanOpdService) {
        this.tujuanOpdService = tujuanOpdService;
    }

    @GetMapping
    public Flux<TujuanOpd> getAllRealisasiTujuanOpd() {
        return tujuanOpdService.getAllRealisasiTujuanOpd();
    }
    
    @GetMapping("/find/{id}")
    public Mono<TujuanOpd> getRealisasiTujuanOpd(@PathVariable("id") Long id) {
        return tujuanOpdService.getRealisasiTujuanOpdById(id);
    }
    
    @GetMapping("/by-tujuan/{tujuanId}")
    public Flux<TujuanOpd> getRealisasiTujuanOpdByTujuanId(@PathVariable String tujuanId) {
        return tujuanOpdService.getRealisasiTujuanOpdByTujuanId(tujuanId);
    }
    
    @GetMapping("/{kodeOpd}")
    public Flux<TujuanOpd> getRealisasiTujuanOpdByKodeOpd(@PathVariable String kodeOpd) {
        return tujuanOpdService.getRealisasiTujuanOpdByKodeOpd(kodeOpd);
    }
    
    @GetMapping("/{kodeOpd}/by-tahun/{tahun}")
    public Flux<TujuanOpd> getRealisasiTujuanOpdByTahunAndKodeOpd(
            @PathVariable String kodeOpd,
            @PathVariable String tahun,
            @RequestParam(required = false) String tujuanId) {
        if (tujuanId != null && !tujuanId.isBlank()) {
            return tujuanOpdService.getRealisasiTujuanOpdByTahunAndTujuanIdAndKodeOpd(tahun, tujuanId, kodeOpd);
        }
        return tujuanOpdService.getRealisasiTujuanOpdByTahunAndKodeOpd(tahun, kodeOpd);
    }
    
    @GetMapping("/{kodeOpd}/by-periode/{tahunAwal}/{tahunAkhir}/rpjmd")
    public Flux<TujuanOpd> getRealisasiTujuanOpdByPeriodeRpjmd(@PathVariable String kodeOpd, @PathVariable String tahunAwal, @PathVariable String tahunAkhir) {
        return tujuanOpdService.getRealisasiTujuanOpdByPeriodeRpjmd(tahunAwal, tahunAkhir, kodeOpd);
    }

    @GetMapping("/by-indikator/{indikatorId}")
    public Flux<TujuanOpd> getRealisasiTujuanOpdByIndikatorId(@PathVariable String indikatorId) {
        return tujuanOpdService.getRealisasiTujuanOpdByIndikatorId(indikatorId);
    }

    @PostMapping
    public Mono<TujuanOpd> submitRealisasiTujuanOpd(@RequestBody @Valid TujuanOpdRequest tujuanOpdRequest) {
        return tujuanOpdService.submitRealisasiTujuanOpd(
                tujuanOpdRequest.tujuanId(),
                tujuanOpdRequest.indikatorId(),
                tujuanOpdRequest.targetId(),
                tujuanOpdRequest.target(),
                tujuanOpdRequest.realisasi(),
                tujuanOpdRequest.satuan(),
                tujuanOpdRequest.tahun(),
                tujuanOpdRequest.jenisRealisasi(),
                tujuanOpdRequest.kodeOpd()
        );
    }

    @PostMapping("/batch")
    public Flux<TujuanOpd> batchSubmitRealisasiTujuanOpd(@RequestBody @Valid List<TujuanOpdRequest> tujuanOpdRequests) {
        return tujuanOpdService.batchSubmitRealisasiTujuanOpd(tujuanOpdRequests);
    }
}
