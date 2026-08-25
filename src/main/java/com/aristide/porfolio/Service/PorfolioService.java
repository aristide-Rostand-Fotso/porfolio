package com.aristide.porfolio.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aristide.porfolio.Repository.*;
import com.aristide.porfolio.Model.*;
@Service
public class PorfolioService {

    private final UserProfileRepository userProfileRepository;
    private final CurriculumViteaRepository curriculumViteaRepository;
    private final FooterConfigRepository footerConfigRepository;

    public PorfolioService(UserProfileRepository userProfileRepository, 
        CurriculumViteaRepository curriculumViteaRepository, 
        FooterConfigRepository footerConfigRepository){
            
        this.userProfileRepository = userProfileRepository;
        this.curriculumViteaRepository = curriculumViteaRepository;
        this.footerConfigRepository = footerConfigRepository;
    }

    public Optional<UserProfile> getUserProfile(){
        return userProfileRepository.findFirstByOrderByIdAsc();
    }
    public Optional<CurriculumVitea> getCurriculumVitea(){
        return curriculumViteaRepository.findFirstByOrderByIdAsc();
    }
    public Optional <FooterConfig> getFooterConfig(){
        return footerConfigRepository.findFirstByOrderByIdAsc();
    }
}
