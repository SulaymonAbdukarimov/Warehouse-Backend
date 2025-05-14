package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Category;
import org.example.appwarehouse.payload.CategoryDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public Result addCategory(@RequestBody CategoryDto categoryDto) {
        Result result = categoryService.addCategory(categoryDto);
        return result;
    }

    @PutMapping("/{id}")
    public Result updateCategory(@PathVariable Integer id, @RequestBody CategoryDto categoryDto) {
        Result result = categoryService.updateCategory(id, categoryDto);
        return result;
    }

    @GetMapping("/list")
    public List<Category> getAllCategories() {
        List<Category>  categoryList = categoryService.getCategories();
        return categoryList;
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return category;
    }

    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable Integer id) {
        Result result = categoryService.deleteCategory(id);
        return result;
    }
}
