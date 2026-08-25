package com.aristide.porfolio.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aristide.porfolio.Model.AdminConfig;
import com.aristide.porfolio.Model.ContactMessage;
import com.aristide.porfolio.Repository.AdminConfigRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AdminConfigRepository adminConfigRepository;

    @Async // il doit isoler chaque envoi d'acuse de reception dans le bloc try/catch

    public void envoyerMessageContact(ContactMessage contactMessage) {

        // recupere l'adresse e-mail config dans la bd
        try {
            String destinataire = adminConfigRepository
                    .findAll().stream()
                    .findFirst()
                    .map(AdminConfig::getAdminEmail)
                    .orElse("ton.email.par.defaut@gmail.com");

            // construction et envoi du mail (Sans enregistremnt en bdd)
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(destinataire);

            mail.setReplyTo(contactMessage.getEmail()); // permet de repondre directement au visteur
            mail.setSubject("Nouveau message de conctact : " + contactMessage.getName());
            mail.setText("Nom : " + contactMessage.getName() + "\n"
                    + contactMessage.getEmail() + "\n\n" + "Message :\n" + contactMessage.getMessage());
            mailSender.send(mail);

        } catch (Exception e) {
            System.err.println("Erreu d'envoi du mail Admin : " + e.getMessage());

        }

        // envoi de l'acc recep au visiteur
        try {
            envoyerAccuserReception(contactMessage);
        } catch (Exception e) {
            System.err.println("Erreu d'envoi de l'accusé de reception : " + e.getMessage());

        }
    }

    // Accuse de reception envoye au visiteur
    private void envoyerAccuserReception(ContactMessage contactMessage) {
        // vrifie ce qui s'affiche 
        System.out.println("DEBUG : Envoi accué à ->" + contactMessage.getEmail());
        
        SimpleMailMessage mailClient = new SimpleMailMessage();
        mailClient.setTo(contactMessage.getEmail()); // @ saisie dans le formulaire

        mailClient.setSubject("Accusé de réception - Confirmation de votre message ");
        mailClient.setText("Bonjour " + contactMessage.getName() + ", \n\n" +
                "J'ai bien reçu votre message et je vous remercie.\n"
                + "Je reviendrai vers vous dans les plus bref délais.\n\n" + "Cordialement, \n" + "Aristide");

        mailSender.send(mailClient);

    }

}
