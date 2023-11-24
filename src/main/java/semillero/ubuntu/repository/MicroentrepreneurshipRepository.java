package semillero.ubuntu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.Microentrepreneurship;

@Repository
public interface MicroentrepreneurshipRepository extends JpaRepository<Microentrepreneurship, Long> {
    
}