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

@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public Measurement saveMeasurement(Measurement measurement) {
        return repository.save(measurement);
    }

    public List<Measurement> getMeasurementFromLastHour() {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        return repository.findByTsAfterOrderByTsDesc(oneHourAgo);
    }

    public List<Measurement> getLatest() {
        return repository.findTop50ByOrderByTsDesc();
    }

    public Measurement getLatestOne() {
        return repository.findTopByOrderByTsDesc();
    }

    public List<Measurement> getAll() {
        return repository.findAllByOrderByTsDesc();
    }

    public List<Measurement> getLast24Hour() {

        Instant dayAgo = Instant.now().minus(24, ChronoUnit.HOURS);

        List<Measurement> all =
                repository.findByTsAfterOrderByTsDesc(dayAgo);

        List<Measurement> result = new ArrayList<>();

        int lastHour = -1;

        for (Measurement m : all) {

            int hour = m.getTs()
                    .atZone(java.time.ZoneId.systemDefault())
                    .getHour();

            if (hour != lastHour) {
                result.add(m);
                lastHour = hour;
            }

            if (result.size() >= 24) {
                break;
            }
        }

        Collections.reverse(result);

        return result;
    }
}
