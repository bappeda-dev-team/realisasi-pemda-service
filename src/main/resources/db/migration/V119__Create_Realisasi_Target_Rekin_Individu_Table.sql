CREATE TABLE realisasi_target_rekin_individu (
    id                      BIGSERIAL PRIMARY KEY   NOT NULL,
    kode_opd                VARCHAR(255)            NOT NULL,
    nip                     VARCHAR(255)            NOT NULL,
    tahun                   VARCHAR(255)            NOT NULL,
    bulan                   VARCHAR(255)            NOT NULL,
    kode_pk_rekin           VARCHAR(255)            NOT NULL,
    kode_indikator_pk_rekin VARCHAR(255)            NOT NULL,
    kode_target_pk_rekin    VARCHAR(255)            NOT NULL,
    realisasi               NUMERIC(20,5)           NOT NULL,
    jenis_realisasi         VARCHAR(255)            NOT NULL,
    faktor_penunjang        VARCHAR(255)            NOT NULL,
    faktor_penghambat       VARCHAR(255)            NOT NULL,
    created_by              VARCHAR(100)            NOT NULL,
    last_modified_by        VARCHAR(100)            NOT NULL,
    created_date            TIMESTAMP,
    last_modified_date      TIMESTAMP
);
