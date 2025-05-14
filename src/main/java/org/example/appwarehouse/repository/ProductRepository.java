package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository  extends JpaRepository<Product, Integer> {
   boolean existsByNameAndCategoryId(String name,Integer category_id);

   boolean existsByAttachmentId(Integer attachment_id);

   boolean existsByNameAndCategoryIdAndIdNot(String name,Integer category_id,Integer id);

   @Query(value = "SELECT * FROM product WHERE code ~ '^[0-9]+$' ORDER BY CAST(code AS INTEGER) DESC LIMIT 1", nativeQuery = true)
   Optional<Product> findTopByCodeAsNumberDesc();

}
