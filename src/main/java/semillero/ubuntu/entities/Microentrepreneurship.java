package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data // Genera los getters y setters
@NoArgsConstructor // Genera un constructor vacío
@AllArgsConstructor // Genera un constructor con todos los argumentos
@ToString // Genera el método toString
@Table(name = "microemprendimiento")
public class Microentrepreneurship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min =3 , message = "El nombre debe tener más de 3 caracteres")
    private String nombre;

    @NotBlank(message = "El país no puede estar en blanco")
    private String pais;

    @NotBlank(message = "La provincia no puede estar en blanco")
    @Size(max = 255, message = "La provincia no puede tener más de 255 caracteres")
    @Size(min = 3, message = "La provincia debe tener más de 3 caracteres")
    private String provincia;

    @NotBlank(message = "La ciudad no puede estar en blanco")
    @Size(max = 255, message = "La ciudad no puede tener más de 255 caracteres")
    @Size(min = 3, message = "La ciudad debe tener más de 3 caracteres")
    private String ciudad;

    @ManyToOne
    @JoinColumn(name = "categoria_id") // Nombre de la columna en la tabla que representa la relación con la categoría
    private Category categoria; // Categoría del microemprendimiento

    @NotBlank(message = "La subcategoría no puede estar en blanco")
    @Size(max = 255, message = "La subcategoría no puede tener más de 255 caracteres")
    private String subCategoria;

    @ElementCollection // Indica que la lista es una colección de elementos,facilita el manejo de colecciones de tipos básicos o en JPA sin necesidad de crear una entidad separada para la colección.
    @Size(max = 3, message = "La lista de imágenes no puede tener más de 3 elementos")
    private List<String> imagenes;

    private Boolean activo = false; // Inicia con valor false por defecto

    @NotBlank(message = "La descripción no puede estar en blanco")
    @Size(max = 300, message = "La descripción no puede tener más de 300 caracteres")
    private String descripcion;

    // Mas información
    @NotBlank(message = "La información adicional no puede estar en blanco")
    @Size(max = 300, message = "La información adicional no puede tener más de 300 caracteres")
    private String informacion_adicional;

    // Getters, setters y constructor generados automáticamente por Lombok

}
