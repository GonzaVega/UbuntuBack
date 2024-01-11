package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import semillero.ubuntu.enums.Management;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "MessageId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    @Column(name = "FullName", nullable = false, length = 60)
    @Size(min = 3, max = 60, message = "El nombre debe tener mínimo 3 caracteres y máximo 60 caracteres")
    private String fullName;

    @NotNull(message = "El email no puede ser nulo")
    @Column(name = "Email", nullable = false)
    @NotBlank(message = "El email debe contener por lo menos un caracter no espaciado")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "El mensaje debe contener por lo menos un caracter no espaciado")
    @Column(name = "Message", nullable = false, length = 300)
    @Size(min = 1, max = 300, message = "El mensaje debe tener mínimo 1 caracter y máximo 300 caracteres")
    private String message;

    @Column(name = "Phone", nullable = false, length = 20)
    @Size(min = 10, max = 20, message = "El telefono debe tener mínimo 10 caracteres y máximo 20 caracteres")
    private String phone;

    @Column(name = "DateOfBirth", nullable = false)
    private LocalDate sentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ManagementStatus", nullable = false)
    Management management;

    @ManyToOne
    @JoinColumn(name = "microentrepreneurship_id")
    private Microentrepreneurship microentrepreneurship;

    @PrePersist
    void preManagement() {
        management = Management.UNMANAGED;
    }
}
