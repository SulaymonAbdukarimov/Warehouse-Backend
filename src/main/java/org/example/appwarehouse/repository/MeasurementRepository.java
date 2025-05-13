package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository  extends JpaRepository<Measurement, Integer> {
    boolean existsByName(String name);
}
