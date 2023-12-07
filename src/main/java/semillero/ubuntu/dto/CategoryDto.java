package semillero.ubuntu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // Genera los getters y setters
@NoArgsConstructor // Genera un constructor vacío
@ToString // Genera el método toString
public class CategoryDto {

    private Long id;
    private String name;

}
