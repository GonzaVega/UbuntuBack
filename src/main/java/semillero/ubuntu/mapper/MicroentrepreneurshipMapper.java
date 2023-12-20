package semillero.ubuntu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Microentrepreneurship;

@Mapper(componentModel = "spring")
public interface MicroentrepreneurshipMapper {

    //create the mapper
    @Mapping(target = "id", ignore = false)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isManaged", ignore = true)
    @Mapping(target = "images", ignore = false)
    Microentrepreneurship mapToEntity(MicroentrepreneurshipDto microentrepreneurshipDto);

    @Mapping(target = "images", ignore = true)
    MicroentrepreneurshipDto mapEntityToDto(Microentrepreneurship microentrepreneurship);

}
