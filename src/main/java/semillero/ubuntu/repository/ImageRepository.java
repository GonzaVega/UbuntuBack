package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semillero.ubuntu.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
}
