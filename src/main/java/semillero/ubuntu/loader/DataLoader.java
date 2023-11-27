package semillero.ubuntu.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import semillero.ubuntu.entities.Country;
import semillero.ubuntu.entities.Province;
import semillero.ubuntu.repository.CountryRepository;
import semillero.ubuntu.repository.ProvinceRepository;

public class DataLoader implements CommandLineRunner {
    final CountryRepository countryRepository;
    final ProvinceRepository provinceRepository;
    @Autowired
    public DataLoader(CountryRepository countryRepository, ProvinceRepository provinceRepository) {
        this.countryRepository = countryRepository;
        this.provinceRepository = provinceRepository;
    }

    @Override
    public void run(String... args){
        Country argentina = new Country();
        argentina.setName("Argentina");
        countryRepository.save(argentina);

        Province buenosAires = new Province();
        buenosAires.setName("Buenos Aires");
        buenosAires.setCountry(argentina);
        provinceRepository.save(buenosAires);
    }
}
