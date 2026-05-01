package cc.kertaskerja.realisasi_opd_service.renaksi.web.renaksi_triwulan_response;

import java.math.BigDecimal;

public record TriwulanDetailResponse(
        BigDecimal target,
        Integer realisasi,
        String satuan,
        String capaian,
        String keteranganCapaian
) {
}
