package semillero.ubuntu.service.impl;
import semillero.ubuntu.config.CloudinaryConfig;
import semillero.ubuntu.service.contract.FileUploadService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUploadService {

    private final CloudinaryConfig cloudinaryConfig;

    @Override
    public List<String> saveImage(List<MultipartFile> multipartFiles) throws IOException {
        List<String> listImageName = new ArrayList<>();
        Cloudinary cloudinary = cloudinaryConfig.configuration();
        Map<String, Object> params = ObjectUtils.asMap(
                "use_filename", true,
                "folder", "ubuntu",
                "unique_filename", true,
                "overwrite", false
        );

        for (MultipartFile image : multipartFiles) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), params);
            String imageUrl = uploadResult.get("url").toString();
            listImageName.add(imageUrl);
        }
        return listImageName;
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFiles) throws IOException {
        return saveImage(multipartFiles);
    }


    //TODO: verificar si las imagenes son iguales
    @Override
    public List<String> updateImage(List<String> existingImages, List<MultipartFile> newImages) throws IOException {
        Cloudinary cloudinary = cloudinaryConfig.configuration();

        //Elimina las imagenes anteriores
        if (existingImages.size() > 0) {
            Map<String, Object> destroyParams = ObjectUtils.asMap("folder", "ubuntu");

            for (String image : existingImages) {
                destroyParams.put("public_id", image);
                    cloudinary.uploader().destroy(image, destroyParams);
            }

            //guarda las nuevas imagenes
            return saveImage(newImages);
        } else {
            return null;
        }
    }
}
