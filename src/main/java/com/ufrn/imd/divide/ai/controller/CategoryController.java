package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.model.Category;
import com.ufrn.imd.divide.ai.repository.CategoryRepository;
import com.ufrn.imd.divide.ai.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(newCategory);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();

        List<CategoryResponseDTO> categoryResponseDTOs = categories.stream()
                .map(category -> new CategoryResponseDTO(category.getId(), category.getName(), category.getDescription(), category.getColor()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryResponseDTOs);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(@PathVariable String name) {
        List<Category> categories = categoryService.getCategoryByName(name);

        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (categories.size() > 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Category category = categories.get(0);
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getColor()
        );

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody Category categoryDetails) {

        Optional<Category> updatedCategory = categoryService.updateCategory(id, categoryDetails);

        return updatedCategory
                .map(cat -> new CategoryResponseDTO(
                        cat.getId(),
                        cat.getName(),
                        cat.getDescription(),
                        cat.getColor()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.ok().build();
    }
}
