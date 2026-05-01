CREATE TABLE renaksi_opd
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    renaksi_id         varchar(255)          NOT NULL,
    renaksi            varchar(255)          NOT NULL,
    nip                varchar(255)          NOT NULL,
    rekin_id           varchar(255)          NOT NULL,
    rekin              varchar(255)          NOT NULL,
    target_id          varchar(255)          NOT NULL,
    target             text                  NOT NULL,
    realisasi          integer               NOT NULL,
    satuan             varchar(255)          NOT NULL,
    bulan              varchar(20)           NOT NULL,
    tahun              varchar(10)           NOT NULL,
    jenis_realisasi    varchar(255)          NOT NULL,
    kode_opd           varchar(255)          NULL,
    status             varchar(255)          NOT NULL,
    created_by         varchar(255)          NULL,
    last_modified_by   varchar(255)          NULL,
    created_date       timestamp             NULL,
    last_modified_date timestamp             NULL,
    version            integer               NOT NULL
);
