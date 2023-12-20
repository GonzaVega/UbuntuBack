package semillero.ubuntu.utils;

import jakarta.persistence.Column;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileValidator {

    public static boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static ResponseEntity<?> validateFiles(List<MultipartFile> files) throws Exception {
        Map<String, Object> response = new HashMap<>(); // Se crea un HashMap para almacenar la respuesta

        // Validar que  no esté vacío
        if (files == null || files.isEmpty() || files.get(0).isEmpty()) {
            response.put("error", "Es necesario cargar al menos una imagen");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Validar el tamaño de la lista de archivos
        if (files.size() > 3) {
            response.put("error", "El número máximo de archivos es 3");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Validar que los archivos sean imágenes y que no superen los 3 MB
        for (MultipartFile file : files) {
            if (!isImage(file)) {
                response.put("error", "El archivo " + file.getOriginalFilename() + " no es una imagen");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else if (file.getSize() > 3 * 1024 * 1024) {
                response.put("error", "El tamaño máximo de cada imagen debe ser de 3 MB");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.ok().build();

    }

}
