package com.aristide.porfolio.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profile")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String shortBio;
    private String profilePhotopath;
    private String githubUrl;
    private String linkedinUrl;
    private String instagramUrl;

}
