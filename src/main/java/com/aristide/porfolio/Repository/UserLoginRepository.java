package com.aristide.porfolio.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristide.porfolio.Model.UserLogin;

public interface UserLoginRepository extends JpaRepository <UserLogin, Long> {
    
Optional<UserLogin> findByUsername(String username);
}
