package semillero.ubuntu.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Category;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.exception.CloudinaryException;
import semillero.ubuntu.mapper.MicroentrepreneurshipMapper;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;
import semillero.ubuntu.utils.FileValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MicroentrepreneurshipServiceImpl implements MicroentrepreneurshipService {

    // Inyección de dependencias a través del constructor
    @Autowired
    private semillero.ubuntu.repository.UserRepository UserRepository;

    @Autowired
    private MicroentrepreneurshipMapper microentrepreneurshipMapper;

    @Autowired
    private FileUploadImpl fileUploadService;

    @Autowired
    private FileValidator fileValidator;

    private final MicroentrepreneurshipRepository microentrepreneurshipRepository;

    public MicroentrepreneurshipServiceImpl(MicroentrepreneurshipRepository microentrepreneurshipRepository) {
        this.microentrepreneurshipRepository = microentrepreneurshipRepository;
    }

    // Este método del servicio está anotado con @Transactional, lo que significa que se ejecutará dentro de una transacción gestionada por Spring. Al cargar Microentrepreneurship dentro de este método, la colección images debería cargarse sin lanzar la excepción LazyInitializationException.
    @Transactional
    @Override
    public List<MicroentrepreneurshipDto> findMicroentrepreneurshipsByCategory(Long idCategory) {
        // Retorna microemprendimientos por coincidencia de categoria
        List<Microentrepreneurship> microentrepreneurships = microentrepreneurshipRepository.findMicroentrepreneurshipsByCategory(idCategory);

        // Accede a la colección images de cada microemprendimiento para forzar la carga perezosa
        for (Microentrepreneurship microentrepreneurship : microentrepreneurships) {
            microentrepreneurship.getImages().size();  // Esto forzará la carga de la colección images
        }

        // Convierte la lista de entidades a DTO antes de retornarla
        List<MicroentrepreneurshipDto> microentrepreneurshipDtos = microentrepreneurshipMapper.entityListToDtoList(microentrepreneurships);
        return microentrepreneurshipDtos;
    }

    @Override
    public ResponseEntity<?> createMicroentrepreneurship(MicroentrepreneurshipDto microentrepreneurshipDto){

        //mapea dto a entidad
        Microentrepreneurship microentrepreneurship = microentrepreneurshipMapper.mapToEntity(microentrepreneurshipDto);

        try {
            List<MultipartFile> multipartImages = List.of(microentrepreneurshipDto.getMultipartImages());

            //validate images
            try {
                ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(multipartImages);
                if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                    return fileValidationResponse;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            microentrepreneurship.setImages(fileUploadService.uploadImage(multipartImages));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Persistir la entidad
        microentrepreneurshipRepository.save(microentrepreneurship);
        return ResponseEntity.ok(microentrepreneurship);
    }

    @Override
    public ResponseEntity<?> editMicroentrepreneurship(Long id, MicroentrepreneurshipDto microentrepreneurshipDto) {
        // Verificar si el microemprendimiento existe
        Optional<Microentrepreneurship> existingMicroentrepreneurshipOptional = microentrepreneurshipRepository.findById(id);

        if (existingMicroentrepreneurshipOptional.isPresent()) {
            //setear los que viene del dto a existingMicroentrepreneurship
            Microentrepreneurship existingMicroentrepreneurship = existingMicroentrepreneurshipOptional.get();
            existingMicroentrepreneurship.setName(microentrepreneurshipDto.getName());
            existingMicroentrepreneurship.setCountry(microentrepreneurshipDto.getCountry());
            existingMicroentrepreneurship.setProvince(microentrepreneurshipDto.getProvince());
            existingMicroentrepreneurship.setCity(microentrepreneurshipDto.getCity());

            //paso la categoría de dto a entidad de categoria
            Category category = new Category();
            category.setName(microentrepreneurshipDto.getCategory().getName());
            category.setId(microentrepreneurshipDto.getCategory().getId());
            existingMicroentrepreneurship.setCategory(category);

            existingMicroentrepreneurship.setSubCategory(microentrepreneurshipDto.getSubCategory());
            existingMicroentrepreneurship.setDescription(microentrepreneurshipDto.getDescription());
            existingMicroentrepreneurship.setMoreInfo(microentrepreneurshipDto.getMoreInfo());

            try {
                List<MultipartFile> newImages = List.of(microentrepreneurshipDto.getMultipartImages());
                List<String> existingImages = existingMicroentrepreneurship.getImages();

                if (!newImages.isEmpty() && !newImages.get(0).isEmpty()) {
                    //todo: verificar si son imagenes nuevas
                    try {
                        ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(newImages);
                        if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                            return fileValidationResponse;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    if(existingImages != null){
                        existingMicroentrepreneurship.setImages(fileUploadService.updateImage(existingImages, newImages));
                    }else{
                        existingMicroentrepreneurship.setImages(fileUploadService.uploadImage(newImages));
                    }
                }else{
                    existingMicroentrepreneurship.setImages(existingImages);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Persistir la entidad actualizada
            microentrepreneurshipRepository.save(existingMicroentrepreneurship);
            return ResponseEntity.ok(existingMicroentrepreneurship);
        } else {
            return ResponseEntity.badRequest().body("El microemprendimiento no existe");
        }
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
    public void manageMicroentrepreneurship(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado"));

        // Gestiona el microemprendimiento
        microentrepreneurship.setIsManaged(true);

        // Guarda la entidad actualizada
        microentrepreneurshipRepository.save(microentrepreneurship);
    }

    // Obtener el microemprendimiento por su id
    @Override
    public ResponseEntity<Object> getMicroentrepreneurshipById(Long id) {
        // Verifica si el microemprendimiento existe
        Optional<Microentrepreneurship> microentrepreneurship = microentrepreneurshipRepository.findById(id);

        if (microentrepreneurship.isPresent()) {
            Microentrepreneurship microentrepreneurshipEntity = microentrepreneurship.get();
            MicroentrepreneurshipDto microentrepreneurshipDto = microentrepreneurshipMapper.mapEntityToDto(microentrepreneurshipEntity);

            return ResponseEntity.ok(microentrepreneurshipDto);
        } else {
            return ResponseEntity.badRequest().body("El microemprendimiento no existe");
        }
    }

    @Override
    public List<Microentrepreneurship> getAllMicroentrepreneurships() {
        // Retorna todos los microemprendimientos
        return microentrepreneurshipRepository.findAllMicroentrepreneurshipsActive();
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

    @Override
    public  Object[][] countMicroentrepreneurshipsByCategories() {
        // Retorna la cantidad de microemprendimientos por categorias
        return microentrepreneurshipRepository.countMicroentrepreneurshipsByCategory();
    }

    @Override
    public List<Microentrepreneurship> findMicroentrepreneurshipsByName(String name) {
        // Retorna microemprendimientos por coincidencia de nombre
        return microentrepreneurshipRepository.findMicroentrepreneurshipsByName(name);
    }

}

