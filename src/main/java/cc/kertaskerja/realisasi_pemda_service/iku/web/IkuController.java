package cc.kertaskerja.realisasi_pemda_service.iku.web;

import cc.kertaskerja.realisasi_pemda_service.iku.domain.Iku;
import cc.kertaskerja.realisasi_pemda_service.iku.domain.IkuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("ikus")
public class IkuController {
    private final IkuService ikuService;

    public IkuController(IkuService ikuService) {
        this.ikuService = ikuService;
    }

    @GetMapping
    public Flux<Iku> getAllRealisasiIku() {
        return ikuService.getAllIku();
    }

    @GetMapping("{tahun}")
    public Flux<Iku> getAllRealisasiIkuByTahun(@PathVariable String tahun) {
        return ikuService.getAllIkuByTahun(tahun);
    }
}
