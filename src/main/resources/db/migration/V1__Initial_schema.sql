CREATE TABLE tujuans
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    tujuan_id          varchar(255)          NOT NULL,
    tujuan             varchar(255)          NOT NULL,
    target             double precision      NOT NULL,
    realisasi          double precision      NOT NULL,
    satuan             varchar(255)          NOT NULL,
    tahun              varchar(10)           NOT NULL,
    jenis_realisasi    varchar(255)          NOT NULL,
    status             varchar(255)          NOT NULL,
    created_date       timestamp             NOT NULL,
    last_modified_date timestamp             NOT NULL,
    version            integer               NOT NULL
);

CREATE TABLE sasarans
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    sasaran_id         varchar(255)          NOT NULL,
    sasaran            varchar(255)          NOT NULL,
    indikator_id       varchar(255)          NOT NULL,
    indikator          varchar(255)          NOT NULL,
    target             double precision      NOT NULL,
    realisasi          double precision      NOT NULL,
    satuan             varchar(255)          NOT NULL,
    tahun              varchar(10)           NOT NULL,
    jenis_realisasi    varchar(255)          NOT NULL,
    status             varchar(255)          NOT NULL,
    created_date       timestamp             NOT NULL,
    last_modified_date timestamp             NOT NULL,
    version            integer               NOT NULL
);
