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

    @GetMapping
    public Flux<Tujuan> getAllRealisasiTujuan() {
        return tujuanService.getAllRealisasiTujuan();
    }

    @GetMapping("{tahun}")
    public Flux<Tujuan> getRealisasiTujuanByTahun(@PathVariable String tahun) {
        return tujuanService.getRealisasiTujuanByTahun(tahun);
    }

    @GetMapping("{tujuanId}")
    public Flux<Tujuan> getRealisasiTujuanByTujuanId(@PathVariable String tujuanId) {
        return tujuanService.getRealisasiTujuanByTujuanId(tujuanId);
    }

    // for editing purposes
    @GetMapping("{id}")
    public Mono<Tujuan> getRealisasiTujuanById(@PathVariable Long id) {
        return tujuanService.getRealisasiTujuanById(id);
    }

    @PostMapping
    public Mono<Tujuan> submitRealisasiTujuan(@RequestBody @Valid TujuanRequest tujuanRequest) {
        return tujuanService.submitRealisasiTujuan(
                tujuanRequest.tujuanId(),
                tujuanRequest.target(),
                tujuanRequest.realisasi(),
                tujuanRequest.satuan(),
                tujuanRequest.tahun(),
                tujuanRequest.jenisRealisasi()
        );
    }
}
