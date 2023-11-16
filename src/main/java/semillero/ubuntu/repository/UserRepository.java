package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semillero.ubuntu.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);
}
