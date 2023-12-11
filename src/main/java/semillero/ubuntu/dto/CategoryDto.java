package semillero.ubuntu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // Genera los getters y setters
@NoArgsConstructor // Genera un constructor vacío
@AllArgsConstructor // Genera un constructor con todos los argumentos
@ToString // Genera el método toString
public class CategoryDto {

    private Long id;
    private String name;

}
