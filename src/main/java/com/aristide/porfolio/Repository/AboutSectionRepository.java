package com.aristide.porfolio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristide.porfolio.Model.AboutSection;

@Repository
public interface AboutSectionRepository extends JpaRepository<AboutSection, Long> {
    
    //RECUPERER L'UNIQUE ENREGISTREMENT DE LA  SECTION A-PROPOS
    AboutSection findFirstByOrderByIdAsc();
}
