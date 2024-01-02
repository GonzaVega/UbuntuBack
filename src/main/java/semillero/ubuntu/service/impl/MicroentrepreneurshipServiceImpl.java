package semillero.ubuntu.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Image;
import semillero.ubuntu.entities.Microentrepreneurship;
import semillero.ubuntu.exception.CloudinaryException;
import semillero.ubuntu.mapper.MicroentrepreneurshipMapper;
import semillero.ubuntu.repository.ImageRepository;
import semillero.ubuntu.repository.MicroentrepreneurshipRepository;
import semillero.ubuntu.service.contract.MicroentrepreneurshipService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MicroentrepreneurshipServiceImpl implements MicroentrepreneurshipService {

    // Inyección de dependencias a través del constructor
    private final MicroentrepreneurshipRepository microentrepreneurshipRepository;
    private final ImageRepository imageRepository;
    private final MicroentrepreneurshipMapper microentrepreneurshipMapper;

    public MicroentrepreneurshipServiceImpl(MicroentrepreneurshipRepository microentrepreneurshipRepository, ImageRepository imageRepository, MicroentrepreneurshipMapper microentrepreneurshipMapper) {
        this.microentrepreneurshipRepository = microentrepreneurshipRepository;
        this.imageRepository = imageRepository;
        this.microentrepreneurshipMapper = microentrepreneurshipMapper;
    }


    @Override
    public MicroentrepreneurshipDto createMicroentrepreneurship(Microentrepreneurship microentrepreneurship) {
        Microentrepreneurship savedMicroentrepreneurship = microentrepreneurshipRepository.save(microentrepreneurship);
        return microentrepreneurshipMapper.entityToDto(savedMicroentrepreneurship);
    }

   @Override
    public MicroentrepreneurshipDto editMicroentrepreneurship(Long id, Microentrepreneurship microentrepreneurship) {

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
        Microentrepreneurship updatedMicroentrepreneurship = microentrepreneurshipRepository.save(existingMicroentrepreneurship);

        // Convierte la entidad a DTO antes de retornarla
        return microentrepreneurshipMapper.entityToDto(updatedMicroentrepreneurship);
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
    public MicroentrepreneurshipDto getMicroentrepreneurshipById(Long id) {
        // Verifica si el microemprendimiento existe
        Microentrepreneurship microentrepreneurship = microentrepreneurshipRepository.findByIdWithImages(id)
                .orElseThrow(() -> new EntityNotFoundException("El microemprendimiento no existe"));

        // Convierte la entidad a DTO antes de retornarla
        return microentrepreneurshipMapper.entityToDto(microentrepreneurship);
    }

    @Override
    public List<MicroentrepreneurshipDto> getAllMicroentrepreneurships() {
        // Retorna todos los microemprendimientos
        List<Microentrepreneurship> microentrepreneurships = microentrepreneurshipRepository.findAllMicroentrepreneurshipsActive();

        // Convierte la lista de entidades a DTO antes de retornarla
        return microentrepreneurshipMapper.entityListToDtoList(microentrepreneurships);
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
    public List<MicroentrepreneurshipDto> findMicroentrepreneurshipsByName(String name) {
        // Retorna microemprendimientos por coincidencia de nombre
        List<Microentrepreneurship> microentrepreneurships = microentrepreneurshipRepository.findMicroentrepreneurshipsByName(name);

        // Convierte la lista de entidades a DTO antes de retornarla
        return microentrepreneurshipMapper.entityListToDtoList(microentrepreneurships);
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
        return microentrepreneurshipMapper.entityListToDtoList(microentrepreneurships);
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

