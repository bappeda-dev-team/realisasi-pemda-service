ALTER TABLE sasaran_opd ADD COLUMN kode_sasaran_opd VARCHAR(255);
ALTER TABLE sasaran_opd ADD COLUMN kode_indikator_sasaran_opd VARCHAR(255);
ALTER TABLE sasaran_opd ADD COLUMN kode_target_sasaran_opd VARCHAR(255);

UPDATE sasaran_opd
SET kode_sasaran_opd = 'KSO-' || id
WHERE kode_sasaran_opd IS NULL;

UPDATE sasaran_opd
SET kode_indikator_sasaran_opd = 'KISO-' || id
WHERE kode_indikator_sasaran_opd IS NULL;

UPDATE sasaran_opd
SET kode_target_sasaran_opd = 'KTSO-' || id
WHERE kode_target_sasaran_opd IS NULL;

ALTER TABLE sasaran_opd ALTER COLUMN kode_sasaran_opd SET NOT NULL;
ALTER TABLE sasaran_opd ALTER COLUMN kode_indikator_sasaran_opd SET NOT NULL;
ALTER TABLE sasaran_opd ALTER COLUMN kode_target_sasaran_opd SET NOT NULL;

ALTER TABLE sasaran_opd ADD CONSTRAINT sasaran_opd_kode_sasaran_opd_unique UNIQUE (kode_sasaran_opd);
ALTER TABLE sasaran_opd ADD CONSTRAINT sasaran_opd_kode_indikator_sasaran_opd_unique UNIQUE (kode_indikator_sasaran_opd);
ALTER TABLE sasaran_opd ADD CONSTRAINT sasaran_opd_kode_target_sasaran_opd_unique UNIQUE (kode_target_sasaran_opd);
