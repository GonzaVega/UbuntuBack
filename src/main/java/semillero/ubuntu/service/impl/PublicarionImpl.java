package semillero.ubuntu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semillero.ubuntu.entities.Publication;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.repository.PublicationRepository;
import semillero.ubuntu.service.contract.PublicationService;

import java.util.Date;
import java.util.List;
@Service
public class PublicarionImpl implements PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;

    @Override
    public Publication createPublication(Publication publication, User User){
        publication.setCreationDate(new Date());
        return publicationRepository.save(publication);
    }
    @Override
    public List<Publication> getAllPublication(){
        return publicationRepository.findAll();
    }

    @Override
    public Publication getPublicationForId(Long id) {
        return publicationRepository.findById(id).orElse(null);
    }

    @Override
    public void increaseViews(Long id) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication != null){
            publication.setViews(publication.getViews() + 1);
            publicationRepository.save(publication);
        }
    }


    @Override
    public void changePublicationStatus(Long id) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication != null){
            publication.setDeleted(true);
            publicationRepository.save(publication);
        }
    }

    @Override
    public List<Publication> getLast10Posts(){
        return publicationRepository.findTop10ByOrderByViewsDesc();
    }
    @Override
    public List<Publication> getAllActivePublications() {
        return publicationRepository.findByDeletedFalse();
    }

}
