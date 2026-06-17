CREATE TABLE IF NOT EXISTS realisasi_target_tujuan_opd (
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    kode_opd            VARCHAR(255)          NOT NULL,
    tahun               VARCHAR(255)          NOT NULL,
    bulan               VARCHAR(255)          NOT NULL,
    kode_tujuan_opd     VARCHAR(255)          NOT NULL,
    kode_indikator      VARCHAR(255)          NOT NULL,
    kode_target         VARCHAR(255)          NOT NULL,
    realisasi           NUMERIC(20,5)         NOT NULL,
    jenis_realisasi     VARCHAR(255)          NOT NULL DEFAULT 'NAIK',
    faktor_penunjang    VARCHAR(255)          NOT NULL,
    faktor_penghambat   VARCHAR(255)          NOT NULL,
    created_by          VARCHAR(100)          NOT NULL,
    last_modified_by    VARCHAR(100)          NOT NULL,
    created_date        TIMESTAMP,
    last_modified_date  TIMESTAMP
);

DO $$
BEGIN
    IF to_regclass('public.tujuan_opd') IS NOT NULL
       AND to_regclass('public.indikator_tujuan_opd') IS NOT NULL
       AND to_regclass('public.target_indikator_tujuan_opd') IS NOT NULL THEN
        INSERT INTO realisasi_target_tujuan_opd (
            kode_opd, tahun, bulan, kode_tujuan_opd, kode_indikator, kode_target,
            realisasi, jenis_realisasi, faktor_penunjang, faktor_penghambat,
            created_by, last_modified_by, created_date, last_modified_date
        )
        SELECT
            t.kode_opd,
            t.tahun,
            t.bulan,
            t.kode_tujuan_opd,
            i.kode_indikator,
            tg.kode_target,
            tg.realisasi,
            'NAIK',
            COALESCE(tg.faktor_penunjang, ''),
            COALESCE(tg.faktor_penghambat, ''),
            COALESCE(tg.created_by, t.created_by, 'system'),
            COALESCE(tg.last_modified_by, t.last_modified_by, 'system'),
            COALESCE(tg.created_date, t.created_date, NOW()),
            COALESCE(tg.last_modified_date, t.last_modified_date, NOW())
        FROM tujuan_opd t
        JOIN indikator_tujuan_opd i ON i.tujuan_opd_id = t.id
        JOIN target_indikator_tujuan_opd tg ON tg.indikator_tujuan_id = i.id;

        DROP TABLE IF EXISTS target_indikator_tujuan_opd;
        DROP TABLE IF EXISTS indikator_tujuan_opd;
        DROP TABLE IF EXISTS tujuan_opd;
    END IF;
END $$;
