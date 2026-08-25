package com.aristide.porfolio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aristide.porfolio.Model.Project;

@Repository
public interface ProjectRepository  extends JpaRepository<Project, Long>{
//RECUPERE LA LISTE DES PROJETS TRIES PAR ORDRE INCREMENT
List<Project>
findAllByOrderByProjectOrderAsc();

//TROUVE LA VALEUR MAX DE PROJECTORDER POUR AUTOM-INCREMENT LE TITRE DU PROJECT SUIVANT
@Query("SELECT COALESCE(MAX(p.projectOrder), 0) FROM Project p")
Integer findMaxProjectOrder();
}
