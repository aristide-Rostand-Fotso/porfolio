package com.aristide.porfolio.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristide.porfolio.Model.Formation;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    //reccupere uni les formation hors object
     List<Formation> findByIsObjectifFalseOrderByIdDesc();
       
    

    // recupere lenregistrement objectif s'il existe 
    Optional<Formation> findFirstByIsObjectifTrue();

}
