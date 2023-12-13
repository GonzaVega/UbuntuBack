package semillero.ubuntu.service.contract;

import semillero.ubuntu.dto.CategoryDto;
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
    List<CategoryDto> getAllCategories();

    /*
     *
     * Obtiene una categoría por su nombre.
     * @param name El nombre de la categoría a buscar.
     * @return Un objeto Category que representa la categoría buscada.
     *
     */
    Category getCategoryByName(String name);

    Category findCategoryById(Long id);




}
