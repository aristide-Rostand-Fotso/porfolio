package com.aristide.porfolio.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path uploadLocation= Paths.get("uploads");

    public FileStorageService(){
        try{
            Files.createDirectories(uploadLocation);
        }catch (IOException e){
            throw new RuntimeException("impossible de creer le dossier de stockage",e);
        }
    }

    public String storeFile(MultipartFile file, String subDirectory){
        if (file.isEmpty()) {
            return null;  
        }
        try{
            Path targetDir = uploadLocation.resolve(subDirectory);
            Files.createDirectories(targetDir);
            String fileName = UUID.randomUUID().toString()+ "_" + file.getOriginalFilename();
            Path targetPath = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return"/uploads/"+subDirectory + "/" + fileName;
        }catch(IOException e){
            throw new RuntimeException("Erreur lors de l'enregistrment du fichier",e);
        }
    }

    public void deleteFile(String filePath){
        if (filePath == null || filePath.isEmpty()) return;   try {
            Path path = Paths.get(filePath.replaceFirst("^/", ""));

            Files.deleteIfExists(path);
        }catch(IOException e){
            System.err.println("Impossible de supprimer le fichier:" + filePath);
        }
    }

}
