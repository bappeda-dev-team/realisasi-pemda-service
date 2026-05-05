ALTER TABLE tujuans ADD COLUMN created_by VARCHAR(255);
ALTER TABLE tujuans ADD COLUMN last_modified_by VARCHAR(255);

ALTER TABLE sasarans ADD COLUMN created_by VARCHAR(255);
ALTER TABLE sasarans ADD COLUMN last_modified_by VARCHAR(255);

ALTER TABLE tujuan_opd ADD COLUMN created_by VARCHAR(255);
ALTER TABLE tujuan_opd ADD COLUMN last_modified_by VARCHAR(255);

ALTER TABLE sasaran_opd ADD COLUMN created_by VARCHAR(255);
ALTER TABLE sasaran_opd ADD COLUMN last_modified_by VARCHAR(255);
