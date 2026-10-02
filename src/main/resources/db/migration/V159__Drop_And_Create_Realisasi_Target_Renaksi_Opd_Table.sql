DROP TABLE IF EXISTS renaksi_opd;

CREATE TABLE IF NOT EXISTS realisasi_target_renaksi_opd
(
    id                    BIGSERIAL PRIMARY KEY NOT NULL,
    kode_opd              VARCHAR(255)   NOT NULL,
    tahun                 VARCHAR(255)   NOT NULL,
    bulan                 VARCHAR(255)   NOT NULL,
    kode_rencana_aksi_opd VARCHAR(255)   NOT NULL,
    realisasi             NUMERIC(20, 5) NOT NULL,
    jenis_realisasi       VARCHAR(255)   NOT NULL,
    faktor_penunjang      TEXT           NOT NULL DEFAULT '',
    faktor_penghambat     TEXT           NOT NULL DEFAULT '',
    created_by            VARCHAR(100)   NULL,
    last_modified_by      VARCHAR(100)   NULL,
    created_date          TIMESTAMP      NULL,
    last_modified_date    TIMESTAMP      NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_realisasi_target_renaksi_opd_kode
    ON realisasi_target_renaksi_opd (kode_opd, tahun, bulan, kode_rencana_aksi_opd);
