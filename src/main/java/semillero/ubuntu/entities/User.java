package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import semillero.ubuntu.enums.Role;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_control", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;

    @Basic
    @Size(min = 2, max = 60)
    @Column(nullable = false, length = 60)
    String name;

    @Size(min = 2, max = 60)
    @Column(nullable = false, length = 60)
    String lastName;

    @Column(nullable = false)
    String email;

    Boolean disabled;

    @Enumerated(EnumType.STRING)
    Role role;

    @Size(min = 2, max = 30)
    @Column(nullable = false, length = 30)
    String phone;

    @PrePersist
    void prePersistDisabled() {
        if (disabled == null) {
            disabled = true;
        }
    }
}
