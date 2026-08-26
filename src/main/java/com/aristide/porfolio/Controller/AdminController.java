package com.aristide.porfolio.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aristide.porfolio.Model.AboutSection;
import com.aristide.porfolio.Model.AdminConfig;
import com.aristide.porfolio.Model.CurriculumVitea;
import com.aristide.porfolio.Model.FooterConfig;
import com.aristide.porfolio.Model.Formation;
import com.aristide.porfolio.Model.UserProfile;
import com.aristide.porfolio.Repository.AdminConfigRepository;
import com.aristide.porfolio.Repository.CurriculumViteaRepository;
import com.aristide.porfolio.Repository.FooterConfigRepository;
import com.aristide.porfolio.Repository.FormationRepository;
import com.aristide.porfolio.Repository.UserProfileRepository;
import com.aristide.porfolio.Service.AboutSectionService;
import com.aristide.porfolio.Service.CloudinaryService;
import com.aristide.porfolio.Service.FormationService;
import com.aristide.porfolio.Service.ProjectService;

@Controller
@RequestMapping("/admi-237-n")
public class AdminController {

    // ENCAPSULATION
    private final UserProfileRepository userProfileRepository;
    private final CurriculumViteaRepository curriculumViteaRepository;
    private final AboutSectionService aboutService;
    private final ProjectService projectService;
    private final AdminConfigRepository adminConfigRepository;
    private final FooterConfigRepository footerConfigRepository;
    private final FormationService formationService;
    private final FormationRepository formationRepository;
    private final CloudinaryService cloudinaryService;

    // CONSTRUTEUR PREND EN PARAMETRE LES INJECTIONS GEREES PAR SPTING ET LES
    // ALIGNES AUX ATTRIBUTS DE LA CLASSE GRACE AUX MOT CLES 'this'
    public AdminController(UserProfileRepository userProfileRepository,
            CurriculumViteaRepository curriculumViteaRepository,
            AboutSectionService aboutService, ProjectService projectService,
            AdminConfigRepository adminConfigRepository,
            FooterConfigRepository footerConfigRepository, FormationService formationService,
            FormationRepository formationRepository, CloudinaryService cloudinaryService) {

        this.userProfileRepository = userProfileRepository;
        this.curriculumViteaRepository = curriculumViteaRepository;
        this.aboutService = aboutService;
        this.projectService = projectService;
        this.adminConfigRepository = adminConfigRepository;
        this.footerConfigRepository = footerConfigRepository;
        this.formationService = formationService;
        this.formationRepository = formationRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // AFFICHER LA PAGE ADMIN
    @GetMapping
    public String afficherAdmin(Model model) {
        UserProfile userProfile = userProfileRepository.findAll().stream()
                .findFirst()
                .orElse(new UserProfile());
        // recupere le dernier cv
        CurriculumVitea cv = curriculumViteaRepository.findAll().stream()
                .reduce((first, second) -> second)
                .orElse(null);

        model.addAttribute("userProfile", userProfile);
        model.addAttribute("cv", cv);
        model.addAttribute("about", aboutService.getAboutData());
        model.addAttribute("projects", projectService.getAllProjectsOrdered());

        // recuperer l'e-mail
        String adminEmail = adminConfigRepository
                .findAll().stream()
                .findFirst()
                .map(AdminConfig::getAdminEmail)
                .orElse("ton-email-par-defaut@gmail.com");

        model.addAttribute("adminEmail", adminEmail);

        // on recupere la config existante pour le footer
        FooterConfig existingConfig = footerConfigRepository.findAll().stream()
                .findFirst()
                .orElse(new FooterConfig());
        model.addAttribute("footerConfig", existingConfig);

        // injectiondonnees de la formation dans le modele
        // on recupere l'objectif den-tete(isObjectif = true)
        Formation objective = formationService.getObjectif();
        if (objective == null) {

            // interaction d'un objet vide
            objective = new Formation();
        }

        model.addAttribute("objective", objective);
        // objet pour entete
        model.addAttribute("formationForm", new Formation());
        // objet vide pour le formulaire d'ajout
        model.addAttribute("formations", formationService.getAllFormationRepositoryOnly()); // liste des diplomes

        return "admin";
    }

    // ENREGISTREMENT DES MODIFICATIONS DE LA SECTIO ACCEUIL
    @PostMapping("/acceuil/enregistrer")
    public String enregistrerAcceuil(@ModelAttribute("userProfile") UserProfile profileForm,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
            @RequestParam(value = "cvTitle", required = false) String cvTitle,
            RedirectAttributes redirectAttributes) {
        try {
            
            // DOSSIER OU SERONT ENREGISTRER PP ET CV
            // RECUPERER L'ENTITE SUAVEGARDER EN BD POUR GARDER LA MEME ID
            UserProfile existingProfile = userProfileRepository.findAll().stream()
                    .findFirst()
                    .orElse(new UserProfile());
            // METTRE A JOUR LES CHAMPS DEPUIS LE FORMULAIRE
            existingProfile.setFullName(profileForm.getFullName());
            existingProfile.setTitle(profileForm.getTitle());
            existingProfile.setShortBio(profileForm.getShortBio());
            existingProfile.setGithubUrl(profileForm.getGithubUrl());
            existingProfile.setLinkedinUrl(profileForm.getLinkedinUrl());
            existingProfile.setInstagramUrl(profileForm.getInstagramUrl());

            // traitement de la pp / ipload vers Cloudinary
            if (photoFile != null && !photoFile.isEmpty()) {

                String photoUrl = cloudinaryService.uploadFile(photoFile);
                existingProfile.setProfilePhotopath(photoUrl);
            }

            // SAUCEGARDE DES INFOS DU PRFIL(nom,titre)
            userProfileRepository.save(existingProfile);

            // traitement du fichier cv vers cloudinary
            if (cvFile != null && !cvFile.isEmpty()) {
                String cvUrl = cloudinaryService.uploadFile(cvFile);
                CurriculumVitea newCv = curriculumViteaRepository.findAll().stream()
                        .findFirst()
                        .orElse(new CurriculumVitea());

                newCv.setTitle(cvTitle != null && !cvTitle.isBlank() ? cvTitle : "CV Aristide");
                newCv.setFilePath(cvUrl);
                newCv.setUploadDate(LocalDateTime.now());
                curriculumViteaRepository.save(newCv);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Section Acceuil mise à jour avec succès ! ");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du téléchargement des fichier.");
        }
        return "redirect:/admi-237-n#acceuil";
    }

    // ENREGISTREMENT SECTION A-PROPOS
    @PostMapping("/about/enregistrer")
    public String enregistrerAbout(@ModelAttribute("about") AboutSection aboutForm,
            RedirectAttributes redirectAttributes) {
        aboutService.saveOrUpdateAbout(aboutForm);
        redirectAttributes.addFlashAttribute("successMessage",
                "Section À Propos mis à jour avec succès ! ");

        return "redirect:/admi-237-n#a-propos";
    }

    // ROUTE GESTION DES PROJETS AJOUT ET SUPPRESSION
    @PostMapping("/projects/ajouter")
    public String ajouterProjet(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("images") List<MultipartFile> images, RedirectAttributes redirectAttributes) {
        try {
            projectService.createProject(title, description, images);
            redirectAttributes.addFlashAttribute("successMessage", "Le projet a été créé avec succès !");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Une erreur est survenue lors de la creation du project");
        }
        return "redirect:/admi-237-n#projets";
    }

    @PostMapping("/projects/supprimer/{id}")
    public String supprimerProject(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute("successMessage", "Le projet a été supprimé avec succès ");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la supprssion du prjet");
        }
        return "redirect:/admi-237-n#projets";
    }

