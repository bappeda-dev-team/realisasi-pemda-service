package cc.kertaskerja.realisasi_opd_service.renaksi.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PenetapanRenaksiOpdListResponse(
        @JsonProperty("kode_opd")
        String kodeOpd,

        @JsonProperty("tahun_aktif")
        Integer tahunAktif,

        @JsonProperty("bulan")
        Integer bulan,

        Integer versi,

        @JsonProperty("is_locked")
        Boolean isLocked,

        @JsonProperty("renaksi_opds")
        List<RenaksiOpdPenetapanResponse> renaksiOpds
) {
    public PenetapanRenaksiOpdListResponse {
        if (renaksiOpds == null) renaksiOpds = List.of();
    }
}
