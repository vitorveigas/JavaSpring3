package com.temqueser.temqueser.controllers;

import com.temqueser.temqueser.models.Evento;
import com.temqueser.temqueser.repositories.EventoRepositories;
import com.temqueser.temqueser.services.EventoServices;
import com.temqueser.temqueser.services.ClienteServices;
import com.temqueser.temqueser.services.JwtService;

import org.hibernate.mapping.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*") //colocar porta do front se necessario

public class EventoController {

   
// Causa provável: Injeção de dependência faltando
@Autowired
private ClienteServices clienteService; // Adicionar esta linha

    @Autowired
    private EventoServices eventoServices;

    @Autowired
    private EventoRepositories eventoRepositories;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private ClienteServices clienteServices;

    @GetMapping("/{idEvento}")
    public ResponseEntity<Evento> findByIdEvento(@PathVariable Long idEvento) {
        Evento evento = eventoServices.findEventoById(idEvento);
        return ResponseEntity.ok().body(evento);
    }
    
    // atualizar evento, somente socios
   @PutMapping("/{id}") 
   public ResponseEntity<String> atualizarEvento(
    @RequestBody Evento eventoAtualizado,
    @PathVariable Long id,
    @RequestHeader("Authorization")String token){
        
        //Validação do token
        String tokenLimpo = token.replace("Bearer ", "");
        if(!jwtService.isTokenValid(tokenLimpo)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Inválido");
        }
         // Verifica se é sócio
        if("cliente".equals(jwtService.extractTipoUsuario(tokenLimpo))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente sócios podem atualizar conteúdo");
        }

        try {
        // Garante que o ID do path é usado, ignorando qualquer ID no body
            Evento eventoAtualizadoComId = new Evento();
            BeanUtils.copyProperties(eventoAtualizado, eventoAtualizadoComId);
            eventoAtualizadoComId.setIdEvento(id); // Força o ID do path
        
            Evento evento = eventoServices.atualizarEvento(eventoAtualizadoComId);
         return ResponseEntity.ok("Evento atualizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
   


        @DeleteMapping("/excluirEvento/{idEvento}")
        public ResponseEntity<String> deletarEvento(@RequestHeader("Authorization") String token,@PathVariable Long idEvento){
            String tokenLimpo = token.replace("Bearer ", "");
            if(!jwtService.isTokenValid(tokenLimpo)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("token Invalido");
            }
            String tipoAcesso = jwtService.extractTipoUsuario(tokenLimpo);
            if("cliente".equals(tipoAcesso)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente socios podem deletar um evento");
            }
            eventoServices.removerEvento(idEvento);
            return ResponseEntity.ok("evento deletado com sucesso");
        }
        //método pra criar evento verifica se é sócio
        @PostMapping("")
        public ResponseEntity<?> criarEvento ( @RequestBody Evento evento,@RequestHeader("Authorization") String token){
              String tokenLimpo = token.replace("Bearer ", "");
        if(!jwtService.isTokenValid(tokenLimpo)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalido");
        }
        String tipoAcesso = jwtService.extractTipoUsuario(tokenLimpo);
        if("cliente".equals(tipoAcesso)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente socios podem criar evento");
        }
        try{
            Evento eventoNovo = eventoServices.criarEvento(evento);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventoNovo);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
            
        }
        }

        //Listar eventos

        @GetMapping("")
        public ResponseEntity<?> listarEventos(@RequestHeader("Authorization") String token){

           
            String tokenLimpo = token.replace("Bearer ", "");
    if (!jwtService.isTokenValid(tokenLimpo)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalido");
    }
    // tirar para verificar se é socio para listar conteudos

    // Aqui você retorna todos os conteúdos do banco
    try {
        return ResponseEntity.ok(eventoRepositories.findAll()); // Vai retornar todos os conteúdos
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar conteúdos");
    }
        }

   

   


   
    
}
