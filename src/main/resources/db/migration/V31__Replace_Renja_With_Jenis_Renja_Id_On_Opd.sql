ALTER TABLE renja_target ADD COLUMN jenis_renja_id VARCHAR(255);
UPDATE renja_target SET jenis_renja_id = renja_id WHERE jenis_renja_id IS NULL;
ALTER TABLE renja_target ALTER COLUMN jenis_renja_id SET NOT NULL;
ALTER TABLE renja_target DROP COLUMN renja_id;
ALTER TABLE renja_target DROP COLUMN renja;

ALTER TABLE renja_pagu ADD COLUMN jenis_renja_id VARCHAR(255);
UPDATE renja_pagu SET jenis_renja_id = renja_id WHERE jenis_renja_id IS NULL;
ALTER TABLE renja_pagu ALTER COLUMN jenis_renja_id SET NOT NULL;
ALTER TABLE renja_pagu DROP COLUMN renja_id;
ALTER TABLE renja_pagu DROP COLUMN renja;
