package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository  extends JpaRepository<Currency, Integer> {
    boolean existsByName(String name);
}
