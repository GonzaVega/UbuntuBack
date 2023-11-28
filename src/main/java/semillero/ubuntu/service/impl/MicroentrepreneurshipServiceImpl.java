package semillero.ubuntu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

@Service
public class MicroentrepreneurshipServiceImpl implements MicroentrepreneurshipService {

    // Inyección de dependencias a través del constructor
    private final MicroentrepreneurshipRepository microentrepreneurshipRepository;

    public MicroentrepreneurshipServiceImpl(MicroentrepreneurshipRepository microentrepreneurshipRepository) {
        this.microentrepreneurshipRepository = microentrepreneurshipRepository;
    }

    @Override
    public Microentrepreneurship createMicroentrepreneurship(Microentrepreneurship microentrepreneurship) {
        microentrepreneurship.setIsActive(false);
    return microentrepreneurshipRepository.save(microentrepreneurship);
    }

    @Override
    public Microentrepreneurship editMicroentrepreneurship(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship existingMicroentrepreneurship = microentrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Microentrepreneurship not found with id: " + id));

        // Actualiza los campos necesarios
        existingMicroentrepreneurship.setName(existingMicroentrepreneurship.getName());
        existingMicroentrepreneurship.setDescription(existingMicroentrepreneurship.getDescription());
        existingMicroentrepreneurship.setCity(existingMicroentrepreneurship.getCity());
        existingMicroentrepreneurship.setCategory(existingMicroentrepreneurship.getCategory());
        existingMicroentrepreneurship.setSubCategory(existingMicroentrepreneurship.getSubCategory());
        existingMicroentrepreneurship.setImages(existingMicroentrepreneurship.getImages());
        existingMicroentrepreneurship.setIsActive(existingMicroentrepreneurship.getIsActive());
        existingMicroentrepreneurship.setMoreInfo(existingMicroentrepreneurship.getMoreInfo());


        // Guarda la entidad actualizada
        return microentrepreneurshipRepository.save(existingMicroentrepreneurship);
    }


    @Override
    public void hideMicroentrepreneurship(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Microentrepreneurship not found with id: " + id));

        // Oculta el microemprendimiento
        microentrepreneurship.setIsActive(false);

        // Guarda la entidad actualizada
        microentrepreneurshipRepository.save(microentrepreneurship);
    }

    @Override
    public Microentrepreneurship getMicroentrepreneurshipById(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findByIdWithImages(id)
                .orElseThrow(() -> new EntityNotFoundException("Microentrepreneurship not found with id: " + id));

        // Retorna el microemprendimiento
        return microentrepreneurship;
    }

}

