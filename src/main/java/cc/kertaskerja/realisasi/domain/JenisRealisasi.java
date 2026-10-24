package cc.kertaskerja.realisasi.domain;

/**
 * Jenis perhitungan capaian realisasi.
 *
 * <p>{@link #NAIK} dipakai ketika nilai realisasi yang semakin besar semakin baik
 * (capaian = realisasi / target * 100).</p>
 *
 * <p>{@link #TURUN} dipakai ketika nilai realisasi yang semakin kecil semakin baik
 * (capaian = 1 + (1 - realisasi / target) * 100).</p>
 *
 * <p>Nilai ini diinput oleh user pada endpoint realisasi (POST) dan wajib diisi
 * dengan salah satu dari {@code NAIK} atau {@code TURUN}.</p>
 */
public enum JenisRealisasi {
    NAIK,
    TURUN;

    /**
     * Mengembalikan nilai yang diberikan, atau {@link #NAIK} bila {@code null}.
     * Dipakai sebagai fallback defensif di layer service.
     */
    public static JenisRealisasi defaultIfNull(JenisRealisasi jenisRealisasi) {
        return jenisRealisasi != null ? jenisRealisasi : NAIK;
    }
}
