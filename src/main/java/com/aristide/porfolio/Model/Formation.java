package com.aristide.porfolio.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "formation")
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    // Derterminer si ces infos entete est une formation ou un ojectif
    private Boolean isObjectif = false;
    // champs partager objectif / formation
    private String diplome; // sert aussi de titre pour objectif
    private String etablissement; // optionnel pour objectif
    private String annee;

    @Column(columnDefinition = "TEXT")
    private String description; // description de la formation ou detail de l'objectif
    private String tags; // mot-cles separe par des virgules(fullstack, methode agile)
    private LocalDateTime createAt = LocalDateTime.now();

    // constructeur vide
    public Formation (){}

    // Get(ajouter/lire) & Set(modifier)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsObjectif() {
        return isObjectif;
    }

    public void setIsObjectif(Boolean isObjectif) {
        this.isObjectif = isObjectif;
    }

    public String getDiplome() {
        return diplome;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;

    }

}
