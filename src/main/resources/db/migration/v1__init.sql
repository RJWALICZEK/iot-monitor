CREATE EXTENSION IF NOT EXISTS timescaledb;

CREATE TABLE IF NOT EXISTS measurements (
                                            id BIGSERIAL PRIMARY KEY,
                                            timestamp TIMESTAMPTZ NOT NULL,
                                            temperature DOUBLE PRECISION NOT NULL,
                                            humidity DOUBLE PRECISION NOT NULL,
                                            device VARCHAR(50),
    location VARCHAR(100)
    );

SELECT create_hypertable('measurements', 'timestamp', if_not_exists => TRUE);

CREATE INDEX IF NOT EXISTS idx_measurements_timestamp
    ON measurements (timestamp DESC);