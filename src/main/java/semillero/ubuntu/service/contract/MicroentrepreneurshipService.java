package semillero.ubuntu.service.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Microentrepreneurship;

import java.util.List;

public interface MicroentrepreneurshipService {

    ResponseEntity<?> createMicroentrepreneurship(MicroentrepreneurshipDto microentrepreneurshipDto);

    Microentrepreneurship editMicroentrepreneurship(Long id, Microentrepreneurship microentrepreneurship);
    void hideMicroentrepreneurship(Long id);
    // gestionar microemprendimiento
    void manageMicroentrepreneurship(Long id);

    // Obtener microemprendimientos por id
    Microentrepreneurship getMicroentrepreneurshipById(Long id);

    List<Microentrepreneurship> getAllMicroentrepreneurships();

    // Cantidad de microemprendimientos
    Long countMicroentrepreneurships();

    // Cantidad de microemprendimientos gestionados
    Long countMicroentrepreneurshipsActive();

    // Cantidad de microemprendimientos no gestionados
    Long countMicroentrepreneurshipsNotActive();

    // Cantidad de microemprendimientos por categorias
    Object[][] countMicroentrepreneurshipsByCategories();

    // Obtener microemprendimientos por coincidencia de nombre
    List<Microentrepreneurship> findMicroentrepreneurshipsByName(String name);

    // Subir las imagenes de los microemprendimientos a cloudinary
    //void uploadImagesMicroentrepreneurship(Long id, List<String> images);
    List<String> UrlImg(List<MultipartFile> files);

    String uploadImage(MultipartFile file);

//    boolean deleteImageFromCloudinary(Image image);
//
//     void deleteImageFromDatabase(Image image);



    }
