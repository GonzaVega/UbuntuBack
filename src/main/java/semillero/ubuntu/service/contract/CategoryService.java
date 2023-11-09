package semillero.ubuntu.service.contract;

import semillero.ubuntu.entities.Category;

import java.util.List;

public interface CategoryService {

    /*
     *
     * Obtiene una lista de todas las Categorías existentes.
     * @return Una lista de objetos Category que representan todas las categorías disponibles.
     *
     */
    List<Category> getAllCategories();


}
