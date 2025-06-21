package com.temqueser.temqueser.services;

import com.temqueser.temqueser.models.Socio;
import com.temqueser.temqueser.repositories.SocioRepositories;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioServices {

    @Autowired
    private SocioRepositories socioRepositories;

    public Socio findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do sócio não pode ser nulo.");
        }

        return socioRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Sócio não encontrado com o ID: " + id));
    }

    @Transactional
    public Socio criar(Socio socio) {
        if (socio.getId() != null) {
            throw new IllegalArgumentException("O ID deve ser nulo ao criar um novo sócio.");
        }

        if (socioRepositories.existsByEmailSocio(socio.getEmailSocio())) {
            throw new IllegalArgumentException("Já existe um sócio com este e-mail.");
        }

        return socioRepositories.save(socio);
    }

    @Transactional
    public Socio atualizar(Socio socio) {
        if (socio.getId() == null) {
            throw new IllegalArgumentException("O ID do sócio não pode ser nulo ao atualizar.");
        }

        Socio socioExistente = this.findById(socio.getId());
        socioExistente.setSenhaSocio(socio.getSenhaSocio());
        socioExistente.setEmailSocio(socio.getEmailSocio());
        socioExistente.setTelefoneSocio(socio.getTelefoneSocio());
        socioExistente.setNomeSocio(socio.getNomeSocio());

        return socioRepositories.save(socioExistente);
    }

    public void remover(Long id) {
        Socio socio = this.findById(id);

        try {
            socioRepositories.delete(socio);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar deletar o sócio com ID: " + id);
        }
    }

    public Socio autenticar(String email, String senha) {
        Socio socio = socioRepositories.findByEmailSocio(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o e-mail: " + email));

        if (!socio.getSenhaSocio().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return socio;
    }

    public List<Socio> findAll() {
        return socioRepositories.findAll();
    }
    
}
