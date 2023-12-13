package semillero.ubuntu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import semillero.ubuntu.entities.Publication;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.service.contract.PublicationService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/publication")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;


    @PostMapping("/create")
    public Publication createPublication(
            @RequestBody Publication publication, User user) {
        return publicationService.createPublication(publication, user);
    }

    @GetMapping
    public List<Publication> getAllPublication(){
        return publicationService.getAllPublication();
    }

    @GetMapping("/activas")
    public List<Publication> getPublicationActive(){
        return publicationService.getAllActivePublications();
    }


    @GetMapping("/{id}")
    public Publication getPublicationForId(@PathVariable Long id){
        Publication publication = publicationService.getPublicationForId(id);
        if (publication != null){
            publicationService.increaseViews(id);
        }
        return publicationService.getPublicationForId(id);
    }

    @GetMapping("/ultimas10")
    public List<Publication> getLatest10Posts(){
        return publicationService.getLast10Posts();
    }
}
