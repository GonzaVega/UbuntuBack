package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.Publication;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findTop10ByOrderByViewsDesc();
    List<Publication> findByDeletedFalse();
}
