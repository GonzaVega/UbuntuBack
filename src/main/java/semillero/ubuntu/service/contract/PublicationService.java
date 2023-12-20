package semillero.ubuntu.service.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import semillero.ubuntu.dto.PublicationDto;
import semillero.ubuntu.entities.Publication;

import java.util.List;
@Service
public interface PublicationService {
    //Publication createPublication(Publication publication);
    ResponseEntity<?> createPublication(PublicationDto publicationDTO, Authentication authentication);
    ResponseEntity<?> updatePublication(PublicationDto publicationDTO, Long id);

    List<Publication> getAllPublication();

    Publication getPublicationForId(Long id);

    void increaseViews(Long id);
    void changePublicationStatus(Long id);

    List<Publication> getLast10Posts();
    List<Publication> getAllActivePublications();

}
