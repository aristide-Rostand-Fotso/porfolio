package com.aristide.porfolio.Model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="footer_config")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FooterConfig {

    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String whatsappNumber;
    private String localisation;
    
    private String availability;    //exemple : "Frelance / stage Proffesionnel"

    @Column(columnDefinition = "TEXT")
    private String copyrighttext;

}
