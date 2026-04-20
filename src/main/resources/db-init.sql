-- Włączenie rozszerzenia TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- Zamiana zwykłej tabeli na hypertable (super ważne dla czasu!)
SELECT create_hypertable('measurements', 'timestamp',
                         if_not_exists => TRUE,
                         migrate_data => TRUE);

-- Indeks dla szybszego wyszukiwania
CREATE INDEX IF NOT EXISTS idx_measurements_timestamp
    ON measurements (timestamp DESC);