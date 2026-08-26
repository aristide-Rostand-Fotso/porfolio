package com.aristide.porfolio.Service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file)
    throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        //envoie du fichier vers cloudinary
        Map uploadResult = cloudinary.uploader().
        upload(file.getBytes(),
    ObjectUtils.asMap(
        "resource_type","auto" // gere auto les images et les documents 
    ));

    // Retourne L'url securiaee HTTPS du fichier heberge
    return uploadResult.get("secure_url").toString();
    }

}
