package com.temqueser.temqueser.models;

import jakarta.persistence.*;


import java.util.List;


@Entity
@Table(name = "Socio")
public class Socio {
    public interface CreateSocio {}
    public interface UpdateSocio {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_socio", nullable = false, length = 100, unique = true)
    private String nomeSocio;

    @Column(name = "telefone_socio", nullable = false, length = 15, unique = true)
    private String telefoneSocio;

    @Column(name = "email_socio", nullable = false, length = 100, unique = true)
    private String emailSocio;

    @Column(name = "senha_socio", nullable = false, length = 30)
    private String senhaSocio;

    @Column(name = "cpf_socio", length = 14, nullable = false, unique = true)
    private String cpfSocio;

    @Column(name = "is_socio_master")
    private boolean isSocioMaster;

    @ManyToMany(mappedBy = "socios")
    private List<Conteudo> conteudos;

    public Socio(long id, String nomeSocio, String telefoneSocio, String emailSocio, String senhaSocio, String cpfSocio) {
        this.id = id;
        this.nomeSocio = nomeSocio;
        this.telefoneSocio = telefoneSocio;
        this.emailSocio = emailSocio;
        this.senhaSocio = senhaSocio;
        this.cpfSocio = cpfSocio;
    }

    public Socio() {
        super();
    }

    
    public String getCpfSocio() {
        return cpfSocio;
    }

    public void setCpfSocio(String cpfSocio) {
        this.cpfSocio = cpfSocio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeSocio() {
        return nomeSocio;
    }

    public void setNomeSocio(String nomeSocio) {
        this.nomeSocio = nomeSocio;
    }

    public String getTelefoneSocio() {
        return telefoneSocio;
    }

    public void setTelefoneSocio(String telefoneSocio) {
        this.telefoneSocio = telefoneSocio;
    }

    public String getEmailSocio() {
        return emailSocio;
    }

    public void setEmailSocio(String emailSocio) {
        this.emailSocio = emailSocio;
    }

    public String getSenhaSocio() {
        return senhaSocio;
    }

    public void setSenhaSocio(String senhaSocio) {
        this.senhaSocio = senhaSocio;
    }

    public List<Conteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }

    public boolean isSocioMaster() {
        return isSocioMaster;
    }

    public void setSocioMaster(boolean isSocioMaster) {
        this.isSocioMaster = isSocioMaster;
    }
}
