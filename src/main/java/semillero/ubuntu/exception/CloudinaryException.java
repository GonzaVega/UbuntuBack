package semillero.ubuntu.exception;

//? Se crea una excepción personalizada para manejar los errores de archivos inválidos
public class CloudinaryException extends  RuntimeException {
    public CloudinaryException(String message) {
        super(message);
    }
}
