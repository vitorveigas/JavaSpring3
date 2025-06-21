package com.temqueser.temqueser.controllers;
import com.temqueser.temqueser.models.SocioMaster;
import com.temqueser.temqueser.repositories.SocioMasterRepositories;
import com.temqueser.temqueser.services.SocioMasterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.temqueser.temqueser.services.JwtService;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/sociosMaster")
@CrossOrigin(origins = "*")
public class SocioMasterController{
    @Autowired
    private SocioMasterServices socioMasterServices;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SocioMasterRepositories socioMasterRepositories;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SocioMaster loginData) {
        Optional<SocioMaster> socioMasterOptional = socioMasterRepositories.findByEmailSocioMaster(loginData.getEmailSocioMaster());
    
    
        if (socioMasterOptional.isPresent()) {
            SocioMaster socioMaster = socioMasterOptional.get();
         
            if(socioMaster.getSenhaSocioMaster().equals(loginData.getSenhaSocioMaster())) {
                String token = jwtService.generateTokenSocioMaster(socioMaster);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("nome", socioMaster.getNomeSocioMaster());
            
                return ResponseEntity.ok().body(response);
         }
    }
    
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }




    @GetMapping("/me")
    public ResponseEntity<SocioMaster> getPerfil(@RequestHeader("Authorization") String token) {
        Long socioMasterID = jwtService.extractUserId(token.replace("Bearer ", ""));
        SocioMaster socioMaster = socioMasterServices.findById(socioMasterID);
        return ResponseEntity.ok().body(socioMaster);
    }

    @PutMapping("/editarSocioMaster")
    public ResponseEntity<?> editar(@RequestHeader("Authorization") String token, @RequestBody SocioMaster socioMasterAtualizado) {
        Long socioMasterId = jwtService.extractUserId(token.replace("Bearer ", ""));
        socioMasterAtualizado.setId(socioMasterId);
        this.socioMasterServices.atualizar(socioMasterAtualizado);
        return ResponseEntity.ok().body("socio master atualizado com sucesso");
    }
    
    @DeleteMapping("/me")
    public ResponseEntity<?> deletar(@RequestHeader("Authorization") String token) {
        Long socioMasterId = jwtService.extractUserId(token.replace("Bearer ", ""));
        socioMasterServices.remover(socioMasterId);
        return ResponseEntity.ok().body("sócio master deletado com sucesso");
    }
}