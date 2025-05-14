package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Input;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InputRepository  extends JpaRepository<Input,Integer> {
    Optional<Input> findTopByOrderByCodeDesc();
}
