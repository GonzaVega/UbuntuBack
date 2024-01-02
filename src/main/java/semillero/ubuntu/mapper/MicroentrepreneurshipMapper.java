package semillero.ubuntu.mapper;

import org.mapstruct.Mapper;
import semillero.ubuntu.dto.MicroentrepreneurshipDto;
import semillero.ubuntu.entities.Microentrepreneurship;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface MicroentrepreneurshipMapper {

    MicroentrepreneurshipDto entityToDto(Microentrepreneurship microentrepreneurship);

    Microentrepreneurship dtoToEntity(MicroentrepreneurshipDto microentrepreneurshipDto);

    List<MicroentrepreneurshipDto> entityListToDtoList(List<Microentrepreneurship> microentrepreneurships);

    List<Microentrepreneurship> dtoListToEntityList(List<MicroentrepreneurshipDto> microentrepreneurshipDtos);
}