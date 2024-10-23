package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO saveCategory(CategoryRequestDTO category);

    List<CategoryResponseDTO> getCategoriesBySubstring(String name);

    List<CategoryResponseDTO> getAllCategories();

    List<CategoryResponseDTO> getCategoriesByUserId(Long userId);

    List<CategoryResponseDTO> getCategoriesByUserType(Long userId, boolean type);

    CategoryResponseDTO getCategoryById(Long id);

    Category getCategoryByIdIfExists(Long id);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDetails);

    List<CategoryResponseDTO> getCategoryByName(String name);

    void deleteCategory(Long id);
}
