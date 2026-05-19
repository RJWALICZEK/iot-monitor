package com.example.iot_monitor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Encja JPA reprezentująca pojedynczy pomiar z urządzenia IoT.
 *
 * Dane są zapisywane w tabeli:
 * measurements
 *
 * Każdy rekord zawiera:
 * - czas pomiaru (ts)
 * - temperaturę
 * - wilgotność
 * - urządzenie źródłowe
 * - lokalizację urządzenia
 */
@Entity
@Table(name = "measurements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    /**
     * Unikalny identyfikator rekordu (klucz główny)
     * Generowany automatycznie przez bazę danych
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Znacznik czasu pomiaru (timestamp)
     * NOT NULL - każdy pomiar musi mieć czas
     */
    @Column(name = "ts", nullable = false)
    private Instant ts;

    /**
     * Wartość temperatury z czujnika
     * NOT NULL
     */
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    /**
     * Wartość wilgotności z czujnika
     * NOT NULL
     */
    @Column(name = "humidity", nullable = false)
    private Double humidity;

    /**
     * Identyfikator/nazwa urządzenia IoT (np. ESP32)
     * Maksymalna długość: 50 znaków
     */
    @Column(name = "device", length = 50)
    private String device;

    /**
     * Lokalizacja urządzenia (np. pokój, budynek)
     * Maksymalna długość: 100 znaków
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * Konstruktor pełny (bez ID)
     * Używany przy tworzeniu nowych pomiarów przed zapisem do bazy
     */
    public Measurement(Instant ts, Double temperature, Double humidity, String device, String location) {
        this.ts = ts;
        this.temperature = temperature;
        this.humidity = humidity;
        this.device = device;
        this.location = location;
    }
}