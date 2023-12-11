package semillero.ubuntu.service.impl;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.dto.CategoryDto;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.mapper.CategoryMapper;
import semillero.ubuntu.repository.CategoryRepository;
import semillero.ubuntu.service.contract.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    // Inyección de dependencias a través del constructor
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category createCategory(Category category) {

        // Guarda la categoría en la base de datos
        return categoryRepository.save(category);

    }

    @Override
    // get all con CategoryDto
    public List<CategoryDto> getAllCategories() {
        // Si no hay categorías registradas, lanza una excepción
        if (categoryRepository.count() == 0) {
            throw new ResourceNotFoundException("No categories found");
        }

        // Ordenar la lista de categorías por nombre
        //Collections.sort(categories, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        List<Category> categoryEntities = categoryRepository.findAll();


        return categoryMapper.categoryListToCategoryDtoList(categoryEntities);
    }

    @Override
    public Category getCategoryByName(String name) {

        return categoryRepository.findByName(name);

    }


}
