package cc.kertaskerja.capaian.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapaianTest {

    @Test
    void testHasilCapaian_TargetIsEmpty_ShouldReturnZero() {
        Capaian capaian = new Capaian(50.0, "", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(0.0, result);
    }

    @Test
    void testHasilCapaian_RealisasiIsZero_ShouldReturnZero() {
        Capaian capaian = new Capaian(0.0, "100", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(0.0, result);
    }

    @Test
    void testHasilCapaian_SingleNumericTarget_ShouldCalculateCorrectly() {
        Capaian capaian = new Capaian(50.0, "100", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(50.0, result);
    }

    @Test
    void testHasilCapaian_MultipleNumericTargets_WhenRealisasiIsMoreThanRange_ShouldReturn100() {
        Capaian capaian = new Capaian(120.0, "50-100", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(100.0, result);
    }

    @Test
    void testHasilCapaian_MultipleNumericTargets_WhenRealisasiIsLessThanRange_ShouldCalculateMin() {
        Capaian capaian = new Capaian(30.0, "50-100", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(60.0, result);
    }

    @Test
    void testHasilCapaian_MultipleNumericTargets_WhenRealisasiInBetweenRange_ShouldCalculateFromMax() {
        Capaian capaian = new Capaian(80.0, "50-100", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(80.0, result);
    }

    @Test
    void testHasilCapaian_TargetContainsSpacesAndCommas_ShouldParseCorrectly() {
        Capaian capaian = new Capaian(75.0, " 200 , 100 ", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(75.0, result);
    }

    @Test
    void testHasilCapaian_TargetIsNull_ShouldReturnZero() {
        Capaian capaian = new Capaian(50.0, null, JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(0.0, result);
    }

    @Test
    void testHasilCapaian_TargetContainsInvalidValues_ShouldThrowException() {
        assertThrows(NumberFormatException.class, () -> new Capaian(80.0, "100-abc", JenisRealisasi.NAIK).hasilCapaian());
    }

    @Test
    void testHasilCapaian_TargetIsSingleZero_ShouldReturnZero() {
        Capaian capaian = new Capaian(50.0, "0", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(0.0, result);
    }

    @Test
    void testHasilCapaian_RealisasiWithDecimalAndNumericTarget_ShouldCalculateCorrectly() {
        Capaian capaian = new Capaian(25.5, "50", JenisRealisasi.NAIK);
        Double result = capaian.hasilCapaian();
        assertEquals(51.0, result);
    }
}