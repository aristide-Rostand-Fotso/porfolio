package com.aristide.porfolio.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristide.porfolio.Model.FooterConfig;

@Repository
public interface FooterConfigRepository extends JpaRepository<FooterConfig, Long> {
Optional<FooterConfig> findFirstByOrderByIdAsc();
}
