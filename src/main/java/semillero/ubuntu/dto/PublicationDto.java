package semillero.ubuntu.dto;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PublicationDto {
    @NotBlank(message = "El título no debe estar en blanco")
    private String title;

    @NotBlank(message = "La descripción no puede estar en blanco")
    @Size(max = 2500, message = "La descripción no puede tener más de 2500 caracteres")
    private String description;

    private MultipartFile[] multipartImages;

    private List<String> images;
}
