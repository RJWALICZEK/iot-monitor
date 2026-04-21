package com.example.iot_monitor.service;

import com.example.iot_monitor.entity.Measurement;
import com.example.iot_monitor.repository.MeasurementRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MeasurementService {
    private final MeasurementRepository repository;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public Measurement saveMeasurement (Measurement measurement) {
        return repository.save(measurement);
    }

    public List<Measurement> getLastMeasurement(int limit) {
        if (limit <= 0) { limit = 10; }
        if (limit >= 1000) {limit = 1000; }
        return repository.findAllByOrderByTimestampDesc(PageRequest.of(0, limit));
    }

    public List<Measurement> getMeasurementFromLastHour() {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        return repository.findByTimestampAfter(oneHourAgo);
    }
}
