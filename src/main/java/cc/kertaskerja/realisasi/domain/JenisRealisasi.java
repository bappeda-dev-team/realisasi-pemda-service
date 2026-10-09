package cc.kertaskerja.realisasi.domain;

public enum JenisRealisasi {
    NAIK,
    IDEAL,
    TURUN;

    public static JenisRealisasi fromCapaian(Double capaian) {
        if (capaian == null || capaian == 0.0) {
            return null;
        }
        if (capaian > 20.0) {
            return NAIK;
        }
        if (capaian < 20.0) {
            return TURUN;
        }
        return IDEAL;
    }
}
