package cc.kertaskerja.realisasi_laporan_rb_service.tematik.web;

import cc.kertaskerja.realisasi_laporan_rb_service.tematik.domain.LaporanRBTematikService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("laporanrbtematik")
@Tag(name = "Laporan RB Tematik", description = "Endpoint realisasi laporan RB tematik.")
public class LaporanRBTematikController {
    private final LaporanRBTematikService laporanRBTematikService;

    public LaporanRBTematikController(LaporanRBTematikService laporanRBTematikService) {
        this.laporanRBTematikService = laporanRBTematikService;
    }

    @GetMapping("/nip/{nip}/kodeOpd/{kodeOpd}/tahun/{tahun}/perencanaan")
    @Operation(summary = "Integrasi perencanaan dengan realisasi laporan RB tematik", description = "Menggabungkan data perencanaan (dari external service) dengan data realisasi laporan RB tematik berdasarkan NIP, kode OPD, dan tahun. Realisasi dihitung pada level RB (laporan, indikator, target); objek rencana aksi dipertahankan apa adanya. Parameter bulan bersifat opsional; jika tidak dikirim, realisasi diakumulasi untuk seluruh tahun.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data perencanaan terintegrasi dengan realisasi",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerencanaanLaporanRBTematikResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<PerencanaanLaporanRBTematikResponse> getPerencanaanByNipAndTahun(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001", required = true) @PathVariable String nip,
            @Parameter(description = "Kode OPD", example = "1.01.0.00.0.00.01.0000", required = true) @PathVariable String kodeOpd,
            @Parameter(description = "Tahun perencanaan", example = "2026", required = true) @PathVariable String tahun,
            @Parameter(description = "Bulan realisasi (opsional)", example = "1", required = false) @RequestParam(required = false) String bulan) {
        if (nip == null || nip.isBlank() || kodeOpd == null || kodeOpd.isBlank() || tahun == null || tahun.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter nip, kodeOpd, dan tahun tidak boleh kosong");
        }
        return laporanRBTematikService.getPerencanaanByNip(nip, kodeOpd, Integer.parseInt(tahun), bulan);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buat realisasi target laporan RB tematik (upsert)", description = "Menyimpan realisasi target laporan RB tematik. Jika data dengan composite key yang sama sudah ada, akan diperbarui.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Realisasi tersimpan", content = @Content(schema = @Schema(implementation = LaporanRBTematikResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<LaporanRBTematikResponse> createLaporanRBTematik(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload realisasi target laporan RB tematik", required = true,
                    content = @Content(schema = @Schema(implementation = LaporanRBTematikRequest.class)))
            @RequestBody @Valid LaporanRBTematikRequest request) {
        return laporanRBTematikService.createLaporanRBTematik(request);
    }

    @PostMapping("/faktor-penunjang")
    @Operation(summary = "Perbarui faktor penunjang laporan RB tematik", description = "Memperbarui hanya field faktor_penunjang pada record yang cocok dengan composite key (kodeOpd, nip, idRbTematik, idIndikatorRbTematik, idTargetRbTematik, tahun, bulan).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil diperbarui", content = @Content(schema = @Schema(implementation = LaporanRBTematikResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Laporan RB tematik tidak ditemukan", content = @Content)
    })
    public Mono<LaporanRBTematikResponse> updateFaktorPenunjang(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload faktor penunjang laporan RB tematik", required = true,
                    content = @Content(schema = @Schema(implementation = FaktorPenunjangLaporanRBTematikRequest.class)))
            @RequestBody @Valid FaktorPenunjangLaporanRBTematikRequest req) {
        return laporanRBTematikService.updateFaktorPenunjang(req);
    }

    @PostMapping("/faktor-penghambat")
    @Operation(summary = "Perbarui faktor penghambat laporan RB tematik", description = "Memperbarui hanya field faktor_penghambat pada record yang cocok dengan composite key (kodeOpd, nip, idRbTematik, idIndikatorRbTematik, idTargetRbTematik, tahun, bulan).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil diperbarui", content = @Content(schema = @Schema(implementation = LaporanRBTematikResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Laporan RB tematik tidak ditemukan", content = @Content)
    })
    public Mono<LaporanRBTematikResponse> updateFaktorPenghambat(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload faktor penghambat laporan RB tematik", required = true,
                    content = @Content(schema = @Schema(implementation = FaktorPenghambatLaporanRBTematikRequest.class)))
            @RequestBody @Valid FaktorPenghambatLaporanRBTematikRequest req) {
        return laporanRBTematikService.updateFaktorPenghambat(req);
    }

    @PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file bukti pendukung", description = "Mengunggah file dan mengembalikan string URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL file berhasil dihasilkan", content = @Content),
            @ApiResponse(responseCode = "400", description = "File tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<Map<String, String>> uploadFile(
            @Parameter(description = "File yang akan diupload", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestPart("file") FilePart file) {
        return laporanRBTematikService.uploadFile(file)
                .map(url -> Map.of("url", url));
    }
}