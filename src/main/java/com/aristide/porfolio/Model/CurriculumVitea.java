package com.aristide.porfolio.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curriculum_vitea")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumVitea {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String filePath;
    private LocalDateTime uploadDate;

}
