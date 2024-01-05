package semillero.ubuntu.service.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Microentrepreneurship;

import java.util.List;

public interface MicroentrepreneurshipService {

    ResponseEntity<?> createMicroentrepreneurship(MicroentrepreneurshipDto microentrepreneurshipDto);

    ResponseEntity<?> editMicroentrepreneurship(Long id, MicroentrepreneurshipDto microentrepreneurshipDto);

    void hideMicroentrepreneurship(Long id);
    // gestionar microemprendimiento
    void manageMicroentrepreneurship(Long id);

    // Obtener microemprendimientos por id
    ResponseEntity<?> getMicroentrepreneurshipById(Long id);

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

    List<MicroentrepreneurshipDto> findMicroentrepreneurshipsByCategory(Long id);


//    boolean deleteImageFromCloudinary(Image image);
//
//     void deleteImageFromDatabase(Image image);



    }
