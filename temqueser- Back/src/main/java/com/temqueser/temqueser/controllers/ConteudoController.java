package com.temqueser.temqueser.controllers;

import com.temqueser.temqueser.models.Conteudo;
import com.temqueser.temqueser.repositories.ConteudoRepositories;
import com.temqueser.temqueser.services.ClienteServices;
import com.temqueser.temqueser.services.ConteudoServices;
import com.temqueser.temqueser.services.JwtService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conteudos")
@CrossOrigin(origins = "*") //colocar porta do front
public class ConteudoController {

    @Autowired
    private ConteudoServices conteudoServices;

    @Autowired
    private ConteudoRepositories conteudoRepositories;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ClienteServices clienteServices;

    //metodo para encontrar o ID de conteudo em controller
    @GetMapping("/{idConteudo}")
    public ResponseEntity<Conteudo> findByIdConteudo(@PathVariable Long idConteudo) {
        Conteudo conteudo = conteudoServices.findConteudoById(idConteudo);
        return ResponseEntity.ok().body(conteudo);
    }

    //metodo de atualizar conteudo, somente socios
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarConteudo(
    @RequestBody Conteudo conteudoAtualizado, 
    @PathVariable Long id,
    @RequestHeader("Authorization") String token) {
    
    // Validação do token
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
        Conteudo conteudoAtualizadoComId = new Conteudo();
        BeanUtils.copyProperties(conteudoAtualizado, conteudoAtualizadoComId);
        conteudoAtualizadoComId.setIdConteudo(id); // Força o ID do path
        
        Conteudo conteudo = conteudoServices.atualizarConteudo(conteudoAtualizadoComId);
        return ResponseEntity.ok("Conteúdo atualizado com sucesso");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } 
}
    // metodo para deletar conteudo
    @DeleteMapping("/excluirConteudo/{idConteudo}")
    public ResponseEntity<String> deletarConteudo(@RequestHeader("Authorization") String token,@PathVariable Long idConteudo) {
        String tokenLimpo = token.replace("Bearer ", "");
        if(!jwtService.isTokenValid(tokenLimpo)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalido");
        }
        String tipoAcesso = jwtService.extractTipoUsuario(tokenLimpo);
        if("cliente".equals(tipoAcesso)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente socios podem deletar o conteudo");
        }
        conteudoServices.removerConteudo(idConteudo);
        return ResponseEntity.ok("Conteudo deletado com sucesso");
    }
    //metodo para criar conteudo, verifica se é socio
    @PostMapping("")
    public ResponseEntity<?> criarConteudo( @RequestBody Conteudo conteudo,@RequestHeader("Authorization") String token) {
        String tokenLimpo = token.replace("Bearer ", "");
        if(!jwtService.isTokenValid(tokenLimpo)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalido");
        }
        String tipoAcesso = jwtService.extractTipoUsuario(tokenLimpo);
        if("cliente".equals(tipoAcesso)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente socios podem criar conteudos");
        }
        try{
            Conteudo conteudoNovo = conteudoServices.criarConteudo(conteudo);
            return ResponseEntity.status(HttpStatus.CREATED).body(conteudoNovo);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método para listar todos os conteúdos
@GetMapping("")
public ResponseEntity<?> listarConteudos(@RequestHeader("Authorization") String token) {
    String tokenLimpo = token.replace("Bearer ", "");
    if (!jwtService.isTokenValid(tokenLimpo)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Invalido");
    }
    // tirar para verificar se é socio para listar conteudos

    // Aqui você retorna todos os conteúdos do banco
    try {
        return ResponseEntity.ok(conteudoRepositories.findAll()); // Vai retornar todos os conteúdos
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar conteúdos");
    }
}

}
