package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    boolean existsByName(String name);
    List<Category> findByParentCategoryIsNull();
}
