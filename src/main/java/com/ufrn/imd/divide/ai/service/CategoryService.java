package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        List<Category> existingCategory = categoryRepository.findByName(category.getName());

        if (!existingCategory.isEmpty()) {
            throw new Error("Categoria com o nome '" + category.getName() + "' já existe.");
        }

        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesBySubstring(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> updateCategory(Long id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setDescription(categoryDetails.getDescription());
            category.setColor(categoryDetails.getColor());
            return categoryRepository.save(category);
        });
    }

    public List<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public boolean deleteCategory(Long id) {
        return categoryRepository.findById(id).map(category -> {
            categoryRepository.delete(category);
            return true;
        }).orElse(false);
    }
}