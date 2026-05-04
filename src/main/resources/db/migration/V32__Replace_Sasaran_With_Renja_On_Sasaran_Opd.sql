ALTER TABLE sasaran_opd ADD COLUMN renja_id VARCHAR(255);
ALTER TABLE sasaran_opd ADD COLUMN renja VARCHAR(255);

UPDATE sasaran_opd SET renja_id = sasaran_id WHERE renja_id IS NULL;
UPDATE sasaran_opd SET renja = sasaran WHERE renja IS NULL;

ALTER TABLE sasaran_opd ALTER COLUMN renja_id SET NOT NULL;
ALTER TABLE sasaran_opd ALTER COLUMN renja SET NOT NULL;

ALTER TABLE sasaran_opd DROP COLUMN sasaran_id;
ALTER TABLE sasaran_opd DROP COLUMN sasaran;
