package semillero.ubuntu.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

// TODO: intenté hacerlo con el dto pero no me da este error, Unsupported Media Type debido a que lo envío como @RequestBody

//    @PostMapping("/create")
//    public ResponseEntity<?> createPublication(@Validated @RequestBody PublicationDto publicationDto,
//                                               Authentication authentication) {
//        return publicationService.createPublication(publicationDto,authentication);
//    }
    

    @PostMapping("/create")
    public ResponseEntity<?> createPublication(
            @RequestParam("images") MultipartFile[] multipartImages,
            @RequestParam("title") String title,
            @RequestParam("description") String description
//            ,@RequestParam("email") String email
    ) {

        if ("undefined".equals(multipartImages)){
            return new ResponseEntity<>("El campo de Imágenes no puede estar vacio", HttpStatus.BAD_REQUEST);
        }

        //build dto from parameters
        PublicationDto publicationDto = new PublicationDto();
        publicationDto.setTitle(title);
        publicationDto.setDescription(description);
        publicationDto.setMultipartImages(multipartImages);

        return publicationService.createPublication(publicationDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePublication(
            @RequestParam("multipartImages") MultipartFile[] multipartImages,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @PathVariable Long id,
            Authentication authentication) {
        //build dto from parameters
        PublicationDto publicationDto = new PublicationDto();
        publicationDto.setTitle(title);
        publicationDto.setDescription(description);
        publicationDto.setMultipartImages(multipartImages);

        return publicationService.updatePublication(publicationDto,id,authentication);
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
    public ResponseEntity<Object> getPublicationForId(@PathVariable Long id){
        ResponseEntity<Object> publication = publicationService.getPublication(id);
        if (publication != null){
            publicationService.increaseViews(id);
        }
        return publicationService.getPublication(id);
    }

    @GetMapping("/ultimas10")
    public List<Publication> getLatest10Posts(){
        return publicationService.getLast10Posts();
    }

    //cambiar status de publicacion
    @PutMapping("/change-status/{id}")
    public void changePublicationStatus(@PathVariable Long id) {
        publicationService.changePublicationStatus(id);
    }
}
