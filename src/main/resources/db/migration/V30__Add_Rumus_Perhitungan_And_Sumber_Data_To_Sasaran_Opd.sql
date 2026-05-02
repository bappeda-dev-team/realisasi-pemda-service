ALTER TABLE sasaran_opd ADD COLUMN rumus_perhitungan VARCHAR(255);
ALTER TABLE sasaran_opd ADD COLUMN sumber_data VARCHAR(255);

UPDATE sasaran_opd
SET rumus_perhitungan = ''
WHERE rumus_perhitungan IS NULL;

UPDATE sasaran_opd
SET sumber_data = ''
WHERE sumber_data IS NULL;

ALTER TABLE sasaran_opd ALTER COLUMN rumus_perhitungan SET NOT NULL;
ALTER TABLE sasaran_opd ALTER COLUMN sumber_data SET NOT NULL;
