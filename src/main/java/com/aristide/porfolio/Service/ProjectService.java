package com.aristide.porfolio.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aristide.porfolio.Model.Project;
import com.aristide.porfolio.Model.ProjectImage;
import com.aristide.porfolio.Repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FileStorageService fileStorageService;

    public ProjectService(ProjectRepository projectRepository,
         FileStorageService fileStorageService){
            
        this.projectRepository = projectRepository;
        this.fileStorageService  = fileStorageService;
    }

    public List <Project> getAllProjectsOrdered(){
        return projectRepository.findAllByOrderByProjectOrderAsc();
    }

    //CREATION DU PROJET
    @Transactional
    public Project createProject(String title, String description, List <MultipartFile> imageFiles){
        
        //VALIDATION METIER: CONTARINTE DE 1 A 12 IMAGES
        if (imageFiles == null || imageFiles.get(0).isEmpty()) {
            throw new IllegalArgumentException("Un projet doit contenir au moins 1 image.");
        }
        if (imageFiles.size() > 12) {
            throw new IllegalArgumentException("Un projet ne peut pas contenir plus de 12 images.");
        }

        Project project = new Project();

        //CALCUL DE L'ORDRE INCREMENTE 
        Integer maxOrder = projectRepository.findMaxProjectOrder();
        int nextOrder = (maxOrder == null ) ?  1 : maxOrder + 1;

        project.setProjectOrder(nextOrder);
        project.setTitle(title);
        project.setDescription(description);

        //SAUVEGARDE DES IMAGES 
        for (MultipartFile file : imageFiles){
            if (!file.isEmpty()) {
                String imagePath = fileStorageService.storeFile(file, "project");
                ProjectImage projectImage = new ProjectImage();
                projectImage.setImagePath(imagePath);
                projectImage.setProject(project);
                project.getImages().add(projectImage);
            }
        }
        return projectRepository.save(project);
    }
    
    //SUPPRESION DU PROJET
    @Transactional
    public void deleteProject(Long id){
        Project project = projectRepository.findById(id).orElseThrow(() -> new 
        IllegalArgumentException("Projets introuvable id: " + id));

        //SUPPRESSION DES FICHIERS IMAGES DE LA BD
        for (ProjectImage image : project.getImages()){
            fileStorageService.deleteFile(image.getImagePath());
        }
        //suppression en bd
        projectRepository.delete(project);
    }
}
