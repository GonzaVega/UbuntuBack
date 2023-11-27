package semillero.ubuntu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semillero.ubuntu.entities.Country;
import semillero.ubuntu.entities.Province;
import semillero.ubuntu.repository.CountryRepository;
import semillero.ubuntu.repository.ProvinceRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;


    @Autowired
    public GeoController(CountryRepository countryRepository, ProvinceRepository provinceRepository, CountryRepository countryRepository1, ProvinceRepository provinceRepository1){
        this.countryRepository = countryRepository1;
        this.provinceRepository = provinceRepository1;
    }

    @GetMapping("/paises")
    public List<Country> getCountry(){
        return countryRepository.findAll();
    }

    @GetMapping("/provincias/{countryName}")
    public List<Province> getProvincesByCountry(@PathVariable String countryName){
        Optional<Country> optionalCountry = Optional.ofNullable(countryRepository.findByName(countryName));

        if (optionalCountry.isPresent()) {
            Country country = optionalCountry.get();
            return provinceRepository.findByCountryName(country.getName());
        } else {
            throw new ResourceNotFoundException("País no encontrado con nombre: " + countryName);
        }

    }
}
