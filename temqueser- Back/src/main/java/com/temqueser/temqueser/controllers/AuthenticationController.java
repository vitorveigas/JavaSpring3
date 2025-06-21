package com.temqueser.temqueser.controllers;

import com.temqueser.temqueser.models.Cliente;
import com.temqueser.temqueser.models.Socio;
import com.temqueser.temqueser.models.SocioMaster;
import com.temqueser.temqueser.services.ClienteServices;
import com.temqueser.temqueser.services.JwtService;
import com.temqueser.temqueser.services.SocioServices;
import com.temqueser.temqueser.services.SocioMasterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private ClienteServices clienteServices;

    @Autowired
    private SocioServices socioServices;

    @Autowired
    private SocioMasterServices socioMasterServices;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Cliente credenciais) {
        try{
            Cliente cliente = clienteServices.autenticar(credenciais.getEmailCliente(), credenciais.getSenhaCliente());
            String token = jwtService.generateToken(cliente);
            return ResponseEntity.ok().body(token);
        }catch(Exception e){
            return ResponseEntity.status(401).body("Credenciais incorretas");
        }
    }

    @PostMapping("/login-socio")
    public ResponseEntity<?> loginSocio(@RequestBody Socio credenciais) {
        try {
            Socio socio = socioServices.autenticar(credenciais.getEmailSocio(), credenciais.getSenhaSocio());
            String token = jwtService.generateTokenSocio(socio);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais de sócio incorretas");
        }
    }
    
    @PostMapping("/login-socioMaster")
    public ResponseEntity<?> loginSocioMaster(@RequestBody SocioMaster credenciais) {
        try {
            SocioMaster socioMaster = socioMasterServices.autenticar(credenciais.getEmailSocioMaster(), credenciais.getSenhaSocioMaster());
            String token = jwtService.generateTokenSocioMaster(socioMaster);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais de sócio master incorretas");
        }
        
    }
    
}
