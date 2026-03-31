package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.web;

import java.util.List;

import cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain.RenjaPaguIndividu;
import cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain.RenjaPaguIndividuService;
import cc.kertaskerja.renja.domain.JenisRenja;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("renja_pagu_individu")
@Tag(name = "Individu - Renja Pagu", description = "Endpoint realisasi renja pagu tingkat individu")
public class RenjaPaguIndividuController {
    private final RenjaPaguIndividuService renjaPaguIndividuService;

    public RenjaPaguIndividuController(RenjaPaguIndividuService renjaPaguIndividuService) {
        this.renjaPaguIndividuService = renjaPaguIndividuService;
    }

    @GetMapping
    @Operation(summary = "Ambil semua realisasi renja pagu individu", description = "Mengambil seluruh data realisasi renja pagu individu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renja pagu individu", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> getAllRealisasiRenjaPaguIndividu() {
        return renjaPaguIndividuService.getAllRealisasiRenjaPaguIndividu();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "Ambil realisasi renja pagu individu berdasarkan ID", description = "Mengambil satu data realisasi renja pagu individu berdasarkan ID internal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data realisasi renja pagu individu ditemukan", content = @Content(schema = @Schema(implementation = RenjaPaguIndividu.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Data tidak ditemukan", content = @Content)
    })
    public Mono<RenjaPaguIndividu> getRealisasiRenjaPaguIndividu(
            @Parameter(description = "ID internal realisasi renja pagu individu", example = "1") @PathVariable Long id) {
        return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuById(id);
    }

    @GetMapping("/by-renja/{renjaId}")
    @Operation(summary = "Cari realisasi renja pagu individu berdasarkan ID renja", description = "Mengambil daftar realisasi renja pagu individu berdasarkan `renjaId`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renja pagu individu", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByRenjaId(
            @Parameter(description = "ID renja", example = "RENJA-001") @PathVariable String renjaId) {
        return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByRenjaId(renjaId);
    }

    @GetMapping("/by-nip/{nip}")
    @Operation(summary = "Cari realisasi renja pagu individu berdasarkan NIP", description = "Mengambil seluruh realisasi renja pagu untuk satu NIP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renja pagu individu", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByNip(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001") @PathVariable String nip) {
        return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByNip(nip);
    }

    @GetMapping("/{nip}/by-tahun/{tahun}")
    @Operation(summary = "Cari realisasi renja pagu individu per tahun", description = "Mengambil realisasi renja pagu individu berdasarkan NIP dan tahun, dapat difilter dengan `renjaId` atau `jenisRenja` + `kodeRenja`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renja pagu individu", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByTahunAndNip(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001") @PathVariable String nip,
            @Parameter(description = "Tahun realisasi", example = "2025") @PathVariable String tahun,
            @Parameter(description = "Filter opsional ID renja", example = "RENJA-001") @RequestParam(required = false) String renjaId,
            @Parameter(description = "Filter opsional jenis renja", example = "PROGRAM") @RequestParam(required = false) JenisRenja jenisRenja,
            @Parameter(description = "Filter opsional kode renja", example = "1.02.01") @RequestParam(required = false) String kodeRenja) {
        if (renjaId != null && !renjaId.isBlank()) {
            return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByTahunAndRenjaIdAndNip(tahun, renjaId, nip);
        }
        if (jenisRenja != null && kodeRenja != null && !kodeRenja.isBlank()) {
            return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByTahunAndJenisRenjaAndKodeRenjaAndNip(tahun, jenisRenja, kodeRenja, nip);
        }
        return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByTahunAndNip(tahun, nip);
    }

    @GetMapping("/by-nip/{nip}/by-kode")
    @Operation(summary = "Cari realisasi renja pagu individu berdasarkan kode renja", description = "Mengambil realisasi renja pagu individu berdasarkan NIP, jenis renja, dan kode renja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renja pagu individu", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByJenisRenjaAndKodeRenjaAndNip(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001") @PathVariable String nip,
            @Parameter(description = "Jenis level renja", example = "PROGRAM") @RequestParam JenisRenja jenisRenja,
            @Parameter(description = "Kode renja sesuai jenis", example = "1.02.01") @RequestParam String kodeRenja) {
        return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByJenisRenjaAndKodeRenjaAndNip(jenisRenja, kodeRenja, nip);
    }

    @GetMapping("/{nip}/by-periode/{tahunAwal}/{tahunAkhir}/rpjmd")
    @Operation(summary = "Cari realisasi renja pagu individu periode RPJMD", description = "Mengambil realisasi renja pagu individu pada rentang tahun RPJMD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renja pagu individu periode RPJMD", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter periode tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> getRealisasiRenjaPaguIndividuByPeriodeRpjmd(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001") @PathVariable String nip,
            @Parameter(description = "Tahun awal periode", example = "2025") @PathVariable String tahunAwal,
            @Parameter(description = "Tahun akhir periode", example = "2030") @PathVariable String tahunAkhir) {
        return renjaPaguIndividuService.getRealisasiRenjaPaguIndividuByPeriodeRpjmd(tahunAwal, tahunAkhir, nip);
    }

    @PostMapping
    @Operation(summary = "Simpan realisasi renja pagu individu", description = "Menyimpan satu data realisasi renja pagu individu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data realisasi renja pagu individu tersimpan", content = @Content(schema = @Schema(implementation = RenjaPaguIndividu.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<RenjaPaguIndividu> submitRealisasiRenjaPaguIndividu(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload realisasi renja pagu individu", required = true,
                    content = @Content(schema = @Schema(implementation = RenjaPaguIndividuRequest.class)))
            @RequestBody @Valid RenjaPaguIndividuRequest renjaPaguIndividuRequest) {
        return renjaPaguIndividuService.submitRealisasiRenjaPaguIndividu(
                renjaPaguIndividuRequest.renjaId(),
                renjaPaguIndividuRequest.renja(),
                renjaPaguIndividuRequest.kodeRenja(),
                renjaPaguIndividuRequest.jenisRenja(),
                renjaPaguIndividuRequest.nip(),
                renjaPaguIndividuRequest.idIndikator(),
                renjaPaguIndividuRequest.indikator(),
                renjaPaguIndividuRequest.pagu(),
                renjaPaguIndividuRequest.realisasi(),
                renjaPaguIndividuRequest.satuan(),
                renjaPaguIndividuRequest.tahun(),
                renjaPaguIndividuRequest.jenisRealisasi()
        );
    }

    @PostMapping("/batch")
    @Operation(summary = "Simpan batch realisasi renja pagu individu", description = "Menyimpan beberapa data realisasi renja pagu individu dalam satu request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch berhasil disimpan", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividu.class)))),
            @ApiResponse(responseCode = "400", description = "Payload batch tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<RenjaPaguIndividu> batchSubmitRealisasiRenjaPaguIndividu(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Daftar payload realisasi renja pagu individu", required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = RenjaPaguIndividuRequest.class)),
                            examples = @ExampleObject(name = "ArrayRequest", value = "[\n" +
                                    "  {\n" +
                                    "    \"targetRealisasiId\": 10,\n" +
                                    "    \"renjaId\": \"RENJA-001\",\n" +
                                    "    \"renja\": \"Program Pembangunan Jalan\",\n" +
                                    "    \"kodeRenja\": \"1.02.01\",\n" +
                                    "    \"jenisRenja\": \"PROGRAM\",\n" +
                                    "    \"nip\": \"198012312005011001\",\n" +
                                    "    \"idIndikator\": \"IND-REN-123\",\n" +
                                    "    \"indikator\": \"Persentase capaian renja\",\n" +
                                    "    \"pagu\": 100000000,\n" +
                                    "    \"realisasi\": 70000000,\n" +
                                    "    \"satuan\": \"Rp\",\n" +
                                    "    \"tahun\": \"2026\",\n" +
                                    "    \"jenisRealisasi\": \"NAIK\"\n" +
                                    "  }\n" +
                                    "]")))
            @RequestBody @Valid List<RenjaPaguIndividuRequest> renjaPaguIndividuRequests) {
        return renjaPaguIndividuService.batchSubmitRealisasiRenjaPaguIndividu(renjaPaguIndividuRequests);
    }
}
