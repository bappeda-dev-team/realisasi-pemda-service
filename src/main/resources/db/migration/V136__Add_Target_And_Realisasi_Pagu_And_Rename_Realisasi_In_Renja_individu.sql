ALTER TABLE realisasi_target_renja_program_individu
    ADD COLUMN target NUMERIC(20, 5) NOT NULL DEFAULT 0;

ALTER TABLE realisasi_target_renja_kegiatan_individu
    ADD COLUMN target NUMERIC(20, 5) NOT NULL DEFAULT 0;

ALTER TABLE realisasi_target_renja_subkegiatan_individu
    ADD COLUMN target_realisasi NUMERIC(20, 5) NOT NULL DEFAULT 0;
ALTER TABLE realisasi_target_renja_subkegiatan_individu
    ADD COLUMN target_pagu NUMERIC(20, 5) NOT NULL DEFAULT 0;
ALTER TABLE realisasi_target_renja_subkegiatan_individu
    RENAME COLUMN realisasi TO realisasi_target;
ALTER TABLE realisasi_target_renja_subkegiatan_individu
    ADD COLUMN realisasi_pagu NUMERIC(20, 5) NOT NULL DEFAULT 0;
