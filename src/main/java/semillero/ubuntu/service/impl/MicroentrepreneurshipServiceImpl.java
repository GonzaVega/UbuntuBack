package semillero.ubuntu.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public ResponseEntity<?> createMicroentrepreneurship(MicroentrepreneurshipDto microentrepreneurshipDto){

        //mapea dto a entidad
        Microentrepreneurship microentrepreneurship = microentrepreneurshipMapper.mapToEntity(microentrepreneurshipDto);

        try {
            //convert array multipart to list of images multipart
            List<MultipartFile> multipartImages = List.of(microentrepreneurshipDto.getMultipartImages());
//            for(MultipartFile file : multipartImages){
//                System.out.println(file.getBytes());
//            }

            //validate images
            try {
                ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(multipartImages);
                if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                    return fileValidationResponse;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            microentrepreneurship.setImages(fileUploadService.uploadImage(microentrepreneurshipDto.getMultipartImages()));
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
            existingMicroentrepreneurship.setImages(microentrepreneurshipDto.getImages());

            try {
                // Convertir array de imágenes multipart a lista de imágenes multipart
                List<MultipartFile> multipartImages = List.of(microentrepreneurshipDto.getMultipartImages());

                // Validar imágenes
                try {
                    ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(multipartImages);
                    if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                        return fileValidationResponse;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Actualizar imágenes en la entidad
                existingMicroentrepreneurship.setImages(fileUploadService.uploadImage(microentrepreneurshipDto.getMultipartImages()));
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
    

//    @Override
//    public boolean deleteImageFromCloudinary(Image image) {
//        try {
//
//            // datos de la cuenta de cloudinary
//            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                    "cloud_name", "dkzspm2fj",
//                    "api_key", "229982374928582",
//                    "api_secret", "ZM54qomggmRWESmK2QQgui7_WPo"));
//
//
//            // Extraer el identificador de la imagen de la URL
//            String publicId = image.getUrl().substring(image.getUrl().lastIndexOf('/') + 1, image.getUrl().lastIndexOf('.'));
//
//            // Eliminar la imagen de Cloudinary
//            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//
//            // Si el resultado es "ok", la eliminación fue exitosa
//            return result.get("result").equals("ok");
//        } catch (Exception e) {
//            // Si ocurre una excepción, la eliminación no fue exitosa
//            return false;
//        }
//    }
//
//    @Override
//    public void deleteImageFromDatabase(Image image) {
//        imageRepository.delete(image);
//    }


}

