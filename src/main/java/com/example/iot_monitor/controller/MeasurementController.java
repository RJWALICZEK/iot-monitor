package com.example.iot_monitor.controller;

import com.example.iot_monitor.entity.Measurement;
import com.example.iot_monitor.service.MeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/measurements")
@CrossOrigin(origins = "*")  //for tests
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Measurement> addMeasurement (@RequestBody Map<String, Object> payload) {

        if(!payload.containsKey("temperature") || !payload.containsKey("humidity")) {
            return ResponseEntity.badRequest().build();
        }
        Double temperature = Double.valueOf(payload.get("temperature").toString());
        Double humidity = Double.valueOf(payload.get("humidity").toString());
        String device = (String) payload.getOrDefault("device", "ESP32_DevKit");
        String location = (String) payload.getOrDefault("location", "mo location available");

        Measurement measurement = new Measurement(
                Instant.now(),
                temperature,
                humidity,
                device,
                location
        );

        Measurement saved = service.saveMeasurement(measurement);

        return ResponseEntity.ok(saved);
    }

    // enpoint for web/android tests
    @GetMapping("/latest")
    public ResponseEntity<List<Measurement>> getLatest() {
        return ResponseEntity.ok(service.getLastMeasurement(50));
    }

    @GetMapping("/last-hour")
    public ResponseEntity<List<Measurement>> getLastHour() {
        return ResponseEntity.ok(service.getMeasurementFromLastHour());
    }
}
