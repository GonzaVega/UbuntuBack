package semillero.ubuntu.service.contract;

import semillero.ubuntu.entities.Category;

import java.util.List;

public interface CategoryService {

    /*
     *
     * Crea una nueva categoría.
     * @param category La categoría a crear.
     * @return La categoría creada.
     *
     */
    Category createCategory(Category category);

    /*
     *
     * Obtiene una lista de todas las Categorías existentes.
     * @return Una lista de objetos Category que representan todas las categorías disponibles.
     *
     */
    List<Category> getAllCategories();




}
