package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semillero.ubuntu.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
