package com.aristide.porfolio.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

        .headers(headers -> headers 
            .frameOptions(frame -> frame.sameOrigin())
        )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/contact-api")) // DESACTIVE LA PROTECTION CSRF POUR LES TESTS, A REACTIVER EN PROD
                .authorizeHttpRequests(auth -> auth

                        // PROTEGE L'ADMINISTRATION DU SITE, SEULEMENT LES UTILISATEURS AUTHENTIFIES
                        // PEUVENT
                        // ACCEDER A CETTE PARTIE
                        .requestMatchers("/admi-237-n/**", "/h2-console/**").hasAuthority("ADMIN")
                        

                        // LE SITE PUBLIC RESTE 100% ACCESSIBLE A TOUS/ PUBLIC
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login") // URL DE LA PAGE LOGIN
                        .defaultSuccessUrl("/admi-237-n", true) // REDIRECTION VERS L'ADMINISTRATION APRES LOGIN
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // REDIRECTION VERS LA PAGE D'ACCUEIL APRES LOGOUT
                        .permitAll());
        return http.build();
    }

    // IDENTIFIANT TEMPORAIRE POUR SE CONNECTER EN MEMOIRE

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


   

}
