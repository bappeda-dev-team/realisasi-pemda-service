ALTER TABLE realisasi_target_renja_subkegiatan_opd
    ADD COLUMN IF NOT EXISTS realisasi_pagu NUMERIC(20, 5) DEFAULT 0;
