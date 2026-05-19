package com.example.iot_monitor.repository;

import com.example.iot_monitor.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

/**
 * Repozytorium JPA dla encji Measurement.
 *
 * Odpowiada za komunikację z bazą danych (CRUD + zapytania customowe).
 *
 * Wykorzystuje Spring Data JPA, dzięki czemu:
 * - nie trzeba pisać zapytań SQL ręcznie
 * - metody są generowane automatycznie na podstawie nazw
 *
 * Tabela: measurements
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    /**
     * 📊 Pobranie ostatnich 50 pomiarów
     *
     * Zwraca dane posortowane malejąco po czasie (ts DESC)
     * Czyli: najnowsze rekordy jako pierwsze
     */
    List<Measurement> findTop50ByOrderByTsDesc();

    /**
     * ⏱ Pobranie pomiarów od określonego momentu czasu
     *
     * Używane np. do:
     * - danych z ostatniej godziny
     * - danych z ostatnich 24h
     *
     * Parametr:
     * @param ts – moment graniczny (Instant)
     */
    List<Measurement> findByTsAfterOrderByTsDesc(Instant ts);

    /**
     * 📌 Pobranie najnowszego pomiaru (1 rekord)
     *
     * Używane do endpointu "current"
     */
    Measurement findTopByOrderByTsDesc();

    /**
     * 🗄 Pobranie wszystkich pomiarów z bazy
     *
     * Dane są sortowane malejąco (najnowsze pierwsze)
     */
    List<Measurement> findAllByOrderByTsDesc();
}