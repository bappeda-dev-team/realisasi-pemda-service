package cc.kertaskerja.realisasi_opd_service.renja.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FaktorPenunjangTargetRenjaKegiatanOpdRequest", description = "Payload untuk memperbarui faktor penunjang pada realisasi target renja OPD tingkat KEGIATAN")
public record FaktorPenunjangTargetRenjaKegiatanOpdRequest(

        @NotNull @NotEmpty
        @Schema(example = "TGT-TRG-PENETAPAN-89455")
        @JsonProperty("kode_target")
        String kodeTarget,

        @NotNull @NotEmpty
        @Schema(description = "Faktor penunjang target kegiatan", example = "Kerjasama tim yang baik")
        @JsonProperty("faktor_penunjang")
        String faktorPenunjang
) {}
