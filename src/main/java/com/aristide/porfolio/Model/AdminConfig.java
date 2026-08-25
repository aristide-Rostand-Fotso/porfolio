package com.aristide.porfolio.Model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable =false)
    private String adminEmail;

}
