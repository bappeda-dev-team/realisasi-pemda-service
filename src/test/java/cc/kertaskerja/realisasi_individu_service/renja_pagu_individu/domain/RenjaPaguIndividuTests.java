package cc.kertaskerja.realisasi_individu_service.renja_pagu_individu.domain;

import cc.kertaskerja.realisasi.domain.JenisRealisasi;
import cc.kertaskerja.renja.domain.JenisRenja;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class RenjaPaguIndividuTests {
    @Test
    void testCapaianRealisasiDibagiPagu() {
        RenjaPaguIndividu renjaPaguIndividu = new RenjaPaguIndividu(
                1L,
                "RENJA-1",
                "Program A",
                "1.02.01",
                JenisRenja.PROGRAM,
                "198012312005011001",
                "IND-1",
                "Indikator A",
                100,
                50,
                "rupiah",
                "2025",
                JenisRealisasi.NAIK,
                RenjaPaguIndividuStatus.UNCHECKED,
                null,
                "system",
                "system",
                Instant.now(),
                Instant.now(),
                1
        );

        Assertions.assertEquals("50.00%", renjaPaguIndividu.capaian());
    }

    @Test
    void testCapaianRealisasiDibagiPaguDenganDesimal() {
        RenjaPaguIndividu renjaPaguIndividu = new RenjaPaguIndividu(
                2L,
                "RENJA-2",
                "Program B",
                "1.02.02",
                JenisRenja.KEGIATAN,
                "198012312005011001",
                "IND-2",
                "Indikator B",
                200,
                75,
                "rupiah",
                "2025",
                JenisRealisasi.NAIK,
                RenjaPaguIndividuStatus.UNCHECKED,
                null,
                "system",
                "system",
                Instant.now(),
                Instant.now(),
                1
        );

        Assertions.assertEquals("37.50%", renjaPaguIndividu.capaian());
    }

    @Test
    void testCapaianPaguNol() {
        RenjaPaguIndividu renjaPaguIndividu = new RenjaPaguIndividu(
                3L,
                "RENJA-3",
                "Program C",
                "1.02.03",
                JenisRenja.SUBKEGIATAN,
                "198012312005011001",
                "IND-3",
                "Indikator C",
                0,
                75,
                "rupiah",
                "2025",
                JenisRealisasi.NAIK,
                RenjaPaguIndividuStatus.UNCHECKED,
                null,
                "system",
                "system",
                Instant.now(),
                Instant.now(),
                1
        );

        Assertions.assertEquals("0.00%", renjaPaguIndividu.capaian());
    }

    @Test
    void testCapaianRealisasiLebihBesarDariPagu() {
        RenjaPaguIndividu renjaPaguIndividu = new RenjaPaguIndividu(
                4L,
                "RENJA-4",
                "Program D",
                "1.02.04",
                JenisRenja.PROGRAM,
                "198012312005011001",
                "IND-4",
                "Indikator D",
                100,
                150,
                "rupiah",
                "2025",
                JenisRealisasi.NAIK,
                RenjaPaguIndividuStatus.UNCHECKED,
                null,
                "system",
                "system",
                Instant.now(),
                Instant.now(),
                1
        );

        Assertions.assertEquals("150.00%", renjaPaguIndividu.capaian());
    }
}
