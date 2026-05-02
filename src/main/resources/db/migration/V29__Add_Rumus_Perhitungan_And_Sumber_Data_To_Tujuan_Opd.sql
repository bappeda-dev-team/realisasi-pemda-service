ALTER TABLE tujuan_opd ADD COLUMN rumus_perhitungan TEXT;
ALTER TABLE tujuan_opd ADD COLUMN sumber_data TEXT;

UPDATE tujuan_opd
SET rumus_perhitungan = ''
WHERE rumus_perhitungan IS NULL;

UPDATE tujuan_opd
SET sumber_data = ''
WHERE sumber_data IS NULL;

ALTER TABLE tujuan_opd ALTER COLUMN rumus_perhitungan SET NOT NULL;
ALTER TABLE tujuan_opd ALTER COLUMN sumber_data SET NOT NULL;
