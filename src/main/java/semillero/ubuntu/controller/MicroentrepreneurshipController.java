package semillero.ubuntu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class MicroentrepreneurshipController {
    private final MicroentrepreneurshipService microentrepreneurshipService;

    @Autowired
    public MicroentrepreneurshipController(MicroentrepreneurshipService microentrepreneurshipService) {
        this.microentrepreneurshipService = microentrepreneurshipService;
    }

    @GetMapping("/microentrepreneurship/{id}")
    public ResponseEntity<Microentrepreneurship> getMicroentrepreneurshipById(@PathVariable Long id) {
        Microentrepreneurship microentrepreneurship = microentrepreneurshipService.getMicroentrepreneurshipById(id);
        return new ResponseEntity<>(microentrepreneurship, HttpStatus.OK);
    }

    @PostMapping("/microentrepreneurship")
    public ResponseEntity<?> createMicroentrepreneurship(@Valid @RequestBody Microentrepreneurship microentrepreneurship, BindingResult result) {
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
            response.put("message", "Microemprendimiento creado con éxito");
            response.put("microentrepreneurship", createdMicroentrepreneurship);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Microentrepreneurship> editMicroentrepreneurship(
            @PathVariable Long id) {
        Microentrepreneurship editedMicroentrepreneurship = microentrepreneurshipService.editMicroentrepreneurship(id);
        return new ResponseEntity<>(editedMicroentrepreneurship, HttpStatus.OK);
    }

}
