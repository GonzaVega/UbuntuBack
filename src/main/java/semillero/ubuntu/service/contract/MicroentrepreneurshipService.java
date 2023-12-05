package semillero.ubuntu.service.contract;

import semillero.ubuntu.entities.Microentrepreneurship;

import java.util.List;

public interface MicroentrepreneurshipService {

    Microentrepreneurship createMicroentrepreneurship(Microentrepreneurship microentrepreneurship);
    Microentrepreneurship editMicroentrepreneurship(Long id, Microentrepreneurship microentrepreneurship);
    void hideMicroentrepreneurship(Long id);
    // Obtener microemprendimientos por id
    Microentrepreneurship getMicroentrepreneurshipById(Long id);

    List<Microentrepreneurship> getAllMicroentrepreneurships();

    // Cantidad de microemprendimientos
    Long countMicroentrepreneurships();

}
