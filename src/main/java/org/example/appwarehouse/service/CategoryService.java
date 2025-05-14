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
        boolean exists = categoryRepository.existsByName(categoryDto.getName());
        if (exists) {
            return new Result("Bundey categoriya bor",false);
        }

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

    public Result updateCategory(Integer id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()) {
            return new Result("Bundey kategoriya mavjud emas", false);
        }

        Category category = optionalCategory.get();


        category.setName(categoryDto.getName());

        if(categoryDto.getParentCategoryId() != null) {

            if(categoryDto.getParentCategoryId().equals(id)) {
                return new Result("Kategoriya o'zini ota kategoriya qila olmaydi", false);
            }

            Optional<Category> optionalParentCategory = categoryRepository.findById(categoryDto.getParentCategoryId());
            if(!optionalParentCategory.isPresent()) {
                return new Result("Bundey ota kategoriya mavjud emas", false);
            }


            Category parentCategory = optionalParentCategory.get();
            if(wouldCreateCircularReference(category, parentCategory)) {
                return new Result("Bu o'zgarish kategoriyalar orasida aylanma bog'liqlik yaratadi", false);
            }

            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        categoryRepository.save(category);
        return new Result("Muvaffaqiyatli o'zgardi", true);
    }

    private boolean wouldCreateCircularReference(Category category, Category newParent) {
        if(category.getId().equals(newParent.getId())) {
            return true;
        }

        Category ancestor = newParent.getParentCategory();
        while(ancestor != null) {
            if(ancestor.getId().equals(category.getId())) {
                return true;
            }
            ancestor = ancestor.getParentCategory();
        }

        return false;
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

    public Category getCategoryById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()) {
            return new Category();
        }
        return optionalCategory.get();
    }

}
