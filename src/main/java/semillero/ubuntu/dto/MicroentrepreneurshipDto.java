package semillero.ubuntu.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MicroentrepreneurshipDto {
    private Long id;
    private String name;
    private String country;
    private String province;
    private String city;
    private CategoryDto category;
    private String subCategory;
    private String description;
    private String moreInfo;
    private MultipartFile[] multipartImages;
    private List<String> images;
}
