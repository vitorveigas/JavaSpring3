package com.temqueser.temqueser.controllers;

import com.temqueser.temqueser.models.Socio;
import com.temqueser.temqueser.repositories.SocioRepositories;
import com.temqueser.temqueser.services.JwtService;
import com.temqueser.temqueser.services.SocioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/socios")
@CrossOrigin(origins = "*")
public class SocioController {

    @Autowired
    private SocioServices socioServices;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SocioRepositories socioRepositories;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Socio loginData) {
        Optional<Socio> socioOptional = socioRepositories.findByEmailSocio(loginData.getEmailSocio());

        if (socioOptional.isPresent()) {
            Socio socio = socioOptional.get();

            if (socio.getSenhaSocio().equals(loginData.getSenhaSocio())) {
                String token = jwtService.generateTokenSocio(socio);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("nome", socio.getNomeSocio());

                return ResponseEntity.ok().body(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

   
    

    @GetMapping("/me")
    public ResponseEntity<Socio> getPerfil(@RequestHeader("Authorization") String token) {
        Long socioId = jwtService.extractUserId(token.replace("Bearer ", ""));
        Socio socio = socioServices.findById(socioId);
        return ResponseEntity.ok().body(socio);
    }

    @PutMapping("/editarSocio")
    public ResponseEntity<?> editar(@RequestHeader("Authorization") String token, @RequestBody Socio socioAtualizado) {
        Long socioId = jwtService.extractUserId(token.replace("Bearer ", ""));
        socioAtualizado.setId(socioId);
        this.socioServices.atualizar(socioAtualizado);
        return ResponseEntity.ok().body("socio atualizado com sucesso");
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deletar(@RequestHeader("Authorization") String token) {
        Long socioId = jwtService.extractUserId(token.replace("Bearer ", ""));
        socioServices.remover(socioId);
        return ResponseEntity.ok().body("socio deletado com sucesso");
    }

    @PostMapping("/cadastrarSocio")
    public ResponseEntity<?> cadastrar(@RequestBody Socio socio){
        try {
            Socio novoSocio = socioServices.criar(socio);
            URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(novoSocio.getId())
            .toUri();
            return ResponseEntity.created(location).body(novoSocio);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar o socío");
        }
    }

    @GetMapping("/todosSocios")
public ResponseEntity<List<Socio>> listarTodos() {
    List<Socio> socios = socioServices.findAll();
    return ResponseEntity.ok(socios);
}

@DeleteMapping("/{id}")
public ResponseEntity<?> deletarPorId(@PathVariable Long id) {
    socioServices.remover(id);
    return ResponseEntity.ok().body("Sócio deletado com sucesso");
}


}
