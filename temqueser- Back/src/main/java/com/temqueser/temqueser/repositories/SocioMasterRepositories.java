package com.temqueser.temqueser.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.temqueser.temqueser.models.Socio;
import com.temqueser.temqueser.models.SocioMaster;

@Repository
public interface SocioMasterRepositories extends JpaRepository<SocioMaster,Long>{
    //Ja tem todos metodos disponiveis
    Optional<SocioMaster> findByEmailSocioMaster(String emailSocioMaster);
    boolean existsByEmailSocioMaster(String emailSocioMaster);
}