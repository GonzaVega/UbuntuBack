package semillero.ubuntu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.service.contract.CategoryService;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

import java.util.*;

import static semillero.ubuntu.utils.FileValidator.validateFiles;


@RestController
@RequestMapping("/api/v1/microentrepreneurship/")
public class MicroentrepreneurshipController {
    private final MicroentrepreneurshipService microentrepreneurshipService;
    private final CategoryService categoryService;

    @Autowired
    public MicroentrepreneurshipController(MicroentrepreneurshipService microentrepreneurshipService, CategoryService categoryService) {
        this.microentrepreneurshipService = microentrepreneurshipService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Microentrepreneurship> getMicroentrepreneurshipById(@PathVariable Long id) {
        // No es necesario usar un try cath porque la excepción se está manejando en la clase GlobalExceptionHandler
        Microentrepreneurship microentrepreneurship = microentrepreneurshipService.getMicroentrepreneurshipById(id);
        return new ResponseEntity<>(microentrepreneurship, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> createMicroentrepreneurship(@Valid @RequestBody Microentrepreneurship microentrepreneurship, @RequestParam("files") List<MultipartFile> files , BindingResult result) {
        // @Valid se utiliza para hacer las validaciones definidas en el modelo, si no se utiliza, no se ejecuta la validación
        // BindingResult result se utiliza para capturar los errores de validación

        Map<String, Object> response = new HashMap<>(); // Se crea un HashMap para almacenar la respuesta

        if (result.hasErrors()) { // Si existen errores de validación

            // Se convierten a una lista
            List<String> errors = new ArrayList<>();

            // Se obtiene cada error y se almacena en la lista
            for (FieldError err : result.getFieldErrors()) {
                // Se mapean los errores para que solo se muestre el mensaje de error y  no mas información
                //String s = "El campo '" + err.getField() + "' " + err.getDefaultMessage(); // Una opcion
                String s = err.getDefaultMessage(); // Otra opcion
                errors.add(s);
            }

            response.put("errors", errors); // Se almacenan los errores en el HashMap
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST); // Se retorna el HashMap con los errores y el código de error
        }

        try {
            Microentrepreneurship createdMicroentrepreneurship = microentrepreneurshipService.createMicroentrepreneurship(microentrepreneurship);
            //List<String> imageUrls = microentrepreneurshipService.uploadImages(files, microentrepreneurship);
            response.put("message", "Microemprendimiento creado con éxito");
            response.put("microentrepreneurship", createdMicroentrepreneurship);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
     * estás teniendo problemas al intentar enviar un objeto.
     * Esto es porque form-data se utiliza principalmente para enviar datos no anidados y archivos.
     * Si intentas enviar un objeto complejo o anidado, puede dar lugar a errores o comportamientos inesperados.
     *
     *  ten en cuenta que @RequestBody no funciona con multipart/form-data
     *
     * Una solución común es enviar los datos del objeto Microentrepreneurship como una cadena JSON en un parámetro @RequestParam,
     *  y luego convertir esta cadena JSON de nuevo a un objeto
     * */

    /*
    *
    * @RequestParam("name") String name, @RequestParam("country") String country,
        @RequestParam("province") String province,@RequestParam("city") String city,
        @RequestParam("category") Long categoryId,@RequestParam("subcategory") String subCategory,
        @RequestParam("description") String description,@RequestParam("moreInformation") String moreInformation,
        @RequestParam("files")List<MultipartFile> files) throws Exception
    * */

    @PostMapping("/saveImg")
    public ResponseEntity<?> createMicroentrepreneurshipImg(@RequestParam("microentrepreneurshipJson") String microentrepreneurshipJson, @RequestParam("files") List<MultipartFile> files) throws Exception {

        Map<String, Object> response = new HashMap<>(); // Se crea un HashMap para almacenar la respuesta

        // Validar los archivos de imagen y devuelve un ResponseEntity de acuerdo al resultado de la validación
        ResponseEntity<?> fileValidationResponse = validateFiles(files);

        // Si la validación falla, se retorna el ResponseEntity con el error
        // getStatusCode() obtiene el estado HTTP de la respuesta,
        // is2xxSuccessful() verifica si el estado es 2xx (200, 201, 202, etc)m es decir, si la respuesta es exitosa
        if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
            return fileValidationResponse;
        }


        // Convertir la cadena JSON de nuevo a un objeto Microentrepreneurship
        // ObjectMapper se utiliza para convertir entre objetos Java y representaciones JSON
        ObjectMapper objectMapper = new ObjectMapper(); // Se crea un objeto ObjectMapper
        Microentrepreneurship microentrepreneurship = objectMapper.readValue(microentrepreneurshipJson, Microentrepreneurship.class); // Se convierte la cadena JSON a un objeto Microentrepreneurship

        System.out.println(microentrepreneurship);

        // Crear el microemprendimiento en la base de datos
        Microentrepreneurship createdMicroentrepreneurship = microentrepreneurshipService.createMicroentrepreneurship(microentrepreneurship);

        // Subir las imagenes a cloudinary y obtener las urls
        List<String> imageUrls = microentrepreneurshipService.UrlImg(files);

        createdMicroentrepreneurship.setImages(imageUrls);

        // Guardar el microemprendimiento con las urls de las imagenes
        microentrepreneurshipService.editMicroentrepreneurship(createdMicroentrepreneurship.getId(), createdMicroentrepreneurship);

       response.put("message", "Microemprendimiento creado con éxito");
        response.put("microentrepreneurship", createdMicroentrepreneurship);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


    @PostMapping("/saveprueba")
    public ResponseEntity<?> createMicroentrepreneurshipPrueba( @RequestParam("microentrepreneurshipJson")String prueba, @RequestParam("files") List<MultipartFile> files ) {
        // @Valid se utiliza para hacer las validaciones definidas en el modelo, si no se utiliza, no se ejecuta la validación
        // BindingResult result se utiliza para capturar los errores de validación

        Map<String, Object> response = new HashMap<>(); // Se crea un HashMap para almacenar la respuesta

        return ResponseEntity.ok(prueba);



    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editMicroentrepreneurship(@Valid @RequestBody Microentrepreneurship microentrepreneurship,BindingResult result,@PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {

            List<String> errors = new ArrayList<>();

            for (FieldError err : result.getFieldErrors()) {
                String s = err.getDefaultMessage();
                errors.add(s);
            }

            response.put("errors", errors); // Se almacenan los errores en el HashMap
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST); // Se retorna el HashMap con los errores y el código de error
        }

        try {
            Microentrepreneurship editedMicroentrepreneurship = microentrepreneurshipService.editMicroentrepreneurship(id,microentrepreneurship);
            response.put("message", "Microemprendimiento editado con éxito");
            response.put("microentrepreneurship", editedMicroentrepreneurship);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/{id}/hide")
    public ResponseEntity<Map<String, Object>> hideMicroentrepreneurship(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        microentrepreneurshipService.hideMicroentrepreneurship(id);
        response.put("message", "Microemprendimiento oculto con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PatchMapping("/{id}/manage")
    public ResponseEntity<Map<String, Object>> manageMicroentrepreneurship(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        microentrepreneurshipService.manageMicroentrepreneurship(id);
        response.put("message", "Microemprendimiento gestionado con éxito");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @GetMapping("/all")
    public ResponseEntity<List<Microentrepreneurship>> getAllMicroentrepreneurships() {
        List<Microentrepreneurship> microentrepreneurships = microentrepreneurshipService.getAllMicroentrepreneurships();
        return new ResponseEntity<>(microentrepreneurships, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countMicroentrepreneurships() {
        Long count = microentrepreneurshipService.countMicroentrepreneurships();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countMicroentrepreneurshipsActive() {
        Long count = microentrepreneurshipService.countMicroentrepreneurshipsActive();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count/notactive")
    public ResponseEntity<Long> countMicroentrepreneurshipsNotActive() {
        Long count = microentrepreneurshipService.countMicroentrepreneurshipsNotActive();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("count/categories")
    public ResponseEntity< Object[][] > countMicroentrepreneurshipsByCategories() {
        Object[][] count = microentrepreneurshipService.countMicroentrepreneurshipsByCategories();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity< Map<String,Object> > findMicroentrepreneurshipsByName(@RequestParam(name = "name") String name) {

        // @RequestParam(name = "name")  se utiliza para obtener el valor del parámetro name de la url, parametro de consulta
        // /microentrepreneurship/find?name=ejemplo. Si no se utiliza, no se obtiene el valor del parámetro

        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        List<Microentrepreneurship>  microentrepreneurships = null;

        // Valida que el nombre no esté vacío
        if (name == null || name.isEmpty()) {
            response.put("error", "El nombre no puede estar vacío");
            status = HttpStatus.BAD_REQUEST;
        }
        else {
            microentrepreneurships = microentrepreneurshipService.findMicroentrepreneurshipsByName(name);
            if (microentrepreneurships.isEmpty()) {
                response.put("Message", "No se encontraron microemprendimientos con el nombre: " + name);
                status = HttpStatus.NOT_FOUND;
            }
            else {
                response.put("microentrepreneurships", microentrepreneurships);
                status = HttpStatus.OK;
            }
        }

        return new ResponseEntity<>(response, status);
    }

}
