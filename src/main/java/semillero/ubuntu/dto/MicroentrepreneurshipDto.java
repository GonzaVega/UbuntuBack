package semillero.ubuntu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import semillero.ubuntu.dto.CategoryDto;
import semillero.ubuntu.entities.Image;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MicroentrepreneurshipDto {

    private Long id;
    private String name;
    private String country;
    private String province;
    private String city;
    private CategoryDto category;
    private String subCategory;
    private List<Image> images;
    private Boolean isActive;
    private Boolean isManaged;
    private String description;
    private String moreInfo;
}