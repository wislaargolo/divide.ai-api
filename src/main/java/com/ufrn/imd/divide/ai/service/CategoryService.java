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
import com.ufrn.imd.divide.ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponseDTO saveCategory(CategoryRequestDTO category) {
        List<Category> existingCategory = categoryRepository.findByName(category.name());

        if (!existingCategory.isEmpty()) {
            throw new BusinessException(
                    "Categoria com o nome '" + category.name() + "' já existe.", HttpStatus.BAD_REQUEST
            );
        }
        User user = userRepository.findById(category.userId())
                .orElseThrow(() -> new Error("Usuário com ID '" + category.userId() + "' não encontrado."));
        Category c = categoryMapper.toEntity(category);
        c.setUser(user);
        return categoryMapper.toDto(categoryRepository.save(c));
    }

    public List<CategoryResponseDTO> getCategoriesBySubstring(String name) {

        List<Category> categories = categoryRepository
                .findByNameContainingIgnoreCase(name);

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found with name: " + name);
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
        Optional<Category> category =  categoryRepository.findById(id);
        if (category.isPresent()){
            return categoryMapper.toDto(category.get());

        }
        throw new ResourceNotFoundException("Category not found");
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDetails) {
        Optional<Category> c =  categoryRepository.findById(id);
        if (c.isPresent()){
            Category category = c.get();
            BeanUtils.copyProperties(categoryDetails, category, AttributeUtils.getNullOrBlankPropertyNames(categoryDetails));
            return categoryMapper.toDto(categoryRepository.save(category));
        }
        throw new ResourceNotFoundException("Category not updated");
    }

    public List<CategoryResponseDTO> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setActive(false);
        categoryRepository.save(category);
    }
}