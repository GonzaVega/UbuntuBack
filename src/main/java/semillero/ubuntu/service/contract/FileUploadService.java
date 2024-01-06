package semillero.ubuntu.service.contract;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileUploadService {

    List<String> saveImage(List<MultipartFile> multipartFiles) throws IOException;

    List<String> uploadImage(List<MultipartFile> multipartFiles) throws IOException;

    List<String> updateImage(List<String> existingImages, List<MultipartFile> newImages) throws IOException;

}
