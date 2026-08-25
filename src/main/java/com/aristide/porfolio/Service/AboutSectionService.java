package com.aristide.porfolio.Service;


import org.springframework.stereotype.Service;

import com.aristide.porfolio.Model.AboutSection;
import com.aristide.porfolio.Repository.AboutSectionRepository;

@Service
public class AboutSectionService {

    private final AboutSectionRepository aboutRepository;

    public AboutSectionService(AboutSectionRepository aboutRepository){
        this.aboutRepository = aboutRepository;
    }

    public AboutSection getAboutData(){
        AboutSection about = aboutRepository.findFirstByOrderByIdAsc();
        if (about == null) {
            about = new AboutSection();  //RETOURNE UN VIDE POUR EVITER LES NULLPOINTER
        }
        return about;
    }

    public AboutSection saveOrUpdateAbout(AboutSection aboutData){
        AboutSection existing = aboutRepository.findFirstByOrderByIdAsc();
        if (existing != null) {
            aboutData.setId(existing.getid()); // CONSERVE L'ID UNIQUE
        }
        return aboutRepository.save(aboutData);
    }
}
