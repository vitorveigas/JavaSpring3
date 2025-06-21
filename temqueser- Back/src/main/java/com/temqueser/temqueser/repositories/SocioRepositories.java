package com.temqueser.temqueser.repositories;

import com.temqueser.temqueser.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocioRepositories extends JpaRepository<Socio, Long> {
    //Ja tem todos metodos disponiveis
    Optional<Socio> findByEmailSocio(String emailSocio);
    boolean existsByEmailSocio(String emailSocio);
}
