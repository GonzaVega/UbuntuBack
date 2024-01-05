package semillero.ubuntu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semillero.ubuntu.entities.Country;


public interface CountryRepository extends JpaRepository<Country, Long> {
     Country findByName(String countryName);

}
