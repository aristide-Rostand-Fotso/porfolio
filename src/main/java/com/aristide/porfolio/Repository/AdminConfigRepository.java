package com.aristide.porfolio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristide.porfolio.Model.AdminConfig;

@Repository
public interface AdminConfigRepository  extends JpaRepository<AdminConfig, Long> {

}
