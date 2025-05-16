package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Input;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InputRepository  extends JpaRepository<Input,Integer> {
@Query(value = "SELECT * FROM input WHERE code ~ '^[0-9]+$' ORDER BY CAST(code AS INTEGER) DESC LIMIT 1", nativeQuery = true)

Optional<Input> findTopByCodeAsNumberDesc();
    @Query("SELECT i FROM Input i " +
            "JOIN FETCH i.warehouse w " +
            "JOIN FETCH i.supplier s " +
            "JOIN FETCH i.currency c " +
            "LEFT JOIN FETCH i.inputProducts ip " +
            "LEFT JOIN FETCH ip.product p " +
            "LEFT JOIN FETCH p.category cat " +
            "LEFT JOIN FETCH cat.parentCategory pc " +
            "LEFT JOIN FETCH p.measurement m " +
            "LEFT JOIN FETCH p.attachment a")
    List<Input> findAllWithDetails();


    @Query("SELECT i FROM Input i " +
            "JOIN FETCH i.warehouse w " +
            "JOIN FETCH i.supplier s " +
            "JOIN FETCH i.currency c " +
            "LEFT JOIN FETCH i.inputProducts ip " +
            "LEFT JOIN FETCH ip.product p " +
            "LEFT JOIN FETCH p.category cat " +
            "LEFT JOIN FETCH cat.parentCategory pc " +
            "LEFT JOIN FETCH p.measurement m " +
            "LEFT JOIN FETCH p.attachment a " +
            "WHERE i.id = :inputId")
    Optional<Input> findByIdWithDetails(@Param("inputId") Integer inputId);

}

