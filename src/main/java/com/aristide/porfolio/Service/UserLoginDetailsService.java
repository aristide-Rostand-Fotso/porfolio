package com.aristide.porfolio.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aristide.porfolio.Model.UserLogin;
import com.aristide.porfolio.Repository.UserLoginRepository;

@Service
public class UserLoginDetailsService implements UserDetailsService {

    private final UserLoginRepository userLoginRepository;
    
    public UserLoginDetailsService(UserLoginRepository userLoginRepository){
        this.userLoginRepository = userLoginRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException{
        UserLogin userLogin = userLoginRepository.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException("Utilisateur non trouve"));

        return org.springframework.security.core.userdetails
        .User.withUsername(userLogin.getUsername())
        .password(userLogin.getPassword())
        .authorities(userLogin.getRole())
        .build();
    }

}
