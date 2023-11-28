package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository <Message, Long> {
}
