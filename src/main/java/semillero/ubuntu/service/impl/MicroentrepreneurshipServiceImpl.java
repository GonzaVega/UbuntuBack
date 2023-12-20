package semillero.ubuntu.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Image;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.entities.UserEntity;
import semillero.ubuntu.exception.CloudinaryException;
import semillero.ubuntu.mapper.MicroentrepreneurshipMapper;
import semillero.ubuntu.repository.ImageRepository;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.repository.UserRepository;
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
    private final ImageRepository imageRepository;
    public MicroentrepreneurshipServiceImpl(MicroentrepreneurshipRepository microentrepreneurshipRepository, ImageRepository imageRepository) {
        this.microentrepreneurshipRepository = microentrepreneurshipRepository;
        this.imageRepository = imageRepository;
    }

//    @Override
//    public Microentrepreneurship createMicroentrepreneurship(Microentrepreneurship microentrepreneurship) {
//    return microentrepreneurshipRepository.save(microentrepreneurship);
//    }

    @Override
    public ResponseEntity<?> createMicroentrepreneurship(MicroentrepreneurshipDto microentrepreneurshipDto){

        //mapea dto a entidad
        Microentrepreneurship microentrepreneurship = microentrepreneurshipMapper.mapToEntity(microentrepreneurshipDto);

        try {
            //convert array multipart to list of images multipart
            List<MultipartFile> multipartImages = List.of(microentrepreneurshipDto.getMultipartImages());
            for(MultipartFile file : multipartImages){
                System.out.println(file.getBytes());
            }

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
        existingMicroentrepreneurship.setDescription(microentrepreneurship.getDescription());
        existingMicroentrepreneurship.setMoreInfo(microentrepreneurship.getMoreInfo());
        existingMicroentrepreneurship.setImages(microentrepreneurship.getImages());

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
    public void manageMicroentrepreneurship(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Microemprendimiento no encontrado"));

        // Gestiona el microemprendimiento
        microentrepreneurship.setIsManaged(true);

        // Guarda la entidad actualizada
        microentrepreneurshipRepository.save(microentrepreneurship);
    }

    @Override
    public Microentrepreneurship getMicroentrepreneurshipById(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findByIdWithImages(id)
                .orElseThrow(() -> new EntityNotFoundException("El microemprendimiento no existe"));

        // Retorna el microemprendimiento
        return microentrepreneurship;
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

    @Override
    public List<String> UrlImg(List<MultipartFile> files ) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String imageUrl = uploadImage(file);
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    // Este metodo se encarga de subir la imagen a  cloudinary y retorna la url de la imagen
    @Override
    public String uploadImage(MultipartFile file) {

        // datos de la cuenta de cloudinary
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dkzspm2fj",
                "api_key", "229982374928582",
                "api_secret", "ZM54qomggmRWESmK2QQgui7_WPo"));

        try {
            Map<?, ?> uploadResult= cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            return imageUrl;
        } catch (Exception e) {
            throw new CloudinaryException("Error al subir la imagen");
        }

    }

    @Override
    public boolean deleteImageFromCloudinary(Image image) {
        try {

            // datos de la cuenta de cloudinary
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dkzspm2fj",
                    "api_key", "229982374928582",
                    "api_secret", "ZM54qomggmRWESmK2QQgui7_WPo"));


            // Extraer el identificador de la imagen de la URL
            String publicId = image.getUrl().substring(image.getUrl().lastIndexOf('/') + 1, image.getUrl().lastIndexOf('.'));

            // Eliminar la imagen de Cloudinary
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Si el resultado es "ok", la eliminación fue exitosa
            return result.get("result").equals("ok");
        } catch (Exception e) {
            // Si ocurre una excepción, la eliminación no fue exitosa
            return false;
        }
    }

    @Override
    public void deleteImageFromDatabase(Image image) {
        imageRepository.delete(image);
    }


}

