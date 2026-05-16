ALTER TABLE tujuan_opd ADD COLUMN kode_tujuan_opd VARCHAR(255);
ALTER TABLE tujuan_opd ADD COLUMN kode_indikator_tujuan_opd VARCHAR(255);
ALTER TABLE tujuan_opd ADD COLUMN kode_target_tujuan_opd VARCHAR(255);

UPDATE tujuan_opd
SET kode_tujuan_opd = 'KTO-' || id
WHERE kode_tujuan_opd IS NULL;

UPDATE tujuan_opd
SET kode_indikator_tujuan_opd = 'KITO-' || id
WHERE kode_indikator_tujuan_opd IS NULL;

UPDATE tujuan_opd
SET kode_target_tujuan_opd = 'KTTO-' || id
WHERE kode_target_tujuan_opd IS NULL;

ALTER TABLE tujuan_opd ALTER COLUMN kode_tujuan_opd SET NOT NULL;
ALTER TABLE tujuan_opd ALTER COLUMN kode_indikator_tujuan_opd SET NOT NULL;
ALTER TABLE tujuan_opd ALTER COLUMN kode_target_tujuan_opd SET NOT NULL;

ALTER TABLE tujuan_opd ADD CONSTRAINT tujuan_opd_kode_tujuan_opd_unique UNIQUE (kode_tujuan_opd);
ALTER TABLE tujuan_opd ADD CONSTRAINT tujuan_opd_kode_indikator_tujuan_opd_unique UNIQUE (kode_indikator_tujuan_opd);
ALTER TABLE tujuan_opd ADD CONSTRAINT tujuan_opd_kode_target_tujuan_opd_unique UNIQUE (kode_target_tujuan_opd);
