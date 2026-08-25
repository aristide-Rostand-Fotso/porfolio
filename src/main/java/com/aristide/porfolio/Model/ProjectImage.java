package com.aristide.porfolio.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="project_images")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

}
