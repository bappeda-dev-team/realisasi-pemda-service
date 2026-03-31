ALTER TABLE rekin ADD COLUMN indikator_id varchar(255);
ALTER TABLE rekin ADD COLUMN indikator varchar(255);

UPDATE rekin SET indikator_id = '' WHERE indikator_id IS NULL;
UPDATE rekin SET indikator = '' WHERE indikator IS NULL;

ALTER TABLE rekin ALTER COLUMN indikator_id SET NOT NULL;
ALTER TABLE rekin ALTER COLUMN indikator SET NOT NULL;
