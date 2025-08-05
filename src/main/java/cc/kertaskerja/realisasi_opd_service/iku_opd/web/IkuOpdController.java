package cc.kertaskerja.realisasi_opd_service.iku_opd.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.kertaskerja.realisasi_opd_service.iku_opd.domain.IkuOpd;
import cc.kertaskerja.realisasi_opd_service.iku_opd.domain.IkuOpdService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("iku_opd")
public class IkuOpdController {
   private final IkuOpdService ikuOpdService;

    public IkuOpdController(IkuOpdService ikuOpdService) {
        this.ikuOpdService = ikuOpdService;
    }

    @GetMapping("/{kodeOpd}")
    public Flux<IkuOpd> getAllRealisasiIkuByKodeOpd(@PathVariable String kodeOpd) {
       return ikuOpdService.getAllIkuOpd(kodeOpd);
    }

    @GetMapping("/{kodeOpd}/by-tahun/{tahun}")
    public Flux<IkuOpd> getAllRealisasiIkuByTahunAndKodeOpd(@PathVariable String tahun, @PathVariable String kodeOpd) {
        return ikuOpdService.getAllIkuOpdByTahunAndKodeOpd(tahun, kodeOpd);
    }
}
