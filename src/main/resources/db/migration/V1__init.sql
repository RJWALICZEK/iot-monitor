CREATE EXTENSION IF NOT EXISTS timescaledb;

CREATE TABLE IF NOT EXISTS measurements (
id BIGSERIAL,
ts TIMESTAMPTZ NOT NULL,
temperature DOUBLE PRECISION NOT NULL,
humidity DOUBLE PRECISION NOT NULL,
device VARCHAR(50) NOT NULL DEFAULT 'unknown',
location VARCHAR(100) NOT NULL DEFAULT 'unknown',
PRIMARY KEY (id, ts)
);

SELECT create_hypertable('measurements', 'ts', if_not_exists => TRUE);

-- SELECT add_retention_policy('measurements', INTERVAL '30 days');

CREATE INDEX IF NOT EXISTS idx_measurements_timestamp
    ON measurements (ts DESC);

/**
 * MeasurementService – warstwa logiki biznesowej aplikacji IoT Monitor.
 *
 * Odpowiada za:
 * - zapis pomiarów do bazy danych
 * - pobieranie danych w różnych zakresach czasu (ostatnia godzina, 24h, wszystkie dane)
 * - przygotowanie danych do wyświetlania w API i wykresach
 * - agregację danych (np. 1 pomiar na godzinę dla analizy trendów)
 *
 * Klasa pośredniczy między kontrolerem REST a repozytorium bazy danych.
 */