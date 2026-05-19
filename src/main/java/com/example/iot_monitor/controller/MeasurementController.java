package com.example.iot_monitor.controller;

import com.example.iot_monitor.entity.Measurement;
import com.example.iot_monitor.service.MeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Kontroler REST API dla danych pomiarowych z urządzeń IoT.
 *
 * Odpowiada za:
 * - odbiór danych z ESP32 (POST)
 * - udostępnianie danych dla aplikacji web/mobile (GET)
 *
 * Endpoint bazowy:
 * /api/v1/measurements
 */
@RestController
@RequestMapping("/api/v1/measurements")
@CrossOrigin(origins = "*")  // umożliwia dostęp z dowolnego frontendu (testy, aplikacje mobilne)
public class MeasurementController {

    private final MeasurementService service;

    /**
     * Wstrzyknięcie warstwy serwisowej (logika biznesowa)
     */
    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    /**
     * 📥 Dodanie nowego pomiaru (ESP32 / klient HTTP)
     *
     * Endpoint:
     * POST /api/v1/measurements
     *
     * Oczekiwany JSON:
     * {
     *   "temperature": 23.5,
     *   "humidity": 60,
     *   "device": "ESP32",
     *   "location": "Room 1"
     * }
     *
     * Walidacja:
     * - wymagane: temperature, humidity
     * - opcjonalne: device, location
     *
     * Dane są zapisywane z aktualnym timestampem (Instant.now()).
     */
    @PostMapping
    public ResponseEntity<Measurement> addMeasurement (@RequestBody Map<String, Object> payload) {

        // prosta walidacja wejścia
        if(!payload.containsKey("temperature") || !payload.containsKey("humidity")) {
            return ResponseEntity.badRequest().build();
        }

        // konwersja danych z JSON → Double
        Double temperature = Double.valueOf(payload.get("temperature").toString());
        Double humidity = Double.valueOf(payload.get("humidity").toString());

        // dane opcjonalne z fallbackiem
        String device = (String) payload.getOrDefault("device", "ESP32_DevKit");
        String location = (String) payload.getOrDefault("location", "mo location available");

        // utworzenie obiektu encji pomiaru
        Measurement measurement = new Measurement(
                Instant.now(),   // czas zapisu
                temperature,
                humidity,
                device,
                location
        );

        // zapis do bazy przez service
        Measurement saved = service.saveMeasurement(measurement);

        // zwrot zapisanej encji
        return ResponseEntity.ok(saved);
    }

    /**
     * 📊 Ostatnie 50 pomiarów (do wykresów / dashboardu)
     *
     * Endpoint:
     * GET /api/v1/measurements/latest
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Measurement>> getLatest() {
        return ResponseEntity.ok(service.getLatest());
    }

    /**
     * ⏱ Pomiar z ostatniej godziny
     *
     * Endpoint:
     * GET /api/v1/measurements/last-hour
     *
     * Używane np. do wykresów "real-time"
     */
    @GetMapping("/last-hour")
    public ResponseEntity<List<Measurement>> getLastHour() {
        return ResponseEntity.ok(service.getMeasurementFromLastHour());
    }

    /**
     * 📌 Najnowszy dostępny pomiar
     *
     * Endpoint:
     * GET /api/v1/measurements/current
     *
     * Zwraca tylko 1 rekord (ostatni zapis)
     */
    @GetMapping("/current")
    public ResponseEntity<Measurement> getCurrent() {
        return ResponseEntity.ok(service.getLatestOne());
    }

    /**
     * 🗄 Wszystkie dane w systemie
     *
     * Endpoint:
     * GET /api/v1/measurements/all
     *
     * Uwaga: może być kosztowne przy dużej ilości danych
     */
    @GetMapping("/all")
    public ResponseEntity<List<Measurement>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * 📈 Dane zagregowane z ostatnich 24h
     *
     * Endpoint:
     * GET /api/v1/measurements/last-24h
     *
     * Logika:
     * - wybiera dane z ostatnich 24h
     * - redukuje do 1 pomiaru na godzinę
     * - wynik: max 24 punkty danych
     *
     * Idealne do wykresów trendów
     */
    @GetMapping("/last-24h")
    public ResponseEntity<List<Measurement>> getLast24() {
        return ResponseEntity.ok(service.getLast24Hour());
    }
}