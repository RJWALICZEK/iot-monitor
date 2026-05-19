package com.example.iot_monitor.service;

import com.example.iot_monitor.entity.Measurement;
import com.example.iot_monitor.repository.MeasurementRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Warstwa serwisowa aplikacji IoT Monitor.
 *
 * Odpowiada za logikę biznesową:
 * - zapis pomiarów
 * - filtrowanie danych po czasie
 * - przygotowanie danych do wykresów i API
 *
 * Oddziela kontroler (REST API) od warstwy bazy danych.
 */
@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    /**
     * Wstrzyknięcie repozytorium (dostęp do bazy danych)
     */
    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    /**
     * 💾 Zapis nowego pomiaru do bazy danych
     */
    public Measurement saveMeasurement(Measurement measurement) {
        return repository.save(measurement);
    }

    /**
     * ⏱ Pobranie pomiarów z ostatniej godziny
     *
     * Logika:
     * - wyliczenie czasu sprzed 1 godziny
     * - pobranie rekordów nowszych niż ten czas
     */
    public List<Measurement> getMeasurementFromLastHour() {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        return repository.findByTsAfterOrderByTsDesc(oneHourAgo);
    }

    /**
     * 📊 Pobranie ostatnich 50 pomiarów
     * Używane np. do dashboardu lub tabeli danych
     */
    public List<Measurement> getLatest() {
        return repository.findTop50ByOrderByTsDesc();
    }

    /**
     * 📌 Pobranie najnowszego pomiaru (1 rekord)
     * Używane np. do "live status"
     */
    public Measurement getLatestOne() {
        return repository.findTopByOrderByTsDesc();
    }

    /**
     * 🗄 Pobranie wszystkich danych z bazy
     * (posortowane od najnowszych)
     */
    public List<Measurement> getAll() {
        return repository.findAllByOrderByTsDesc();
    }

    /**
     * 📈 Pobranie danych z ostatnich 24h (zagregowanych)
     *
     * LOGIKA:
     * 1. Pobiera dane z ostatnich 24h
     * 2. Redukuje je do maksymalnie 1 pomiaru na godzinę
     * 3. Tworzy listę max 24 punktów danych
     * 4. Odwraca kolejność (chronologicznie rosnąco)
     *
     * Zastosowanie:
     * - wykresy trendów
     * - analiza zmian temperatury i wilgotności
     */
    public List<Measurement> getLast24Hour() {

        // czas graniczny (24h wstecz)
        Instant dayAgo = Instant.now().minus(24, ChronoUnit.HOURS);

        // pobranie danych z bazy
        List<Measurement> all =
                repository.findByTsAfterOrderByTsDesc(dayAgo);

        // lista wynikowa (zagregowana)
        List<Measurement> result = new ArrayList<>();

        // ostatnia godzina, która została już dodana
        int lastHour = -1;

        for (Measurement m : all) {

            // wyciągnięcie godziny z timestampu
            int hour = m.getTs()
                    .atZone(java.time.ZoneId.systemDefault())
                    .getHour();

            // dodaj tylko jeden rekord na godzinę
            if (hour != lastHour) {
                result.add(m);
                lastHour = hour;
            }

            // zabezpieczenie: maksymalnie 24 rekordy
            if (result.size() >= 24) {
                break;
            }
        }

        // odwrócenie kolejności (od najstarszego do najnowszego)
        Collections.reverse(result);

        return result;
    }
}