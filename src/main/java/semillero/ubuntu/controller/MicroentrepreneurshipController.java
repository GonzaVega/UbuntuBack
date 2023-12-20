package semillero.ubuntu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.entities.Image;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.service.contract.CategoryService;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

import java.util.*;
import java.util.stream.Collectors;

import static semillero.ubuntu.utils.FileValidator.validateFiles;


@RestController
@RequestMapping("/api/v1/microentrepreneurship")
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


    /*
     * Se está teniendo problemas al intentar enviar un objeto.
     * Esto es porque form-data se utiliza principalmente para enviar datos no anidados y archivos.
     * Si intentas enviar un objeto complejo o anidado, puede dar lugar a errores o comportamientos inesperados.
     * @RequestBody Microentrepreneurship microentrepreneurship
     *
     * ten en cuenta que @RequestBody no funciona con multipart/form-data
     *
     * Una solución común es enviar los datos del objeto Microentrepreneurship como una cadena JSON en un parámetro @RequestParam,
     * y luego convertir esta cadena JSON de nuevo a un objeto
     * */

    /*
    *
    * @RequestParam("name") String name, @RequestParam("country") String country,
        @RequestParam("province") String province,@RequestParam("city") String city,
        @RequestParam("category") Long categoryId,@RequestParam("subcategory") String subCategory,
        @RequestParam("description") String description,@RequestParam("moreInformation") String moreInformation,
        @RequestParam("files")List<MultipartFile> files) throws Exception
    * */

    @PostMapping("/save")
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

        // Crear el microemprendimiento en la base de datos
        Microentrepreneurship createdMicroentrepreneurship = microentrepreneurshipService.createMicroentrepreneurship(microentrepreneurship);

        // Guardar las imagenes
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = microentrepreneurshipService.uploadImage(file);
            Image image = new Image();
            image.setUrl(url);
            image.setName(file.getOriginalFilename());
            image.setMicroentrepreneurship(createdMicroentrepreneurship);
            images.add(image);
        }

        createdMicroentrepreneurship.setImages(images);

        // Guardar el microemprendimiento con las urls de las imagenes
        microentrepreneurshipService.editMicroentrepreneurship(createdMicroentrepreneurship.getId(), createdMicroentrepreneurship);

       response.put("message", "Microemprendimiento creado con éxito");
        response.put("microentrepreneurship", createdMicroentrepreneurship);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editMicroentrepreneurship(@PathVariable Long id,  @RequestParam("microentrepreneurshipJson") String microentrepreneurshipJson, @RequestParam("files") List<MultipartFile> files) throws JsonProcessingException {

        Map<String, Object> response = new HashMap<>();

        // Convertir la cadena JSON de nuevo a un objeto Microentrepreneurship
        ObjectMapper objectMapper = new ObjectMapper();
        Microentrepreneurship microentrepreneurship = objectMapper.readValue(microentrepreneurshipJson, Microentrepreneurship.class);

        try {
            // Obtener el microemprendimiento existente
            Microentrepreneurship existingMicroentrepreneurship = microentrepreneurshipService.getMicroentrepreneurshipById(id);

            // Si las imágenes no están vacías se suben
            if (!files.isEmpty() && !files.get(0).isEmpty()){

                // Obtener los nombres de los archivos de las imágenes del JSON
                List<String> jsonImageNames = microentrepreneurship.getImages().stream()
                        .map(Image::getName)
                        .collect(Collectors.toList());

                // Obtener las imágenes del microemprendimiento existente
                List<Image> existingImages = existingMicroentrepreneurship.getImages();

                // Elimina las imágenes que no están en el JSON (por el nombre) para agregar las nuevas imágenes
                List<Image> newImages = existingImages.stream()
                        .filter(i -> jsonImageNames.contains(i.getName())) // Filtra las imágenes cuando el nombre de la imagen existente está en la lista de nombres de imágenes del JSON
                        .collect(Collectors.toList());

                for (MultipartFile file : files) {
                    String imageName = file.getOriginalFilename();
                    // Si la imagen no existe en el JSON (no está en la lista de nombres de imágenes del JSON) subirla y agregarla a la lista de nuevas imágenes
                    if (!jsonImageNames.contains(imageName)) {
                        // La imagen es nueva, subirla y agregarla a la lista de nuevas imágenes
                        String imageUrl = microentrepreneurshipService.uploadImage(file);
                        Image image = new Image();
                        image.setUrl(imageUrl);
                        image.setName(imageName);
                        image.setMicroentrepreneurship(existingMicroentrepreneurship);
                        newImages.add(image);
                    }
                    else{
                        // La imagen ya existe, agregarla a la lista de nuevas imágenes
                        Image image = microentrepreneurship.getImages().stream()
                                .filter(i -> i.getName().equals(imageName))
                                .findFirst()
                                .orElse(null);
                        newImages.add(image);

                    }
                    // Agregar las nuevas imágenes a la lista existente de imágenes
                    microentrepreneurship.getImages().addAll(newImages);
                }

                // Reemplazar las imágenes existentes con las nuevas imágenes
                microentrepreneurship.setImages(newImages);
            }

            else {
                // Si las imágenes están vacías, se mantienen las imágenes existentes
                microentrepreneurship.setImages(existingMicroentrepreneurship.getImages());
            }

            // Editar el microemprendimiento
            Microentrepreneurship editedMicroentrepreneurship = microentrepreneurshipService.editMicroentrepreneurship(existingMicroentrepreneurship.getId(), microentrepreneurship);

            response.put("message", "Microemprendimiento editado con éxito");
            response.put("microentrepreneurship", editedMicroentrepreneurship);
            return new ResponseEntity<>(response, HttpStatus.OK);

            } catch(Exception e){
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

    @Transactional // Se utiliza para que se ejecute la consulta en una transacción,Esto asegurará que la sesión de Hibernate permanezca abierta hasta que se haya completado la operación.
    @GetMapping("/all")
    public ResponseEntity<List<Microentrepreneurship>> getAllMicroentrepreneurships() {
        List<Microentrepreneurship> microentrepreneurships = microentrepreneurshipService.getAllMicroentrepreneurships();
        // Accede a las imágenes de cada microemprendimiento para inicializar la colección de imágenes
        microentrepreneurships.forEach(m -> m.getImages().size()); // Inicializa la colección de imágenes
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
