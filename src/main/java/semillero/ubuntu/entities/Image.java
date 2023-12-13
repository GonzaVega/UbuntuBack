package semillero.ubuntu.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String url;

    @NotBlank
    private String name;


    @ManyToOne
    @JoinColumn(name = "microentrepreneurship_id")
    @JsonBackReference // ndica que el campo microentrepreneurship es el lado inverso de la relación y no debe ser serializado para evitar la recursión infinita.
    private Microentrepreneurship microentrepreneurship;
}
