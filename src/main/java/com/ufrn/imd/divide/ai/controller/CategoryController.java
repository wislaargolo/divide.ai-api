package com.ufrn.imd.divide.ai.controller;

import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.ApiResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.service.interfaces.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryDTO) {
        CategoryResponseDTO newCategory = categoryService.saveCategory(categoryDTO);
        ApiResponseDTO<CategoryResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Categoria criada com sucesso.",
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
                "Categorias retornadas com sucesso.",
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
                "Categorias recuperadas com sucesso para usuário com ID: " + userId,
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
                "Categorias retornadas com sucesso.",
                categories,
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> getById(
            @PathVariable Long id) {

        ApiResponseDTO<CategoryResponseDTO> response = new ApiResponseDTO<>(
                true,
                "Categoria recuperada com sucesso.",
                categoryService.getCategoryById(id),
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
                 "Categoria atualizada com sucesso.",
                 updatedCategory,
                 null
         );
         return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        ApiResponseDTO<Void> response = new ApiResponseDTO<>(
                true,
                "Categoria removida com sucesso.",
                null,
                null
        );

        return ResponseEntity.ok(response);
    }
}
