package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository  extends JpaRepository<Product, Integer> {
   boolean existsByNameAndCategoryId(String name,Integer category_id);
   boolean existsByNameAndCategoryIdAndIdNot(String name,Integer category_id,Integer id);
   Optional<Product> findTopByOrderByCodeDesc();

}
