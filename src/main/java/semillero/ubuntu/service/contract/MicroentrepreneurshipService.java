package semillero.ubuntu.service.contract;

import semillero.ubuntu.entities.Microentrepreneurship;

public interface MicroentrepreneurshipService {

    Microentrepreneurship createMicroentrepreneurship(Microentrepreneurship microentrepreneurship);
    Microentrepreneurship editMicroentrepreneurship(Long id);
    void hideMicroentrepreneurship(Long id);
    // Obtener microemprendimientos por id
    Microentrepreneurship getMicroentrepreneurshipById(Long id);

}
