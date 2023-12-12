package semillero.ubuntu.exception;

//? Esta clase se utiliza para manejar las excepciones que se puedan presentar en la aplicación

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

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
}
