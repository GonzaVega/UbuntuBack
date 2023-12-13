package semillero.ubuntu.exception;

//? Esta clase se utiliza para manejar las excepciones que se puedan presentar en la aplicación

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.*;

@ControllerAdvice // Indica que es una clase global para manejar excepciones
public class GlobalExceptionHandler {

    // Se pueden crear métodos para manejar excepciones específicas con @ExceptionHandler

    @ExceptionHandler(EntityNotFoundException.class) // Indica que este método se ejecuta cuando se lanza la excepción EntityNotFoundException
    @ResponseStatus(HttpStatus.NOT_FOUND) // Establece el estado de la respuesta HTTP
    @ResponseBody // Indica que el valor de retorno del método se agrega al cuerpo de la respuesta HTTP
    public Map<String, String> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }



    // Cuando se lanzan excepciones de validación, se puede capturar y devolver los mensajes de error
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        // Captura las excepciones de validación y devuelve los mensajes de error
        Map<String, Object> response = new HashMap<>(); // Se crea un HashMap para almacenar la respuesta
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            errors.add(violation.getMessage());
        }
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Maneja la excepción cuando se excede el tamaño máximo de carga
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        Map<String,String> response = new HashMap<>();

        response.put("alert", "Tamaño máximo de carga excedido. El tamaño máximo permitido para las imagenes  es de 10 MB.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // Manaja la excepción sobre cloudinary
    @ExceptionHandler(CloudinaryException.class)
    public ResponseEntity<?> handleCloudinaryException(CloudinaryException ex) {
        Map<String,String> response = new HashMap<>();

        response.put("alert", "Error al subir las imagenes a cloudinary");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

}
