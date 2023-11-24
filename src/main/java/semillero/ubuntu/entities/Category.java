package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que la clase es una entidad
@Data // Genera los getters y setters
@NoArgsConstructor // Genera un constructor vacío
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera un valor único para el id
    private Long id;

    @NotNull
    private String nombre;

    // Constructor sin argumentos generado automáticamente por Lombok.
    // Este constructor se usa en la inicialización de datos.

    // Getters y setters generados automáticamente por Lombok.

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }



}
