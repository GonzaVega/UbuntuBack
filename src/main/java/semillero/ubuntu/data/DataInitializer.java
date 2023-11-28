package semillero.ubuntu.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.service.contract.CategoryService;

import java.util.HashMap;
import java.util.Map;

@Service
// Esta clase se ejecuta al iniciar la aplicación y sirve
// Es util para inicializar datos en la base de datos.
public class DataInitializer implements ApplicationRunner {

    // Inyección de dependencias a través del constructor
    private final CategoryService categoryService;

    public DataInitializer(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        // Map para almacenar los datos de las categorías
        Map<String, Category> categories = new HashMap<>();

        // Crea las categorías
        Category category1 = new Category();
        category1.setName("Economía social/Desarrollo local/ Inclusión financiera");
        categories.put(category1.getName(), category1);

        Category category2 = new Category();
        category2.setName("Agroecología/Orgánicos/Alimentación saludable");
        categories.put(category2.getName(), category2);

        Category category3 = new Category();
        category3.setName("Conservación/Regeneración/Servicios ecosistémicos");
        categories.put(category3.getName(), category3);

        Category category4 = new Category();
        category4.setName("Empresas/Organismos de impacto/Economía circular");
        categories.put(category4.getName(), category4);

        // Guarda las categorías en la base de datos
        for (Category category : categories.values()) {
            // Si la categoría no existe, la crea
            if (categoryService.getCategoryByName(category.getName()) == null) {
                categoryService.createCategory(category);
            }
        }
    }
}
