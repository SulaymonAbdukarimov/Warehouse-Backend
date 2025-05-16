package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Output;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OutputRepository extends JpaRepository<Output, Integer> {
    @Query(value = "SELECT * FROM output WHERE code ~ '^[0-9]+$' ORDER BY CAST(code AS INTEGER) DESC LIMIT 1", nativeQuery = true)
    Optional<Output> findTopByCodeAsNumberDesc();
}
