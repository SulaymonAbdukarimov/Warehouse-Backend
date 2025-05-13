package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    boolean existsByName(String name);
    List<Category> findByParentCategoryIsNull();



}
