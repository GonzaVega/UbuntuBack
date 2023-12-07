package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
