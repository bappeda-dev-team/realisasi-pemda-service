package cc.kertaskerja.realisasi_individu_service.renja.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "FaktorPenunjangTargetRenjaSubKegiatanRequest", description = "Payload untuk memperbarui faktor penunjang pada realisasi target renja individu tingkat SUBKEGIATAN")
public record FaktorPenunjangTargetRenjaSubKegiatanRequest(

        @NotNull @NotEmpty
        @Schema(example = "TGT-TRG-PENETAPAN-89457")
        @JsonProperty("kode_target")
        String kodeTarget,

        @NotNull @NotEmpty
        @Schema(description = "Faktor penunjang target subkegiatan", example = "Kerjasama tim yang baik")
        @JsonProperty("faktor_penunjang")
        String faktorPenunjang
) {}
