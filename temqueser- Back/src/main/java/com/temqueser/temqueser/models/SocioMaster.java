package com.temqueser.temqueser.models;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SocioMaster")
public class SocioMaster {
    public interface CreateSocioMaster{};
    public interface UpdateSocioMaster{};

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_socioMaster", nullable = false, length = 100, unique = true)
    private String nomeSocioMaster;

    @Column(name = "telefone_socioMaster", nullable = false, length = 15, unique = true)
    private String telefoneSocioMaster;

    @Column(name = "email_socioMaster", nullable = false, length = 100, unique = true)
    private String emailSocioMaster;

    @Column(name = "senha_socioMaster", nullable = false, length = 30)
    private String senhaSocioMaster;

    @Column(name = "cpf_socioMaster", length = 14, nullable = false, unique = true)
    private String cpfSocioMaster;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        SOCIO_MASTER
    }
    
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNomeSocioMaster() {
        return nomeSocioMaster;
    }


    public void setNomeSocioMaster(String nomeSocioMaster) {
        this.nomeSocioMaster = nomeSocioMaster;
    }


    public String getTelefoneSocioMaster() {
        return telefoneSocioMaster;
    }


    public void setTelefoneSocioMaster(String telefoneSocioMaster) {
        this.telefoneSocioMaster = telefoneSocioMaster;
    }


    public String getEmailSocioMaster() {
        return emailSocioMaster;
    }


    public void setEmailSocioMaster(String emailSocioMaster) {
        this.emailSocioMaster = emailSocioMaster;
    }


    public String getSenhaSocioMaster() {
        return senhaSocioMaster;
    }


    public void setSenhaSocioMaster(String senhaSocioMaster) {
        this.senhaSocioMaster = senhaSocioMaster;
    }


    public String getCpfSocioMaster() {
        return cpfSocioMaster;
    }


    public void setCpfSocioMaster(String cpfSocioMaster) {
        this.cpfSocioMaster = cpfSocioMaster;
    }


    
}