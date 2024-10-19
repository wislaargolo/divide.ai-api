package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryDTO) {
        System.out.println("antes do service" + categoryDTO);
        CategoryResponseDTO newCategory = categoryService.saveCategory(categoryDTO);
        System.out.println("depois do service" + newCategory);
        ApiResponseDTO<CategoryResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Category created successfully.",
                newCategory,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();

        ApiResponseDTO<List<CategoryResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Categories retrieved successfully.",
                categories,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getCategoriesByUserId(@PathVariable Long userId) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByUserId(userId);

        ApiResponseDTO<List<CategoryResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Categories retrieved successfully for user with ID: " + userId,
                categories,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getCategoriesByName(
            @PathVariable String name) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesBySubstring(name);

        ApiResponseDTO<List<CategoryResponseDTO>> response = new ApiResponseDTO<>(
                true,
                "Categories retrieved successfully.",
                categories,
                null
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO categoryDetails) {

        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, categoryDetails);

         ApiResponseDTO<CategoryResponseDTO> response = new ApiResponseDTO<>(
                 true,
                 "Category updated successfully.",
                 updatedCategory,
                 null
         );
         return ResponseEntity.ok(response);

//                .orElseGet(() -> {
//                    ApiResponseDTO<CategoryDTO> response = new ApiResponseDTO<>(
//                            false,
//                            "Error: Category not found.",
//                            null,
//                            null
//                    );
//                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        ApiResponseDTO<Void> response = new ApiResponseDTO<>(
                true,
                "Category deleted successfully",
                null,
                null
        );

        return ResponseEntity.ok(response);
    }
}
