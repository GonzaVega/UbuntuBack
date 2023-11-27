package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semillero.ubuntu.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    public Optional<User> findByEmail(String email);
}
