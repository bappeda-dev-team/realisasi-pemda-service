package cc.kertaskerja.realisasi_opd_service.sasaran.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PenetapanSasaranOpdListResponse(
        @JsonProperty("kode_opd")
        String kodeOpd,

        @JsonProperty("tahun")
        Integer tahun,

        @JsonProperty("bulan")
        Integer bulan,

        List<SasaranOpdPenetapanResponse> sasaranOpds
) {
}
