package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Currency;
import org.example.appwarehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    boolean existsByName(String name);
}
