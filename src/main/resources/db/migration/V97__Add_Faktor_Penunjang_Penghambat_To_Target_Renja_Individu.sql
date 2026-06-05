ALTER TABLE target_renja_program_individu ADD COLUMN faktor_penunjang TEXT NOT NULL DEFAULT '';
ALTER TABLE target_renja_program_individu ADD COLUMN faktor_penghambat TEXT NOT NULL DEFAULT '';

ALTER TABLE target_renja_kegiatan_individu ADD COLUMN faktor_penunjang TEXT NOT NULL DEFAULT '';
ALTER TABLE target_renja_kegiatan_individu ADD COLUMN faktor_penghambat TEXT NOT NULL DEFAULT '';

ALTER TABLE target_renja_subkegiatan_individu ADD COLUMN faktor_penunjang TEXT NOT NULL DEFAULT '';
ALTER TABLE target_renja_subkegiatan_individu ADD COLUMN faktor_penghambat TEXT NOT NULL DEFAULT '';
