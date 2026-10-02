package cc.kertaskerja.integration.penetapan.renaksi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PenetapanRenaksiOpd {

    public record PenetapanRenaksiOpdRoot(
            @JsonProperty("kode_opd") String kodeOpd,
            @JsonProperty("tahun_aktif") Integer tahunAktif,
            Integer versi,
            @JsonProperty("is_locked") Boolean isLocked,
            @JsonProperty("RenaksiOpds") @JsonAlias("renaksi_opds") List<RenaksiPenetapanData> renaksiOpds
    ) {
        public PenetapanRenaksiOpdRoot {
            if (renaksiOpds == null) renaksiOpds = List.of();
        }
    }

    public record RenaksiPenetapanData(
            @JsonProperty("kode_rencana_aksi_opd") String kodeRencanaAksiOpd,
            @JsonProperty("kode_opd") String kodeOpd,
            @JsonProperty("kode_sasaran_opd") String kodeSasaranOpd,
            @JsonProperty("kode_pk") String kodePk,
            @JsonProperty("nama_pk") String namaPk,
            @JsonProperty("pegawai_id") String pegawaiId,
            @JsonProperty("kode_subkegiatan") String kodeSubkegiatan,
            @JsonProperty("nama_subkegiatan") String namaSubkegiatan,
            @JsonProperty("anggaran_renaksi") Long anggaranRenaksi,
            Integer tahun,
            Integer tw1,
            Integer tw2,
            Integer tw3,
            Integer tw4
    ) {}
}
