package com.aristide.porfolio.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristide.porfolio.Model.CurriculumVitea;

@Repository
public interface CurriculumViteaRepository extends JpaRepository<CurriculumVitea, Long> {
//GARANTIT L'ACCES AU SEUL CV PRESENT
Optional<CurriculumVitea> findFirstByOrderByIdAsc();
}
