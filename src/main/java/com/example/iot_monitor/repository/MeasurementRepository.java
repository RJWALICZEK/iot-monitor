package com.example.iot_monitor.repository;

import com.example.iot_monitor.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    //get last 50 measurements
    List<Measurement> findTop50ByOrderByTimestampDesc();

    //get measurements from last hour
    List<Measurement> findByTimestampAfter(LocalDateTime timestamp);
}
