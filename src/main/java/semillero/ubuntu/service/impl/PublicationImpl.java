package semillero.ubuntu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semillero.ubuntu.dto.PublicationDto;
import semillero.ubuntu.entities.Publication;
import semillero.ubuntu.entities.UserEntity;
import semillero.ubuntu.mapper.PublicationMapper;
import semillero.ubuntu.repository.PublicationRepository;
import semillero.ubuntu.repository.UserRepository;
import semillero.ubuntu.service.contract.PublicationService;
import semillero.ubuntu.utils.FileValidator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PublicationImpl implements PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PublicationMapper publicationMapper;

    @Autowired
    private FileUploadImpl fileUploadService;

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private FileValidator fileValidator;

    //guarda la publicacion
    @Override
    public ResponseEntity<?> createPublication(PublicationDto publicationDTO, Authentication authentication){
        //get the user id from the spring security sesion
        //Authentication
        //Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        //UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        String username = currentUser.getUsername();
//
//        System.out.println("username: " + username );
        String username  = authentication.getName();
        System.out.println("currentUser: " + authentication.getPrincipal() );
        Optional<UserEntity> currentUser = UserRepository.findByEmail(username);

        // Mapear DTO a entidad
        Publication publication = publicationMapper.mapDtoToEntity(publicationDTO);

        //List<String> processedImages = processImages(publicationDTO.getImages());
        publication.setUser(currentUser.get());

        // Asignar las imágenes procesadas a la entidad
        // first validate the images and then add them to the publication

        try {
            //convert array multipart to list of images multipart
            List<MultipartFile> multipartImages = List.of(publicationDTO.getMultipartImages());
            for(MultipartFile file : multipartImages){
                System.out.println(file.getBytes());
            }

            //validate images
            try {
                ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(multipartImages);
                if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                    return fileValidationResponse;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            publication.setImages(fileUploadService.uploadImage(publicationDTO.getMultipartImages()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Persistir la entidad
        publicationRepository.save(publication);


        return ResponseEntity.ok(publication);
    }

    @Override
    public ResponseEntity<?> updatePublication(PublicationDto publicationDTO, Long id) {
        return null;
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
