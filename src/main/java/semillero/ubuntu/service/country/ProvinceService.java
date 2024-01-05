package semillero.ubuntu.service.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Province;
import semillero.ubuntu.repository.ProvinceRepository;

import java.util.List;

@Service
public class ProvinceService {
    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    public Province saveProvince(Province province) {
        return provinceRepository.save(province);
    }

    public List<Province> getProvinceByCountry(String countryName) {
        return provinceRepository.findByCountryName(countryName);
    }
}
