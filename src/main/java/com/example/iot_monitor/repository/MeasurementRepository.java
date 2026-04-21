package com.example.iot_monitor.repository;

import com.example.iot_monitor.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    //get last 50 measurements
    List<Measurement> findAllByOrderByTsDesc(Pageable pageable);

    //get measurements from last hour
    List<Measurement> findByTsAfter(Instant ts);
}
