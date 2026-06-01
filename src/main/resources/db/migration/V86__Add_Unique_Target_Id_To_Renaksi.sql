DELETE FROM renaksi WHERE id NOT IN (
  SELECT MIN(id) FROM renaksi GROUP BY target_id
);

ALTER TABLE renaksi ADD CONSTRAINT renaksi_target_id_key UNIQUE (target_id);
