package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semillero.ubuntu.entities.Province;

import java.util.List;


public interface ProvinceRepository extends JpaRepository<Province, Long> {
    List<Province> findByCountryName(String CountryName);;
}
