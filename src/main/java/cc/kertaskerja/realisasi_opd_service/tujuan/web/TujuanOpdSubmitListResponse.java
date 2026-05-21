package cc.kertaskerja.realisasi_opd_service.tujuan.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TujuanOpdSubmitListResponse(
        @JsonProperty("kode_opd")
        String kodeOpd,

        @JsonProperty("tahun")
        Integer tahun,

        List<TujuanOpdResponse> tujuanOpds
) {
}
