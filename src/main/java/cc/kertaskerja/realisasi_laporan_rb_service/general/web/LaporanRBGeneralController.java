package cc.kertaskerja.realisasi_laporan_rb_service.general.web;

import cc.kertaskerja.realisasi_laporan_rb_service.general.domain.LaporanRBGeneralService;
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
@RequestMapping("laporanrbgeneral")
@Tag(name = "Laporan RB General", description = "Endpoint realisasi laporan RB general.")
public class LaporanRBGeneralController {
    private final LaporanRBGeneralService laporanRBGeneralService;

    public LaporanRBGeneralController(LaporanRBGeneralService laporanRBGeneralService) {
        this.laporanRBGeneralService = laporanRBGeneralService;
    }

    @GetMapping("/nip/{nip}/kodeOpd/{kodeOpd}/tahun/{tahun}/perencanaan")
    @Operation(summary = "Integrasi perencanaan dengan realisasi laporan RB general", description = "Menggabungkan data perencanaan (dari external service) dengan data realisasi laporan RB general berdasarkan NIP, kode OPD, dan tahun. Realisasi dihitung pada level RB (laporan, indikator, target); objek rencana aksi dipertahankan apa adanya. Parameter bulan bersifat opsional; jika tidak dikirim, realisasi diakumulasi untuk seluruh tahun.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data perencanaan terintegrasi dengan realisasi",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerencanaanLaporanRBGeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<PerencanaanLaporanRBGeneralResponse> getPerencanaanByNipAndTahun(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001", required = true) @PathVariable String nip,
            @Parameter(description = "Kode OPD", example = "1.01.0.00.0.00.01.0000", required = true) @PathVariable String kodeOpd,
            @Parameter(description = "Tahun perencanaan", example = "2026", required = true) @PathVariable String tahun,
            @Parameter(description = "Bulan realisasi (opsional)", example = "1", required = false) @RequestParam(required = false) String bulan) {
        if (nip == null || nip.isBlank() || kodeOpd == null || kodeOpd.isBlank() || tahun == null || tahun.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter nip, kodeOpd, dan tahun tidak boleh kosong");
        }
        return laporanRBGeneralService.getPerencanaanByNip(nip, kodeOpd, Integer.parseInt(tahun), bulan);
    }

    @GetMapping("/nip/{nip}/kodeOpd/{kodeOpd}/tahun/{tahun}/laporan")
    @Operation(summary = "Laporan realisasi RB general per target", description = "Mengembalikan baris laporan flat (satu baris per target indikator dengan tahun_next = tahun) yang sudah digabung dengan realisasi terakhir per target (record dengan id terbesar). Target tanpa realisasi tetap muncul dengan realisasi null. Parameter bulan bersifat opsional; jika tidak dikirim, dipakai seluruh realisasi pada tahun berjalan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Laporan per target berhasil dibuat",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LaporanRBGeneralPerTargetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parameter tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<LaporanRBGeneralPerTargetResponse> getLaporanPerTarget(
            @Parameter(description = "NIP pelaksana", example = "198012312005011001", required = true) @PathVariable String nip,
            @Parameter(description = "Kode OPD", example = "1.01.0.00.0.00.01.0000", required = true) @PathVariable String kodeOpd,
            @Parameter(description = "Tahun laporan", example = "2026", required = true) @PathVariable String tahun,
            @Parameter(description = "Bulan realisasi (opsional)", example = "1", required = false) @RequestParam(required = false) String bulan) {
        if (nip == null || nip.isBlank() || kodeOpd == null || kodeOpd.isBlank() || tahun == null || tahun.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter nip, kodeOpd, dan tahun tidak boleh kosong");
        }
        return laporanRBGeneralService.getLaporanPerTarget(nip, kodeOpd, Integer.parseInt(tahun), bulan);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Buat realisasi target laporan RB general (upsert)", description = "Menyimpan realisasi target laporan RB general. Jika data dengan composite key yang sama sudah ada, akan diperbarui.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Realisasi tersimpan", content = @Content(schema = @Schema(implementation = LaporanRBGeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public Mono<LaporanRBGeneralResponse> createLaporanRBGeneral(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload realisasi target laporan RB general", required = true,
                    content = @Content(schema = @Schema(implementation = LaporanRBGeneralRequest.class)))
            @RequestBody @Valid LaporanRBGeneralRequest request) {
        return laporanRBGeneralService.createLaporanRBGeneral(request);
    }

    @PostMapping("/faktor-penunjang")
    @Operation(summary = "Perbarui faktor penunjang laporan RB general", description = "Memperbarui hanya field faktor_penunjang pada record yang cocok dengan composite key (kodeOpd, nip, idRbGeneral, idIndikatorRbGeneral, idTargetRbGeneral, tahun, bulan).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil diperbarui", content = @Content(schema = @Schema(implementation = LaporanRBGeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Laporan RB general tidak ditemukan", content = @Content)
    })
    public Mono<LaporanRBGeneralResponse> updateFaktorPenunjang(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload faktor penunjang laporan RB general", required = true,
                    content = @Content(schema = @Schema(implementation = FaktorPenunjangLaporanRBGeneralRequest.class)))
            @RequestBody @Valid FaktorPenunjangLaporanRBGeneralRequest req) {
        return laporanRBGeneralService.updateFaktorPenunjang(req);
    }

    @PostMapping("/faktor-penghambat")
    @Operation(summary = "Perbarui faktor penghambat laporan RB general", description = "Memperbarui hanya field faktor_penghambat pada record yang cocok dengan composite key (kodeOpd, nip, idRbGeneral, idIndikatorRbGeneral, idTargetRbGeneral, tahun, bulan).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil diperbarui", content = @Content(schema = @Schema(implementation = LaporanRBGeneralResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload tidak valid", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Laporan RB general tidak ditemukan", content = @Content)
    })
    public Mono<LaporanRBGeneralResponse> updateFaktorPenghambat(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload faktor penghambat laporan RB general", required = true,
                    content = @Content(schema = @Schema(implementation = FaktorPenghambatLaporanRBGeneralRequest.class)))
            @RequestBody @Valid FaktorPenghambatLaporanRBGeneralRequest req) {
        return laporanRBGeneralService.updateFaktorPenghambat(req);
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
        return laporanRBGeneralService.uploadFile(file)
                .map(url -> Map.of("url", url));
    }
}