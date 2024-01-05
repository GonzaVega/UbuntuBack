package semillero.ubuntu.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @Column(nullable = false)
     String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
     Country country;


}
