package com.temqueser.temqueser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.temqueser.temqueser.models.SocioMaster;
import com.temqueser.temqueser.repositories.SocioMasterRepositories;
import com.temqueser.temqueser.repositories.SocioRepositories;

@Service
public class SocioMasterServices{

    @Autowired
    private SocioMasterRepositories socioMasterRepositories;


    public SocioMaster findById(Long id){
        if (id == null){
            throw new IllegalArgumentException("O ID do sócio master não pode ser nulo");
        }
        return socioMasterRepositories.findById(id)
        .orElseThrow(()-> new RuntimeException("Sócio Master não encontrado com o ID: " + id));
    }

    @Transactional
    public SocioMaster criar(SocioMaster socioMaster){
        if (socioMaster.getId() != null) {
            throw new IllegalArgumentException("O ID deve ser nulo ao criar um novo sócio.");
        }
        
        if (socioMasterRepositories.existsByEmailSocioMaster(socioMaster.getEmailSocioMaster())){
            throw new IllegalArgumentException("Já existe um sócio master com este e-mail");
        }
        
        return socioMasterRepositories.save(socioMaster);
    }



    @Transactional
    public SocioMaster atualizar(SocioMaster socioMaster){
        if(socioMaster.getId() == null){
            throw new IllegalArgumentException("O ID dos socio master não pode ser nulo ao atualizar.");
        }

        SocioMaster socioMasterExistente = this.findById(socioMaster.getId());
        socioMasterExistente.setSenhaSocioMaster(socioMaster.getSenhaSocioMaster());
        socioMasterExistente.setEmailSocioMaster(socioMaster.getEmailSocioMaster());
        socioMasterExistente.setTelefoneSocioMaster(socioMaster.getTelefoneSocioMaster());
        socioMasterExistente.setNomeSocioMaster(socioMaster.getNomeSocioMaster());
        
        return socioMasterRepositories.save(socioMasterExistente);
    }


    public void remover(Long id){
        SocioMaster socioMaster = this.findById(id);
    

        try {
            socioMasterRepositories.delete(socioMaster);
        } catch (Exception e){
            throw new RuntimeException("Erro ao tentar deletar o sócio master com ID: " + id);
        }
    }


    public SocioMaster autenticar(String email, String senha){
        SocioMaster socioMaster = socioMasterRepositories.findByEmailSocioMaster(email)
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o e-mail: " + email));
                
    
        if (!socioMaster.getSenhaSocioMaster().equals(senha)){
            throw new IllegalArgumentException("Senha incorreta.");
        }
        
        return socioMaster;
    }


}