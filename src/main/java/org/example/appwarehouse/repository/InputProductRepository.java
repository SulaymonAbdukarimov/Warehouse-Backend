package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.InputProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InputProductRepository extends JpaRepository<InputProduct, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM InputProduct ip WHERE ip.input.id = :inputId")
    void deleteByInputId(@Param("inputId") Integer inputId);

    List<InputProduct> findByInputId(Integer inputId);
}
