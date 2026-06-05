ALTER TABLE target_renja_program_opd ADD COLUMN faktor_penunjang TEXT NOT NULL DEFAULT '';
ALTER TABLE target_renja_program_opd ADD COLUMN faktor_penghambat TEXT NOT NULL DEFAULT '';

ALTER TABLE target_renja_kegiatan_opd ADD COLUMN faktor_penunjang TEXT NOT NULL DEFAULT '';
ALTER TABLE target_renja_kegiatan_opd ADD COLUMN faktor_penghambat TEXT NOT NULL DEFAULT '';

ALTER TABLE target_renja_subkegiatan_opd ADD COLUMN faktor_penunjang TEXT NOT NULL DEFAULT '';
ALTER TABLE target_renja_subkegiatan_opd ADD COLUMN faktor_penghambat TEXT NOT NULL DEFAULT '';
