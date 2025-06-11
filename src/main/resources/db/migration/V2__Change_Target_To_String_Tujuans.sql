ALTER TABLE tujuans
    ALTER COLUMN target TYPE text USING target::text;

ALTER TABLE sasarans
    ALTER COLUMN target TYPE text USING target::text;
