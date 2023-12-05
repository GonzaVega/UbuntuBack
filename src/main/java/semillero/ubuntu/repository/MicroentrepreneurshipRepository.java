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

    // Obtener la cantidad de microemprendimientos gestionados
    @Query("SELECT COUNT(m) FROM Microentrepreneurship m WHERE m.isActive = true")
    Long countMicroentrepreneurshipsActive();

    // Obtener la cantidad de microemprendimientos no gestionados
    @Query("SELECT COUNT(m) FROM Microentrepreneurship m WHERE m.isActive = false")
    Long countMicroentrepreneurshipsNotActive();

    // Obtener  el nombre de las categorias  y  la cantidad de microemprendimientos de todas la categorias
    @Query("SELECT c.name AS name_category, COUNT(m.category.id) AS count FROM Category c LEFT JOIN Microentrepreneurship m ON m.category.id = c.id GROUP BY c.name")
    Object[][] countMicroentrepreneurshipsByCategory();

}