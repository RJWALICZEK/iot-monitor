package com.example.iot_monitor.service;

import com.example.iot_monitor.entity.Measurement;
import com.example.iot_monitor.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return repository.findTop50ByOrderByTimestampDesc();
    }

    public List<Measurement> getMeasurementFromLastHour() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return repository.findByTimestampAfter(oneHourAgo);
    }
}
