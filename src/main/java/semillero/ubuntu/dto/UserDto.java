package semillero.ubuntu.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import semillero.ubuntu.enums.Role;

public class UserDto {

    String userId;
    String name;
    String lastName;
    String email;
    Boolean disabled;
    @Enumerated(EnumType.STRING)
    Role role;
    String phone;
}
