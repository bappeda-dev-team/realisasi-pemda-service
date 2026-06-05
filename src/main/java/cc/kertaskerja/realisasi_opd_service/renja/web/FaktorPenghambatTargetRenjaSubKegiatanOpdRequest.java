package cc.kertaskerja.realisasi_opd_service.renja.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FaktorPenghambatTargetRenjaSubKegiatanOpdRequest", description = "Payload untuk memperbarui faktor penghambat pada realisasi target renja OPD tingkat SUBKEGIATAN")
public record FaktorPenghambatTargetRenjaSubKegiatanOpdRequest(

        @NotNull @NotEmpty
        @Schema(example = "TGT-TRG-PENETAPAN-89455")
        @JsonProperty("kode_target")
        String kodeTarget,

        @NotNull @NotEmpty
        @Schema(description = "Faktor penghambat target subkegiatan", example = "Keterbatasan anggaran")
        @JsonProperty("faktor_penghambat")
        String faktorPenghambat
) {}
