package semillero.ubuntu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import semillero.ubuntu.dto.PublicationDto;
import semillero.ubuntu.entities.Publication;
import semillero.ubuntu.entities.User;
import semillero.ubuntu.mapper.PublicationMapper;
import semillero.ubuntu.repository.PublicationRepository;
import semillero.ubuntu.service.contract.PublicationService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
@Service
public class PublicarionImpl implements PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PublicationMapper publicationMapper;

    @Autowired
    private FileUploadImpl fileUploadService;

    //guarda la publicacion
    @Override
    public ResponseEntity<?> createPublication(PublicationDto publicationDTO) {
        //get the user id from the spring security sesion
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String username = currentUser.getUsername();

        System.out.println("username: " + username );
        System.out.println("currentUser: " + currentUser);

        // Validaciones adicionales en la capa de servicio si es necesario
        validatePublicationDTO(publicationDTO);

        // Mapear DTO a entidad
        Publication publication = publicationMapper.mapDtoToEntity(publicationDTO);

        // Procesar las imágenes (ejemplo: aquí asumo que las imágenes son URLs)
        //List<String> processedImages = processImages(publicationDTO.getImages());
        publication.setUser((User) currentUser);

        // Asignar las imágenes procesadas a la entidad
        try {
            publication.setImages(fileUploadService.uploadImage(publicationDTO.getMultipartImages()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Persistir la entidad
        publicationRepository.save(publication);

        // Mapear DTO a entidad
        Publication publicationEntity = publicationMapper.mapDtoToEntity(publicationDTO);

        return ResponseEntity.ok(publicationEntity);
    }

    private void validatePublicationDTO(PublicationDto publicationDto) {
        // Realizar validaciones adicionales si es necesario
        // Por ejemplo, puedes verificar ciertas condiciones en los datos del DTO
        // y lanzar excepciones si las condiciones no se cumplen.
    }

    private List<String> processImages(List<String> images) {
        //process images with cloudinary


        return images;
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
