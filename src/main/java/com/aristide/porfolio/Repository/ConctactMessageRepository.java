package com.aristide.porfolio.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristide.porfolio.Model.ContactMessage;

@Repository
public interface ConctactMessageRepository extends JpaRepository<ContactMessage, Long> {
//RECUPERE LES MESSAGES LES PLUS ANCIEN POUR LE DASBOARD ADMIN
List<ContactMessage> findAllByOrderBySubmittedAtDesc();
}
