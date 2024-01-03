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
    public ResponseEntity<?> updatePublication(PublicationDto publicationDTO, Long id, Authentication authentication){

        // Obtener el usuario logueado
        String username  = authentication.getName();
        Optional<UserEntity> currentUser = UserRepository.findByEmail(username);

        // Obtener la publicación a actualizar
        Optional<Publication> publicationToUpdate = publicationRepository.findById(id);

        // Verificar que la publicación exista
        if (publicationToUpdate.isEmpty()) {
            return ResponseEntity.badRequest().body("The publication does not exists");
        }

        // Actualizar los campos de la publicación
        Publication publicationEntity = publicationToUpdate.get();
        publicationEntity.setTitle(publicationDTO.getTitle());
        publicationEntity.setDescription(publicationDTO.getDescription());
        //Si otro usuario administrador cambia la publicación, actualizamos el usuario
        publicationEntity.setUser(currentUser.get());

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

            publicationEntity.setImages(fileUploadService.updateImage(publicationToUpdate.get().getImages(),publicationDTO.getMultipartImages()));
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
