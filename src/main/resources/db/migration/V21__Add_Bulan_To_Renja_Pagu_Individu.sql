ALTER TABLE renja_pagu_individu ADD COLUMN bulan VARCHAR(255);
UPDATE renja_pagu_individu SET bulan = '1';
ALTER TABLE renja_pagu_individu ALTER COLUMN bulan SET NOT NULL;