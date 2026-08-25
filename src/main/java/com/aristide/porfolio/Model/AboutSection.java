package com.aristide.porfolio.Model;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

@Entity
@Table(name ="about_section")
public class AboutSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //INFORMATION PERSONNELES
    private String birthPlace; //LIEU DE NAISSANCE
    private String experienceYears; //ANNÉES D'EXPÉRIENCE

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate  birthDate; //DATE DE NAISSANCE

    // INFORMATION RELATIVE AUX PROJETS
    private Integer successfulProjects; //PROJETS RÉUSSIS +1
    private Integer happyClients; //CLIENTS SATISFAITS 
    private Boolean isFreelance; //STATUT FREELANCE

    //BIO & SIGNATURE
    @Column(columnDefinition = "TEXT")
    private String bioDescription; //BIOGRAPHIE pour le texte principale a-propos
    private String signatureText; //TEXTE DE SIGNATURE
    private String signatureFont; //POLICE APPLIQUEE('GREATVIBES' CURSIVE)

    // STACK
    @Column(name = "stack_title")
    private String stackTitle;

    @Column(name = "stack_description", columnDefinition = "TEXT")
    private String stackDescription;

    // SOLUTION SUR MESSURE
    @Column(name = "custom_solutions_description", columnDefinition = "TEXT")
    private String customSolutionsDescription;

    //VEILLE CONTINUE
    @Column(name = "continuous_watch_description", columnDefinition = "TEXT")
    private String continuousWatchDescription;


    // METHODE METIER CALCUL AGE & FALLBCKS
    // calcul auto a partir de birthdate
    public String getCalculatedAge(){
       if (this.birthDate == null) {
        return "Non renseigé";
       }
       return Period.between(this.birthDate, LocalDate.now()).getYears() + "ans";
    }

    //GETTER AVEC LA VALEUR PAR DEFAUT "NON RENSEIGNE"
    public String getBirthPlaceDisplay(){
        return (birthPlace != null && !birthPlace.isBlank()) ? 
        birthPlace : "Non renseigé";
    }

    public String getExperienceYearsDisplay(){
        return (experienceYears != null && !experienceYears.isBlank()) ? 
        experienceYears : "Non renseigé";
    }

    public String getFormattedBirthDate(){
        if (this.birthDate == null) {
            return "Non renseigé";
        }
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.
        ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH);
        return this.birthDate.format(formatter);
    }

    public String getSignatureTextDisplay(){
        return (signatureText != null && !signatureText.isBlank()) ?
        signatureText : "Non renseigé";
    }
    

    //GETTERS ET SETTERS STANDARD
    public Long getid(){
        return id;
    }
    public void setId(Long id){
        this.id =id;
    }
    public String getBirthPlace(){
        return birthPlace;
    }
    public void setBirthPlace(String birthPlace){
        this.birthPlace = birthPlace;
    }
    public String getExperienceYears(){
        return experienceYears;
    }
    public void setExperienceYears(String experienceYears){
        this.experienceYears = experienceYears;
    }
    public LocalDate getBirthDate(){
        return birthDate;
    } 
    public void setBirthDate(LocalDate birthDate){
        this.birthDate = birthDate;
    }
    public Integer getSuccessfulProjects(){
        return successfulProjects;
    }
    public void setSuccessfulProjects(Integer sucessfullProjects){
        this.successfulProjects = sucessfullProjects;
    }
    public Integer getHappyClients(){
        return happyClients;
    }
    public void setHappyClients(Integer happyClients){
        this.happyClients = happyClients;
    }
    public Boolean getIsFreelance(){
        return isFreelance;
    }
    public void setIsFreelance(Boolean isFreelance){
        this.isFreelance = isFreelance;
    }
    public String getBioDescription(){
        return bioDescription;
    }
    public void setBioDescription(String bioDescription){
        this.bioDescription = bioDescription;
    }
    public String getSignatureText(){
        return signatureText;
    }
    public void setSignatureText(String signatureText){
        this.signatureText = signatureText;
    }
    public String getSignatureFont(){
        return signatureFont;
    }
    public void setSignatureFont(String signatureFont){
        this.signatureFont = signatureFont;
    }

    //GETTER ET SETTER DES CARTES 
    public String getStackTitle(){
        return stackTitle;
    }
    public void setStackTitle(String stackTitle){
        this.stackTitle = stackTitle;
    }

    public String getStackDescription(){
        return stackDescription;
    }
    public void setStackDescription(String stackDescription){
        this.stackDescription = stackDescription;
    }

    public String getCustomSolutionsDescription(){
        return customSolutionsDescription;
    }
    public void setCustomSolutionsDescription(String customSolutionsDescription){
        this.customSolutionsDescription = customSolutionsDescription;
    }

    public String getContinuousWatchDescription(){
        return continuousWatchDescription;
    }
    public void setContinuousWatchDescription(String continuousWatchDescription){
        this.continuousWatchDescription = continuousWatchDescription;
    }



}
