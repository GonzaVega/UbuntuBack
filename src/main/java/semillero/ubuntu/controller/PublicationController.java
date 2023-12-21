package semillero.ubuntu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.PublicationDto;
import semillero.ubuntu.entities.Publication;
import semillero.ubuntu.service.contract.PublicationService;

import java.util.List;


@RestController
@RequestMapping("${api.url}/publication")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;


//    @PostMapping("/create")
//    public ResponseEntity<?> createPublication(@RequestParam("imagen") MultipartFile multipartImages, @Validated @RequestBody PublicationDto publicationDto, Authentication authentication) {
//        return publicationService.createPublication(multipartImages, publicationDto,authentication);
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createPublication(@RequestParam("multipartImages") MultipartFile[] multipartImages, @RequestParam("title") String title, @RequestParam("description") String description,  Authentication authentication) {
        //build dto from parameters
        PublicationDto publicationDto = new PublicationDto();
        publicationDto.setTitle(title);
        publicationDto.setDescription(description);
        publicationDto.setMultipartImages(multipartImages);

        return publicationService.createPublication(publicationDto,authentication);
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
