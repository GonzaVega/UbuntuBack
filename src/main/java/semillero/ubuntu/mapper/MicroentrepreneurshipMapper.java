package semillero.ubuntu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Microentrepreneurship;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MicroentrepreneurshipMapper {

    //create the mapper
    @Mapping(target = "id", ignore = false)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isManaged", ignore = true)
    @Mapping(target = "images", ignore = false)
    Microentrepreneurship mapToEntity(MicroentrepreneurshipDto microentrepreneurshipDto);

    @Mapping(target = "images", ignore = false)
    MicroentrepreneurshipDto mapEntityToDto(Microentrepreneurship microentrepreneurship);

    @Mapping(target = "images", ignore = false)
    List<MicroentrepreneurshipDto> entityListToDtoList(List<Microentrepreneurship> microentrepreneurships);

    @Mapping(target = "images", ignore = false)
    List<Microentrepreneurship> dtoListToEntityList(List<MicroentrepreneurshipDto> microentrepreneurshipDtos);

}
