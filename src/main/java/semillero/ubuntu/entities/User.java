package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import semillero.ubuntu.enums.Role;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_control", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Basic
    @Size(min = 2, max = 60)
    @Column(nullable = false, length = 60)
    private String name;

    @Size(min = 2, max = 60)
    @Column(nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private Boolean disabled;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Size(min = 2, max = 30)
    @Column(nullable = false, length = 30)
    private String phone;

    @PrePersist
    void prePersistDisabled() {
        if (disabled == null) {
            disabled = true;
        }
    }
}
