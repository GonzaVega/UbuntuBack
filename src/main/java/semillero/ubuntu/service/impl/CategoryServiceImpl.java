package semillero.ubuntu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.repository.CategoryRepository;
import semillero.ubuntu.service.contract.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    // Inyección de dependencias a través del constructor
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {

        // Guarda la categoría en la base de datos
        return categoryRepository.save(category);

    }

    @Override
    public List<Category> getAllCategories() {
        // Si no hay categorías registradas, lanza una excepción
        if (categoryRepository.count() == 0) {
            throw new ResourceNotFoundException("No categories found");
        }

        // Ordenar la lista de categorías por nombre
        //Collections.sort(categories, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        return categoryRepository.findAll();
    }
}
