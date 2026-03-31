package cc.kertaskerja.realisasi_individu_service.renaksi.web;

import cc.kertaskerja.realisasi_individu_service.renaksi.domain.Renaksi;
import cc.kertaskerja.realisasi_individu_service.renaksi.domain.RenaksiService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("renaksi")
@Tag(name = "Individu - Renaksi", description = "Endpoint realisasi renaksi tingkat individu")
public class RenaksiController {
    private final RenaksiService renaksiService;

    public RenaksiController(RenaksiService renaksiService) {
        this.renaksiService = renaksiService;
    }

    @GetMapping
    @Operation(summary = "Ambil semua realisasi renaksi", description = "Mengambil seluruh data realisasi renaksi.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getAllRealisasiRenaksi() {
        return renaksiService.getAllRealisasiRenaksi();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "Ambil realisasi renaksi berdasarkan ID", description = "Mengambil satu data realisasi renaksi berdasarkan ID internal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data realisasi renaksi ditemukan", content = @Content(schema = @Schema(implementation = Renaksi.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Data tidak ditemukan", content = @Content)
    })
    public Mono<Renaksi> getRealisasiRenaksi(
            @Parameter(description = "ID internal realisasi renaksi", example = "1") @PathVariable("id") Long id) {
        return renaksiService.getRealisasiRenaksiById(id);
    }

    @GetMapping("/by-renaksi/{renaksiId}")
    @Operation(summary = "Cari realisasi renaksi berdasarkan ID renaksi", description = "Mengambil daftar realisasi renaksi berdasarkan `renaksiId`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getRealisasiRenaksiByRenaksiId(
            @Parameter(description = "ID renaksi", example = "RENAKSI-001") @PathVariable String renaksiId) {
        return renaksiService.getRealisasiRenaksiByRenaksiId(renaksiId);
    }

    @GetMapping("/by-rekin/{rekinId}")
    @Operation(summary = "Cari realisasi renaksi berdasarkan ID rekin", description = "Mengambil daftar realisasi renaksi berdasarkan `rekinId`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getRealisasiRenaksiByRekinId(
            @Parameter(description = "ID rekin", example = "REKIN-001") @PathVariable String rekinId) {
        return renaksiService.getRealisasiRenaksiByRekinId(rekinId);
    }

    @GetMapping("/by-tahun/{tahun}")
    @Operation(summary = "Cari realisasi renaksi per tahun", description = "Mengambil realisasi renaksi berdasarkan tahun.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getRealisasiRenaksiByTahun(
            @Parameter(description = "Tahun realisasi", example = "2025") @PathVariable String tahun) {
        return renaksiService.getRealisasiRenaksiByTahun(tahun);
    }

    @GetMapping("/{renaksiId}/target/{targetId}/by-bulan/{bulan}/by-tahun/{tahun}")
    @Operation(summary = "Cari realisasi renaksi per bulan dan tahun", description = "Mengambil realisasi renaksi berdasarkan ID renaksi, ID target, bulan, dan tahun tertentu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getRealisasiRenaksiByBulanAndTahun(
            @Parameter(description = "ID renaksi", example = "RENAKSI-001") @PathVariable String renaksiId,
            @Parameter(description = "ID target", example = "TAR-1") @PathVariable String targetId,
            @Parameter(description = "Bulan realisasi", example = "Januari") @PathVariable String bulan,
            @Parameter(description = "Tahun realisasi", example = "2025") @PathVariable String tahun) {
        if (bulan == null || bulan.isBlank()
                || tahun == null || tahun.isBlank()
                || renaksiId == null || renaksiId.isBlank()
                || targetId == null || targetId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter bulan, tahun, renaksiId, dan targetId tidak boleh kosong");
        }
        return renaksiService.getRealisasiRenaksiByBulanAndTahunAndRenaksiIdAndTargetId(bulan, tahun, renaksiId, targetId);
    }

    @GetMapping("/by-periode/{tahunAwal}/{tahunAkhir}/rpjmd")
    @Operation(summary = "Cari realisasi renaksi periode RPJMD", description = "Mengambil realisasi renaksi pada rentang tahun RPJMD.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi periode RPJMD", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "400", description = "Parameter periode tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getRealisasiRenaksiByPeriodeRpjmd(
            @Parameter(description = "Tahun awal periode", example = "2025") @PathVariable String tahunAwal,
            @Parameter(description = "Tahun akhir periode", example = "2030") @PathVariable String tahunAkhir) {
        return renaksiService.getRealisasiRenaksiByPeriodeRpjmd(tahunAwal, tahunAkhir);
    }

    @GetMapping("/by-nip/{nip}")
    @Operation(summary = "Cari realisasi renaksi berdasarkan NIP", description = "Mengambil realisasi renaksi berdasarkan `nip`.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar realisasi renaksi", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> getRealisasiRenaksiByNip(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001") @PathVariable String nip) {
        return renaksiService.getRealisasiRenaksiByNip(nip);
    }

    @PostMapping
    @Operation(summary = "Simpan realisasi renaksi", description = "Menyimpan satu data realisasi renaksi.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data realisasi renaksi tersimpan", content = @Content(schema = @Schema(implementation = Renaksi.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<Renaksi> submitRealisasiRenaksi(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload realisasi renaksi", required = true,
                    content = @Content(schema = @Schema(implementation = RenaksiRequest.class)))
            @RequestBody @Valid RenaksiRequest renaksiRequest) {
        return renaksiService.submitRealisasiRenaksi(
                renaksiRequest.renaksiId(),
                renaksiRequest.renaksi(),
                renaksiRequest.nip(),
                renaksiRequest.rekinId(),
                renaksiRequest.rekin(),
                renaksiRequest.targetId(),
                renaksiRequest.target(),
                renaksiRequest.realisasi(),
                renaksiRequest.satuan(),
                renaksiRequest.bulan(),
                renaksiRequest.tahun(),
                renaksiRequest.jenisRealisasi()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hapus realisasi renaksi", description = "Menghapus satu data realisasi renaksi berdasarkan ID internal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data realisasi renaksi terhapus", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Data tidak ditemukan", content = @Content)
    })
    public Mono<Void> deleteRealisasiRenaksi(
            @Parameter(description = "ID internal realisasi renaksi", example = "1") @PathVariable Long id) {
        return renaksiService.deleteRealisasiRenaksi(id);
    }

    @PostMapping("/batch")
    @Operation(summary = "Simpan batch realisasi renaksi", description = "Menyimpan beberapa data realisasi renaksi dalam satu request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch berhasil disimpan", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Renaksi.class)))),
            @ApiResponse(responseCode = "400", description = "Payload batch tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Flux<Renaksi> batchSubmitRealisasiRenaksi(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Daftar payload realisasi renaksi", required = true,
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = RenaksiRequest.class)),
                            examples = @ExampleObject(name = "ArrayRequest", value = "[\n" +
                                    "  {\n" +
                                    "    \"targetRealisasiId\": \"1\",\n" +
                                    "    \"renaksiId\": \"RENAKSI-001\",\n" +
                                    "    \"renaksi\": \"Renaksi Peningkatan Infrastruktur\",\n" +
                                    "    \"nip\": \"198012312005011001\",\n" +
                                    "    \"rekinId\": \"REKIN-001\",\n" +
                                    "    \"rekin\": \"Rekin Peningkatan Infrastruktur\",\n" +
                                    "    \"targetId\": \"TAR-1\",\n" +
                                    "    \"target\": \"100\",\n" +
                                    "    \"realisasi\": 85,\n" +
                                    "    \"satuan\": \"%\",\n" +
                                    "    \"bulan\": \"Januari\",\n" +
                                    "    \"tahun\": \"2026\",\n" +
                                    "    \"jenisRealisasi\": \"NAIK\"\n" +
                                    "  }\n" +
                                    "]")))
            @RequestBody @Valid List<RenaksiRequest> renaksiRequests) {
        return renaksiService.batchSubmitRealisasiRenaksi(renaksiRequests);
    }
}
