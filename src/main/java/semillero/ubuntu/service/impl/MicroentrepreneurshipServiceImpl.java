package semillero.ubuntu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

import java.util.List;

@Service
public class MicroentrepreneurshipServiceImpl implements MicroentrepreneurshipService {

    // Inyección de dependencias a través del constructor
    private final MicroentrepreneurshipRepository microentrepreneurshipRepository;

    public MicroentrepreneurshipServiceImpl(MicroentrepreneurshipRepository microentrepreneurshipRepository) {
        this.microentrepreneurshipRepository = microentrepreneurshipRepository;
    }

    @Override
    public Microentrepreneurship createMicroentrepreneurship(Microentrepreneurship microentrepreneurship) {
    return microentrepreneurshipRepository.save(microentrepreneurship);
    }

    @Override
    public Microentrepreneurship editMicroentrepreneurship(Long id,Microentrepreneurship microentrepreneurship) {
        
        // Verifica si el microemprendimiento existe
        Microentrepreneurship existingMicroentrepreneurship = microentrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado con ID: " + id));

        // Actualiza los datos del microemprendimiento existente con los nuevos datos
        existingMicroentrepreneurship.setName(microentrepreneurship.getName());
        existingMicroentrepreneurship.setCountry(microentrepreneurship.getCountry());
        existingMicroentrepreneurship.setProvince(microentrepreneurship.getProvince());
        existingMicroentrepreneurship.setCity(microentrepreneurship.getCity());
        existingMicroentrepreneurship.setCategory(microentrepreneurship.getCategory());
        existingMicroentrepreneurship.setSubCategory(microentrepreneurship.getSubCategory());
        existingMicroentrepreneurship.setImages(microentrepreneurship.getImages());
        existingMicroentrepreneurship.setIsActive(microentrepreneurship.getIsActive());
        existingMicroentrepreneurship.setDescription(microentrepreneurship.getDescription());
        existingMicroentrepreneurship.setMoreInfo(microentrepreneurship.getMoreInfo());

        // Guarda la entidad actualizada
        return microentrepreneurshipRepository.save(existingMicroentrepreneurship);

    }


    @Override
    public void hideMicroentrepreneurship(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado con ID: " + id));

        // Oculta el microemprendimiento
        microentrepreneurship.setIsActive(false);

        // Guarda la entidad actualizada
        microentrepreneurshipRepository.save(microentrepreneurship);
    }

    @Override
    public Microentrepreneurship getMicroentrepreneurshipById(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findByIdWithImages(id)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado con ID: " + id));

        // Retorna el microemprendimiento
        return microentrepreneurship;
    }

    @Override
    public List<Microentrepreneurship> getAllMicroentrepreneurships() {
        // Retorna todos los microemprendimientos
        return microentrepreneurshipRepository.findAll();
    }

    @Override
    public Long countMicroentrepreneurships() {
        // Retorna la cantidad de microemprendimientos
        return microentrepreneurshipRepository.count();
    }

    @Override
    public Long countMicroentrepreneurshipsActive() {
        // Retorna la cantidad de microemprendimientos gestionados
        return microentrepreneurshipRepository.countMicroentrepreneurshipsActive();
    }

    @Override
    public Long countMicroentrepreneurshipsNotActive() {
        // Retorna la cantidad de microemprendimientos no gestionados
        return microentrepreneurshipRepository.countMicroentrepreneurshipsNotActive();
    }



}

