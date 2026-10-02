package cc.kertaskerja.integration.penetapan;

import cc.kertaskerja.integration.penetapan.renaksi.PenetapanRenaksiOpd;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PenetapanRenaksiOpdTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String PAYLOAD = """
            {
              "data": {
                "kode_opd": "8.01.0.00.0.00.01.0000",
                "tahun_aktif": 2026,
                "versi": 3,
                "is_locked": true,
                "RenaksiOpds": [
                  {
                    "kode_rencana_aksi_opd": "REN-OPD-1",
                    "kode_opd": "",
                    "kode_sasaran_opd": "SAS-OPD-5975",
                    "kode_pk": "REKIN-PEG-2026-19928",
                    "nama_pk": "Terlaksananya Koordinasi antar Pimpinan Daerah di Kabupaten",
                    "pegawai_id": "198607022010011013",
                    "kode_subkegiatan": "",
                    "nama_subkegiatan": "",
                    "anggaran_renaksi": 168340222,
                    "tahun": 2026,
                    "tw1": 25,
                    "tw2": 25,
                    "tw3": 35,
                    "tw4": 15
                  }
                ]
              }
            }
            """;

    private PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot parseData() throws Exception {
        var rootNode = objectMapper.readTree(PAYLOAD);
        return objectMapper.treeToValue(rootNode.get("data"), PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot.class);
    }

    @Test
    void shouldParseRootMetadata() throws Exception {
        var root = parseData();

        assertEquals("8.01.0.00.0.00.01.0000", root.kodeOpd());
        assertEquals(2026, root.tahunAktif());
        assertEquals(3, root.versi());
        assertEquals(true, root.isLocked());
    }

    @Test
    void shouldParseRenaksiOpdsWithCapitalisedKey() throws Exception {
        var root = parseData();

        assertEquals(1, root.renaksiOpds().size());
        var renaksi = root.renaksiOpds().getFirst();
        assertEquals("REN-OPD-1", renaksi.kodeRencanaAksiOpd());
        assertEquals("", renaksi.kodeOpd());
        assertEquals("SAS-OPD-5975", renaksi.kodeSasaranOpd());
        assertEquals("REKIN-PEG-2026-19928", renaksi.kodePk());
        assertEquals("Terlaksananya Koordinasi antar Pimpinan Daerah di Kabupaten", renaksi.namaPk());
        assertEquals("198607022010011013", renaksi.pegawaiId());
        assertEquals(168340222L, renaksi.anggaranRenaksi());
        assertEquals(2026, renaksi.tahun());
        assertEquals(25, renaksi.tw1());
        assertEquals(25, renaksi.tw2());
        assertEquals(35, renaksi.tw3());
        assertEquals(15, renaksi.tw4());
    }

    @Test
    void shouldAlsoAcceptLowercaseRenaksiOpdsKey() throws Exception {
        var root = objectMapper.readValue(
                """
                {"kode_opd":"8.01.0.00.0.00.01.0000","tahun_aktif":2026,"versi":3,"is_locked":true,"renaksi_opds":[]}
                """,
                PenetapanRenaksiOpd.PenetapanRenaksiOpdRoot.class
        );

        assertTrue(root.renaksiOpds().isEmpty());
    }
}
