package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Category;
import org.example.appwarehouse.payload.CategoryDto;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Result addCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        if(categoryDto.getParentCategoryId() != null) {
            //TODO: should be check
            Optional<Category> optionalCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if(!optionalCategory.isPresent()) {
                return new Result("Bundey ota kategoriya mavjud emas",false);
            }
            category.setParentCategory(optionalCategory.get());
        }

        categoryRepository.save(category);
        return new Result("Muvaffaqiyatli saqlandi",true);
    };

    public Result updateCategory(Integer id,CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()) {
            return new Result("Bundey kategoriya mavjud emas",false);
        };

        Category category = new Category();
        category.setName(categoryDto.getName());
        if(categoryDto.getParentCategoryId() != null) {
            Optional<Category> optionalCategory2 = categoryRepository.findById(categoryDto.getParentCategoryId());
            if(!optionalCategory2.isPresent()) {
                return new Result("Bundey ota kategoriya mavjud emas",false);
            }
            category.setParentCategory(optionalCategory2.get());
        }
        categoryRepository.save(category);
        return new Result("Muvaffaqiyatli o'zgardi",true);
    }

        public Result deleteCategory(@PathVariable Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()) {
            return new Result("Bundey kategoriya mavjud emas",false);
        }
        categoryRepository.delete(optionalCategory.get());
        return new Result("Muvaffaqiyatli o'chirildi",true);
    }

    public List<Category> getCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

}
