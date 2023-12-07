package semillero.ubuntu.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.dto.CategoryDto;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.service.contract.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class CategoryController {

    // Inyección de dependencias a través del constructor
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    // Crear una nueva categoría
    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        Category newCategory = new Category();


        try {
            newCategory = categoryService.createCategory(category);
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {

        List<CategoryDto> categories = categoryService.getAllCategories();

        for (CategoryDto category : categories) {
            System.out.println(category.getName());
        }

        // Si no hay categorías registradas, lanza una excepción
        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Retornar la lista de categorías
        return ResponseEntity.ok(categories);

    }


}
