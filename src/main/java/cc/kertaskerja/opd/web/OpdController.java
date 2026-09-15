package cc.kertaskerja.opd.web;

import cc.kertaskerja.integration.kepegawaian.OpdClient;
import cc.kertaskerja.opd.domain.OpdService;
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
@RequestMapping("opd")
@Tag(name = "OPD", description = "Endpoint daftar OPD.")
public class OpdController {
    private final OpdService opdService;

    public OpdController(OpdService opdService) {
        this.opdService = opdService;
    }

    @GetMapping("/findAll")
    @Operation(summary = "Daftar semua OPD", description = "Mengambil daftar seluruh OPD dari service kepegawaian.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar OPD", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OpdClient.OpdData.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<List<OpdClient.OpdData>> findAllOpd() {
        return opdService.findAllOpd();
    }
}