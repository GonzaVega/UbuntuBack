package semillero.ubuntu.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import semillero.ubuntu.dto.CategoryDto;
import semillero.ubuntu.entities.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Entidad Category a CategoryDto
    CategoryDto categoryEntityToDto(Category category);

    List<CategoryDto> categoriesEntityToDto(List<Category> categories);


    // CategoryDto a Category Entity
    Category categoryDtoToEntity(CategoryDto categoryDto);


}
