package semillero.ubuntu.service.contract;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileUploadService {

    List<String> uploadImage(MultipartFile[] multipartFile) throws IOException;

    List<String> updateImage(List<String> images, MultipartFile[] multipartFiles) throws IOException;

}
