package com.aristide.porfolio.Controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aristide.porfolio.Model.ContactMessage;
import com.aristide.porfolio.Model.CurriculumVitea;
import com.aristide.porfolio.Model.FooterConfig;
import com.aristide.porfolio.Model.Formation;
import com.aristide.porfolio.Model.UserProfile;
import com.aristide.porfolio.Service.AboutSectionService;
import com.aristide.porfolio.Service.EmailService;
import com.aristide.porfolio.Service.FormationService;
import com.aristide.porfolio.Service.PorfolioService;
import com.aristide.porfolio.Service.ProjectService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientController {

    private final PorfolioService porfolioService;
    private final ProjectService projectService;
    private final AboutSectionService aboutService;
    private final EmailService emailService;
    private final FormationService formationService;

    public ClientController(PorfolioService porfolioService, ProjectService projectService,
            AboutSectionService aboutService, EmailService emailService, FormationService formationService) {

        this.porfolioService = porfolioService;
        this.projectService = projectService;
        this.aboutService = aboutService;
        this.emailService = emailService;
        this.formationService = formationService;
    }

    // PAGE PRINCIPALE DU PORFOLIO (VISITEUR)
    @GetMapping("/")
    public String index(Model model, HttpSession session) {

        // PROFIL UTILISATEUR
        Optional<UserProfile> profileOpt = porfolioService.getUserProfile();
        UserProfile userProfile = profileOpt.orElse(null);
        model.addAttribute("userProfile", userProfile);

        // CV UNIQUE
        Optional<CurriculumVitea> cvOpt = porfolioService.getCurriculumVitea();
        CurriculumVitea cv = cvOpt.orElse(null);
        model.addAttribute("cv", cv);
        model.addAttribute("cvExist", cv != null && cv.getFilePath() != null);
        model.addAttribute("cvTitle", cv != null ? cv.getTitle() : "Aucun CV disponible");

        // PROJETS
        model.addAttribute("projects", projectService.getAllProjectsOrdered());

        // FOOTER CONFIG
        model.addAttribute("footerConfig", porfolioService.getFooterConfig().orElse(new FooterConfig()));

        // FORMULAIRE DE CONTACT VIDE
        model.addAttribute("contactMessage", new ContactMessage());

        // INJECTION DES DONNEES DE LA SECTION A-PROPOS -
        model.addAttribute("about", aboutService.getAboutData());

        //INJECTION DES DONNEES DE LA SECTION FORMATION
        //recuperation d'objet d'entete(isObjectif = true)-//variable locale'objective' de type formation
        Formation objective = formationService.getObjectif();

        //recuperation des formations classiques(isObjectif = false) -variable locale'objective' de type List<Formation>
        List<Formation> formations = formationService.getAllFormationRepositoryOnly();
        model.addAttribute("objective", objective); //permet d'afficher la couverture- l'objectif en haut
        model.addAttribute("formations", formations);
        
        return "index";
    }

    // TELECHARGEMENT DU CV
    @GetMapping("/cv/download")
    public ResponseEntity<Resource> downloadCV() {
        Optional<CurriculumVitea> cvOpt = porfolioService.getCurriculumVitea();
        if (cvOpt.isEmpty() || cvOpt.get().getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Path filePath = Paths.get(cvOpt.get().getFilePath().replaceFirst("^/", ""));
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(
                        "CONTENT_DISPOSITION", "attachment; filename=\"" + cvOpt.get().getTitle()
                                + ".pdf\"")
                        .body(resource);

            }
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.notFound().build();
    }

    // TRAITEMENT DU FORMULAIRE DE CONTACT
    @PostMapping("/contact-api")
    @ResponseBody
    public ResponseEntity<String>submitContact(@RequestBody ContactMessage contactMessage) {

        // 1. validation: mail, nom et message doivent etre renseignes
        if (contactMessage.getName() == null
                || contactMessage.getName().trim().isEmpty()

                || contactMessage.getEmail() == null
                || contactMessage.getEmail().trim().isEmpty()

                || contactMessage.getMessage() == null
                || contactMessage.getMessage().trim().isEmpty()) {

            return ResponseEntity.badRequest().body("Tous les champs (nom, email, message) sont obligatoires.");

        }

        try {
            // 2.recuperation de @-email distinataire enregistree dans emailService
            // enregistrer dans emailService
            emailService.envoyerMessageContact(contactMessage);
            return ResponseEntity.ok( "Votre message à été envoyé avec succèss !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Une erreur est survenue lors de l'envoi du message. ");
        }
    }
}
