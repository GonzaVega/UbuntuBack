package semillero.ubuntu.mapper;

import semillero.ubuntu.dto.PublicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import semillero.ubuntu.entities.Publication;

@Mapper(componentModel = "spring")
public interface PublicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "images", ignore = true) // Ignoramos la lista de imágenes, ya que será procesada
    Publication mapDtoToEntity(PublicationDto publicationDto);

    @Mapping(target = "images", ignore = true) // Ignoramos la lista de imágenes, ya que será procesada
    PublicationDto mapEntityToDto(Publication publication);
}
