package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Size(min = 3, message = "El nombre debe tener más de 3 caracteres")
    private String name;

    @NotBlank(message = "El país no puede estar en blanco")
    private String country;

    @NotBlank(message = "La provincia no puede estar en blanco")
    @Size(max = 255, message = "La provincia no puede tener más de 255 caracteres")
    @Size(min = 3, message = "La provincia debe tener más de 3 caracteres")
    private String province;

    @NotBlank(message = "La ciudad no puede estar en blanco")
    @Size(max = 255, message = "La ciudad no puede tener más de 255 caracteres")
    @Size(min = 3, message = "La ciudad debe tener más de 3 caracteres")
    private String city;

    @ManyToOne
    @NotNull(message = "La categoría no puede estar en blanco")
    @JoinColumn(name = "category_id") // Nombre de la columna en la tabla que representa la relación con la categoría
    private Category category; // Categoría del microemprendimiento

    @NotBlank(message = "La subcategoría no puede estar en blanco")
    @Size(max = 255, message = "La subcategoría no puede tener más de 255 caracteres")
    private String subCategory;

    @ElementCollection(fetch = FetchType.EAGER)
    // ElementCollection Indica que la lista es una colección de elementos,facilita el manejo de colecciones de tipos básicos o en JPA sin necesidad de crear una entidad separada para la colección.
    // FetchType.EAGER indica que la lista se cargará de forma inmediata cuando se cargue el microemprendimiento, es decir, cuando se haga una consulta a la base de datos, esto es para que se carguen las imágenes
    @Size(max = 3, message = "La lista de imágenes no puede tener más de 3 elementos")
    private List<String> images;

    private Boolean isActive = false; // Inicia con valor false por defecto

    @NotBlank(message = "La descripción no puede estar en blanco")
    @Size(max = 300, message = "La descripción no puede tener más de 300 caracteres")
    private String description;

    // Mas información no es obligatoria
    @Size(max = 300, message = "La información adicional no puede tener más de 300 caracteres")
    private String moreInfo;

    // Getters, setters y constructor generados automáticamente por Lombok

    // ? PrePersist se ejecuta antes de que se persista es decir se cree  el objeto en la base de datos, es |
    @PrePersist
    public void  prePersist() {
        this.setIsActive(false);
    }

}
