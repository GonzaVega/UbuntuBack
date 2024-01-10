package semillero.ubuntu.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<?> createPublication(PublicationDto publicationDTO){
        //get the user id from the spring security sesion
        //Authentication
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();

        String email = null;
        UserEntity currentUser2 = null;
        if (authenticationn != null && authenticationn.isAuthenticated()) {
            Object principal = authenticationn.getPrincipal();

            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            }
            if (StringUtils.isBlank(email) || email == null)  {
                return new ResponseEntity<>("No se encontró usuario para asignar a la publicación", HttpStatus.BAD_REQUEST);
            }
            else{
                currentUser2 = UserRepository.findByEmail(email).orElse(null);
            }
        }

        // Mapear DTO a entidad
        Publication publication = publicationMapper.mapDtoToEntity(publicationDTO);
        publication.setUser(currentUser2);

        if (StringUtils.isBlank(publicationDTO.getTitle()) || StringUtils.isBlank(publicationDTO.getDescription()) || "undefined".equals(publicationDTO.getTitle()) || "undefined".equals(publicationDTO.getDescription()))  {
            return new ResponseEntity<>("Titulo y descripción no pueden estar vacios", HttpStatus.BAD_REQUEST);
        }

        try {
            List<MultipartFile> newImages = List.of(publicationDTO.getMultipartImages());                               //convert array multipart to list of images multipart

            //validate images
            try {
                ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(newImages);
                if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                    return fileValidationResponse;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            publication.setImages(fileUploadService.uploadImage(newImages));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Persistir la entidad
        publicationRepository.save(publication);

        return ResponseEntity.ok(publication);
    }

    @Override
    public ResponseEntity<?> updatePublication(PublicationDto publicationDTO, Long id, Authentication authentication){

        String username  = authentication.getName();                                                                    // usuario logueado
        Optional<UserEntity> currentUser = UserRepository.findByEmail(username);

        Optional<Publication> publicationToUpdate = publicationRepository.findById(id);                                 // publicación a actualizar

        // Verificar que la publicación exista
        if (publicationToUpdate.isEmpty()) {
            return ResponseEntity.badRequest().body("The publication does not exists");
        }

        // Actualizar los campos de la publicación
        Publication publicationEntity = publicationToUpdate.get();
        publicationEntity.setTitle(publicationDTO.getTitle());
        publicationEntity.setDescription(publicationDTO.getDescription());
        publicationEntity.setUser(currentUser.get());                                                                   //Si otro usuario administrador cambia la publicación, actualizamos el usuario

        try {
            List<MultipartFile> newImages = List.of(publicationDTO.getMultipartImages());
            List<String> existingImages = publicationToUpdate.get().getImages();

            if (!newImages.isEmpty() && !newImages.get(0).isEmpty()){                                                    // si manda imágenes, mira si son diferentes, las valida, actualiza y elimina de cloudinary las que se cambiaron
                //todo: verificar si son imagenes nuevas
                try {
                    ResponseEntity<?> fileValidationResponse = fileValidator.validateFiles(newImages);                  //validate images
                    if (!fileValidationResponse.getStatusCode().is2xxSuccessful()) {
                        return fileValidationResponse;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                publicationEntity.setImages(fileUploadService.updateImage(existingImages,newImages));

                if(existingImages != null){
                    fileUploadService.updateImage(existingImages,newImages);
                }else{
                    fileUploadService.uploadImage(newImages);
                }

            }else{ // si no manda imágenes, se mantienen las actuales
                publicationEntity.setImages(existingImages);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Persistir la entidad
        publicationRepository.save(publicationEntity);

        return ResponseEntity.ok(publicationEntity);
    }


    @Override
    public List<Publication> getAllPublication(){
        return publicationRepository.findAll();
    }

//    @Override
//    public Publication getPublicationForId(Long id) {
//        return publicationRepository.findById(id).orElse(null);
//    }

    @Override
    public ResponseEntity<Object> getPublication(Long id) {
        Optional<Publication> publication = publicationRepository.findById(id);

        if (publication.isPresent()) {
            Publication publicationEntity = publication.get();
            PublicationDto publicationDto = publicationMapper.mapEntityToDto(publicationEntity);

            return ResponseEntity.ok(publicationDto);
        } else {
            return ResponseEntity.badRequest().body("The publication does not exists");
        }
    }


    // las vistas de la publicacion se incrementan sólo cuando se llama a este endpoint no cuando se hace un get a la publicacion
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