    @PostMapping("/config/email")
    public String uploadAdminemail(@RequestParam String adminEmail, RedirectAttributes redirectAttributes) {
        AdminConfig config = adminConfigRepository.findAll().stream()
                .findFirst()
                .orElse(new AdminConfig());

        config.setAdminEmail(adminEmail);
        adminConfigRepository.save(config);
        redirectAttributes.addFlashAttribute("successMessage", "Adresse e-mail mise à jour avec succès ! ");

        return "redirect:/admi-237-n#contact";
    }

    @PostMapping("/footer/enregistrer")
    public String enregistrerFooter(@ModelAttribute("footerConfig") FooterConfig footerForm,
            RedirectAttributes redirectAttributes) {

        // recuperation existant de l'enregistrement ou creation si vide
        FooterConfig existingConfig = footerConfigRepository.findFirstByOrderByIdAsc()
                .orElse(new FooterConfig());

        // mise a jour des valeurs envoyees depuis le formulaire admin
        existingConfig.setWhatsappNumber(footerForm.getWhatsappNumber());
        existingConfig.setLocalisation(footerForm.getLocalisation());
        existingConfig.setAvailability(footerForm.getAvailability());
        existingConfig.setCopyrighttext(footerForm.getCopyrighttext());

        // sauvegarde (UPLOAD si ID present, INSERT si nouvel objet)
        footerConfigRepository.save(existingConfig);

        redirectAttributes.addFlashAttribute("successMessage", "Footer mis à jour avec succès !.");
        return "redirect:/admi-237-n#footer";
    }

    @PostMapping("/formations/save-objective")
    public String saveObjective(@RequestParam("titre") String titre,
            @RequestParam("description") String description,
            RedirectAttributes redirectAttributes) {
        formationService.saveOrUpdateObjectif(titre, description);
        redirectAttributes.addFlashAttribute("successmessage",
                "Objectif de la formation mis à jour avec succès !");

        return "redirect:/admi-237-n#formations";
    }

    // ENREGISTRER OU MODIFIER UNE FORMATION / DIPLOME
    @PostMapping("/formations/save")
    public String saveFromation(@ModelAttribute("formationForm") Formation formation,
            RedirectAttributes redirecAttributes) {
        formationService.saveFormation(formation);
        redirecAttributes.addFlashAttribute("successMMessage",
                "Formation enregistrées avec succès !");

        return "redirect:/admi-237-n#formations";
    }

    // supprimer une formation
    @PostMapping("/formations/delete/{id}")
    public String deleteFormation(@PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {
        formationRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMMessage", "Formation supprimée avec succès !");

        return "redirect:/admi-237-n#formations";
    }

}