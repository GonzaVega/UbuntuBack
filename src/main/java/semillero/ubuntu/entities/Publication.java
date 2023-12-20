package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "publication")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @NotBlank(message = "El titulo no debe estar en blanco")
    String title;

    @NotBlank(message = "La descripción no puede estar en blanco")
    @Size(max = 2500, message = "La descripción no puede tener mas de 2500 caracteres")
    String description;

    @Size(min = 1, max = 3, message = "Debe haber entre 1 y 3  imágenes")
    @Column(length = 500)
    private List<String> images;

    //@Column(nullable = false)
    boolean deleted;

    //@Column(nullable = false)
    //Date creationDate;
    private LocalDateTime creationDate = LocalDateTime.now();
    int views = 0;
}
