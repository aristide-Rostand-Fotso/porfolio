package com.aristide.porfolio.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aristide.porfolio.Model.Formation;
import com.aristide.porfolio.Repository.FormationRepository;

@Service
public class FormationService {

    private final FormationRepository formationRepository;

    public FormationService(FormationRepository formationRepository){
        this.formationRepository = formationRepository;
    }

    //formation
    public List<Formation>getAllFormationRepositoryOnly(){
        return formationRepository.findByIsObjectifFalseOrderByIdDesc();
    }

    public Optional<Formation> getformationById(Long id){
        return formationRepository.findById(id);
    }
    
    public Formation saveFormation(Formation formation){
        formation.setIsObjectif(false);
        return formationRepository.save(formation);
    }

    public void delateFormation(Long id){
        formationRepository.deleteById(id);
    }

    //objectif 
    public Formation getObjectif(){
        return formationRepository.findFirstByIsObjectifTrue().orElse(null);
    }

    public void saveOrUpdateObjectif(String titre, String description){
        Formation objectif = formationRepository.findFirstByIsObjectifTrue()
        .orElse(new Formation());
        objectif.setIsObjectif(true);
        objectif.setDiplome(titre); // on reuttilise le champ diplome pour stocker le titre de objectif
        objectif.setDescription(description);
        formationRepository.save(objectif);
    }
}
