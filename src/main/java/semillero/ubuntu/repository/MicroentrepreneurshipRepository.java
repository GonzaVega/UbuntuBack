package semillero.ubuntu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.Microentrepreneurship;

import java.util.Optional;

@Repository
public interface MicroentrepreneurshipRepository extends JpaRepository<Microentrepreneurship, Long> {
    // Toca de esta manera  por la lazy loading, 
    @Query("SELECT m FROM Microentrepreneurship m LEFT JOIN FETCH m.images WHERE m.id = :id")
    Optional<Microentrepreneurship> findByIdWithImages(@Param("id") Long id);

}