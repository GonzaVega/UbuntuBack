package semillero.ubuntu.exception;

//? Se crea una excepción personalizada para manejar los errores de archivos inválidos
public class InvalidFilesException extends  RuntimeException {
    public InvalidFilesException(String message) {
        super(message);
    }
}
