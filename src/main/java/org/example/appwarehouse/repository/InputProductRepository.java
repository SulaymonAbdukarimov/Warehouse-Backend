package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.InputProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InputProductRepository extends JpaRepository<InputProduct, Integer> {
    void deleteByInputId(Integer inputId);
}
