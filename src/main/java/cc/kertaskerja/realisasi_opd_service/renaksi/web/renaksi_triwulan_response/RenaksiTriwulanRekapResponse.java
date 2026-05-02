package cc.kertaskerja.realisasi_opd_service.renaksi.web.renaksi_triwulan_response;

public record RenaksiTriwulanRekapResponse(
        String renaksiId,
        String renaksi,
        String rekinId,
        String rekin,
        String targetId,
        TriwulanDetailResponse tw1,
        TriwulanDetailResponse tw2,
        TriwulanDetailResponse tw3,
        TriwulanDetailResponse tw4
) {
}
