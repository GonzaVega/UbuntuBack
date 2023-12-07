package semillero.ubuntu.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import semillero.ubuntu.dto.CategoryDto;
import semillero.ubuntu.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Entidad Category a CategoryDto
    CategoryDto categoryEntityToDto(Category category);

    // CategoryDto a Category Entity
    Category categoryDtoToEntity(CategoryDto categoryDto);

}
