package cc.kertaskerja.realisasi_pemda_service.sasaran.web;

import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.Sasaran;
import cc.kertaskerja.realisasi_pemda_service.sasaran.domain.SasaranService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("sasarans")
public class SasaranController {
    private final SasaranService sasaranService;

    public SasaranController(SasaranService sasaranService) {
        this.sasaranService = sasaranService;
    }

    @GetMapping
    public Flux<Sasaran> getAllRealisasiSasaran() {
        return sasaranService.getAllRealisasiSasaran();
    }

    @GetMapping("{tahun}")
    public Flux<Sasaran> getAllRealisasiSasaranByTahun(@PathVariable String tahun) {
        return sasaranService.getAllRealisasiSasaranByTahun(tahun);
    }

    @GetMapping("{sasaranId}")
    public Flux<Sasaran> getAllRealisasiSasaranBySasaranId(@PathVariable String sasaranId) {
        return sasaranService.getAllRealisasiSasaranBySasaranId(sasaranId);
    }

    @GetMapping("{id}")
    public Mono<Sasaran> getSasaranById(@PathVariable Long id) {
        return sasaranService.getSasaranById(id);
    }

    @PostMapping
    public Mono<Sasaran> submitRealisasiSasaran(@RequestBody @Valid SasaranRequest sasaranRequest) {
        return sasaranService.submitRealisasiSasaran(
                sasaranRequest.sasaranId(),
                sasaranRequest.indikatorId(),
                sasaranRequest.target(),
                sasaranRequest.realisasi(),
                sasaranRequest.satuan(),
                sasaranRequest.tahun(),
                sasaranRequest.jenisRealisasi()
        );
    }
}
