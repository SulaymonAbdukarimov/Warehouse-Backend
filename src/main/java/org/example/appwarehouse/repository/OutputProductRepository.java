package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.OutputProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OutputProductRepository extends JpaRepository<OutputProduct, Integer> {
    @Modifying
    @Transactional
    @Query("delete from OutputProduct op where op.output.id = :outputId")
    int deleteByProductId(@Param("outputId") Integer outputId);

    List<OutputProduct> findAllByOutputId(Integer outputId);
}
