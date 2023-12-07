package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository <Message, Long> {

    List<Message> findByMicroentrepreneurshipId(Long microentrepreneurshipId);
}
