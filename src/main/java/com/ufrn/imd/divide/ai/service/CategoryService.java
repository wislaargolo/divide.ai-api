package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.exception.ResourceNotFoundException;
import com.ufrn.imd.divide.ai.mapper.CategoryMapper;
import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.CategoryRepository;
import com.ufrn.imd.divide.ai.util.AttributeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
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

    public CategoryResponseDTO saveCategory(CategoryRequestDTO category) {
        List<Category> existingCategory = categoryRepository.findByName(category.name());

        if (!existingCategory.isEmpty()) {
            throw new BusinessException(
                    "Categoria com o nome '" + category.name() + "' já existe.", HttpStatus.BAD_REQUEST
            );
        }

        Category c = categoryMapper.toEntity(category);
        return categoryMapper.toDto(categoryRepository.save(c));
    }

    public List<CategoryResponseDTO> getCategoriesBySubstring(String name) {

        List<Category> categories = categoryRepository
                .findByNameContainingIgnoreCase(name);

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Não foram encontradas categorias com o nome: " + name);
        }

        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = getCategoryByIdIfExists(id);
        return categoryMapper.toDto(category);
    }

    public Category getCategoryByIdIfExists(Long id) {
        Optional<Category> category =  categoryRepository.findById(id);
        if (category.isPresent()){
            return category.get();

        }
        throw new ResourceNotFoundException("Categoria não encontrada.");
    }


    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDetails) {
        Optional<Category> c =  categoryRepository.findById(id);
        if (c.isPresent()){
            Category category = c.get();
            BeanUtils.copyProperties(categoryDetails, category, AttributeUtils.getNullOrBlankPropertyNames(categoryDetails));
            return categoryMapper.toDto(categoryRepository.save(category));
        }
        throw new ResourceNotFoundException("Categoria não encontrada.");
    }

    public List<CategoryResponseDTO> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        category.setActive(false);
        categoryRepository.save(category);
    }
}