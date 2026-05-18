package cc.kertaskerja.realisasi_opd_service.sasaran.web;

import cc.kertaskerja.realisasi_opd_service.sasaran.domain.SasaranOpdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("sasaran_opd")
@Tag(name = "OPD - Sasaran", description = "Endpoint realisasi sasaran tingkat OPD. Role `level_1`, `level_2`, `level_3`, dan `level_4` hanya diizinkan mengakses endpoint `GET` pada resource ini.")
public class SasaranOpdController {
    private final SasaranOpdService sasaranOpdService;

    public SasaranOpdController(
            SasaranOpdService sasaranOpdService
    ) {
        this.sasaranOpdService = sasaranOpdService;
    }

    @GetMapping("/{kodeOpd}/tahun/{tahun}/bulan/{bulan}")
    @Operation(summary = "Cari realisasi sasaran OPD per tahun dan bulan", description = "Mengambil realisasi sasaran OPD berdasarkan kode OPD, tahun, dan bulan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi sasaran OPD", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SasaranOpdResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<SasaranOpdResponse> getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan(
            @Parameter(description = "Kode OPD", example = "5.01.5.05.0.00.01.0000") @PathVariable String kodeOpd,
            @Parameter(description = "Tahun realisasi", example = "2026") @PathVariable String tahun,
            @Parameter(description = "Bulan realisasi", example = "1") @PathVariable String bulan) {
        return sasaranOpdService.getRealisasiSasaranOpdByTahunAndKodeOpdAndBulan(tahun, kodeOpd, bulan);
    }

    @GetMapping("/{kodeOpd}/tahun/{tahun}/penetapan")
    @Operation(summary = "Integrasi penetapan dengan realisasi sasaran OPD", description = "Menggabungkan data penetapan (dari external service) dengan data realisasi sasaran OPD berdasarkan kode OPD dan tahun. Parameter bulan bersifat opsional; jika tidak dikirim, hanya data penetapan tanpa realisasi yang dikembalikan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data penetapan terintegrasi dengan realisasi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SasaranOpdPenetapanResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<SasaranOpdPenetapanResponse> getPenetapanWithRealisasi(
            @Parameter(description = "Kode OPD", example = "5.01.5.05.0.00.01.0000") @PathVariable String kodeOpd,
            @Parameter(description = "Tahun", example = "2026") @PathVariable String tahun,
            @Parameter(description = "Bulan realisasi (opsional)", example = "1") @RequestParam(required = false) String bulan) {
        return sasaranOpdService.getPenetapanWithRealisasi(kodeOpd, Integer.parseInt(tahun), bulan);
    }

    @PostMapping
    @Operation(summary = "Simpan realisasi sasaran OPD", description = "Menyimpan satu data realisasi sasaran OPD. Role `level_1`, `level_2`, `level_3`, dan `level_4` tidak diizinkan mengakses endpoint ini.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data realisasi sasaran OPD tersimpan", content = @Content(schema = @Schema(implementation = SasaranOpdResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden untuk role level_1, level_2, level_3, dan level_4", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<SasaranOpdResponse> submitRealisasiSasaranOpd(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload realisasi sasaran OPD", required = true,
                    content = @Content(schema = @Schema(implementation = SasaranOpdRequest.class)))
            @RequestBody @Valid SasaranOpdRequest sasaranOpdRequest) {
        return sasaranOpdService.submitRealisasiSasaranOpd(sasaranOpdRequest);
    }

    @PostMapping("/create/batch")
    @Operation(summary = "Simpan batch realisasi sasaran OPD", description = "Menyimpan beberapa data realisasi sasaran OPD dalam satu request. Role `level_1`, `level_2`, `level_3`, dan `level_4` tidak diizinkan mengakses endpoint ini.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch berhasil disimpan", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SasaranOpdResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Payload batch tidak valid", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden untuk role level_1, level_2, level_3, dan level_4", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<SasaranOpdResponse> batchSubmitRealisasiSasaranOpd(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Daftar payload realisasi sasaran OPD", required = true,
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SasaranOpdRequest.class))))
            @RequestBody @Valid List<SasaranOpdRequest> sasaranOpdRequests) {
        return sasaranOpdService.batchSubmitRealisasiSasaranOpd(sasaranOpdRequests);
    }
}
