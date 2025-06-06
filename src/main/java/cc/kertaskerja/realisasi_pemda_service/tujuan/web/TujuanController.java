package cc.kertaskerja.realisasi_pemda_service.tujuan.web;

import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.Tujuan;
import cc.kertaskerja.realisasi_pemda_service.tujuan.domain.TujuanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("tujuans")
public class TujuanController {
    private final TujuanService tujuanService;

    public TujuanController(TujuanService tujuanService) {
        this.tujuanService = tujuanService;
    }

    // for editing purposes
    @GetMapping("{id}")
    public Mono<Tujuan> getRealisasiTujuanById(@PathVariable Long id) {
        return tujuanService.getRealisasiTujuanById(id);
    }

    @GetMapping
    public Flux<Tujuan> getAllRealisasiTujuan() {
        return tujuanService.getAllRealisasiTujuan();
    }

    @GetMapping("/by-tujuan/{tujuanId}")
    public Flux<Tujuan> getRealisasiTujuanByTujuanId(@PathVariable String tujuanId) {
        return tujuanService.getRealisasiTujuanByTujuanId(tujuanId);
    }

    @GetMapping("/by-tahun/{tahun}")
    public Flux<Tujuan> getRealisasiTujuanByTahunAndOptionalTujuanId(
            @PathVariable String tahun,
            @RequestParam(required = false) String tujuanId) {
        if (tujuanId != null && !tujuanId.isBlank()) {
            return tujuanService.getRealisasiTujuanByTahunAndTujuanId(tahun, tujuanId);
        }
        return tujuanService.getRealisasiTujuanByTahun(tahun);
    }

    @PostMapping
    public Mono<Tujuan> submitRealisasiTujuan(@RequestBody @Valid TujuanRequest tujuanRequest) {
        return tujuanService.submitRealisasiTujuan(
                tujuanRequest.tujuanId(),
                tujuanRequest.indikatorId(),
                tujuanRequest.target(),
                tujuanRequest.realisasi(),
                tujuanRequest.satuan(),
                tujuanRequest.tahun(),
                tujuanRequest.jenisRealisasi()
        );
    }
}
