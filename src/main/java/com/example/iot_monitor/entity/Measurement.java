package com.example.iot_monitor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "measurements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ts", nullable = false)
    private Instant ts;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "humidity", nullable = false)
    private Double humidity;

    @Column(name = "device", length = 50)
    private String device;

    @Column(name = "location", length = 100)
    private String location;

    public Measurement(Instant ts, Double temperature, Double humidity, String device, String location) {
        this.ts = ts;
        this.temperature = temperature;
        this.humidity = humidity;
        this.device = device;
        this.location = location;
    }
}