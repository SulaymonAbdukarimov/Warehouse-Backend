package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Input;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InputRepository  extends JpaRepository<Input,Integer> {
@Query(value = "SELECT * FROM input WHERE code ~ '^[0-9]+$' ORDER BY CAST(code AS INTEGER) DESC LIMIT 1", nativeQuery = true)

Optional<Input> findTopByCodeAsNumberDesc();

}

