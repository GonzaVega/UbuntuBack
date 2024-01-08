package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByEmail(String email);

    // Definir una consulta personalizada para recuperar correos electrónicos de usuarios con el rol "admin"
    @Query("SELECT u.email FROM UserEntity u WHERE u.role = 'ADMIN'")
    List<String> getAllAdminEmails();
}
