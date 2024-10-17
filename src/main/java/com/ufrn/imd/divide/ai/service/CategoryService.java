package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.CategoryDTO;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.CategoryMapper;
import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;


    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO saveCategory(CategoryDTO category) {
        List<Category> existingCategory = categoryRepository.findByName(category.name());

        if (!existingCategory.isEmpty()) {
            throw new Error("Categoria com o nome '" + category.name() + "' já existe.");
        }

        Category c = categoryMapper.toEntity(category);
        return categoryMapper.toDto(categoryRepository.save(c));
    }

    public List<CategoryDTO> getCategoriesBySubstring(String name) {

        return categoryRepository.findByNameContainingIgnoreCase(name).stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category =  categoryRepository.findById(id);
        if ( category.isPresent()){
            return categoryMapper.toDto(category.get());

        }
        throw new ResourceNotFoundException("Category not found");
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDetails) {
        Optional<Category> c =  categoryRepository.findById(id);
        if ( c.isPresent()){
            Category category = c.get();
            category.setName(categoryDetails.name());
            category.setDescription(categoryDetails.description());
            category.setColor(categoryDetails.color());

            return categoryMapper.toDto(categoryRepository.save(category));
        }
        throw new ResourceNotFoundException("Category not updated");
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