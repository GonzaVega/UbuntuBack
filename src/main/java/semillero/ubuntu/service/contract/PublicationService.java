package semillero.ubuntu.service.contract;

import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Publication;
import semillero.ubuntu.entities.User;

import java.util.List;
@Service
public interface PublicationService {
    Publication createPublication(Publication publication, User user);

    List<Publication> getAllPublication();

    Publication getPublicationForId(Long id);

    void increaseViews(Long id);
    void changePublicationStatus(Long id);

    List<Publication> getLast10Posts();
    List<Publication> getAllActivePublications();

}
