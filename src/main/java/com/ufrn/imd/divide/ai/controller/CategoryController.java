package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.CategoryDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Category>> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.saveCategory(category);
        ApiResponseDTO<Category> response = new ApiResponseDTO<>(
                true,
                "Success: Category created successfully.",
                newCategory,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CategoryDTO>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getColor()))
                .collect(Collectors.toList());

        ApiResponseDTO<List<CategoryDTO>> response = new ApiResponseDTO<>(
                true,
                "Success: Categories retrieved successfully.",
                categoryDTOs,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponseDTO<List<CategoryDTO>>> getCategoriesByName(@PathVariable String name) {
        List<Category> categories = categoryService.getCategoriesBySubstring(name);

        if (categories.isEmpty()) {
            ApiResponseDTO<List<CategoryDTO>> response = new ApiResponseDTO<>(
                    false,
                    "Error: No categories found with the given name.",
                    null,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryDTO(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.getColor()
                ))
                .collect(Collectors.toList());

        ApiResponseDTO<List<CategoryDTO>> response = new ApiResponseDTO<>(
                true,
                "Success: Categories retrieved successfully.",
                categoryDTOs,
                null
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody Category categoryDetails) {

        Optional<Category> updatedCategory = categoryService.updateCategory(id, categoryDetails);

        return updatedCategory
                .map(cat -> {
                    CategoryDTO categoryDTO = new CategoryDTO(
                            cat.getId(),
                            cat.getName(),
                            cat.getDescription(),
                            cat.getColor()
                    );

                    ApiResponseDTO<CategoryDTO> response = new ApiResponseDTO<>(
                            true,
                            "Success: Category updated successfully.",
                            categoryDTO,
                            null
                    );

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ApiResponseDTO<CategoryDTO> response = new ApiResponseDTO<>(
                            false,
                            "Error: Category not found.",
                            null,
                            null
                    );
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCategory(@PathVariable Long id) {
        boolean isDeleted = categoryService.deleteCategory(id);

        if (isDeleted) {
            ApiResponseDTO<Void> response = new ApiResponseDTO<>(
                    true,
                    "Success: Category deleted successfully.",
                    null,
                    null
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponseDTO<Void> response = new ApiResponseDTO<>(
                    false,
                    "Error: Category not found.",
                    null,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
