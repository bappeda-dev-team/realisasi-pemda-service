package cc.kertaskerja.pegawai.web;

import cc.kertaskerja.integration.kepegawaian.PegawaiClient;
import cc.kertaskerja.pegawai.domain.PegawaiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("pegawai")
@Tag(name = "Pegawai", description = "Endpoint daftar pegawai.")
public class PegawaiController {
    private final PegawaiService pegawaiService;

    public PegawaiController(PegawaiService pegawaiService) {
        this.pegawaiService = pegawaiService;
    }

    @GetMapping("/findAll")
    @Operation(summary = "Daftar semua pegawai", description = "Mengambil daftar seluruh pegawai dari service kepegawaian.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar pegawai", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PegawaiClient.PegawaiData.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<List<PegawaiClient.PegawaiData>> findAllPegawai() {
        return pegawaiService.findAllPegawai();
    }
}