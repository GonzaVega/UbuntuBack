package semillero.ubuntu.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import semillero.ubuntu.enums.Role;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_control", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User implements UserDetails {

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

    public String getFullName(){
        StringBuilder fullName = new StringBuilder();
        fullName.append(name);
        fullName.append(" ");
        fullName.append(lastName);

        return fullName.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
