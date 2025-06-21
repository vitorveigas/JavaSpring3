    package com.temqueser.temqueser.controllers;

    import com.temqueser.temqueser.models.Cliente;
    import com.temqueser.temqueser.repositories.ClienteRepositories;
    import com.temqueser.temqueser.services.ClienteServices;
    import com.temqueser.temqueser.services.JwtService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

    import java.util.Optional;
    import java.util.HashMap;
    import java.util.Map;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;

    import java.net.URI;
    import java.util.HashMap;
    import java.util.Optional;

    import static com.fasterxml.jackson.databind.type.LogicalType.Map;

    @RestController
    @RequestMapping("/clientes")
    @CrossOrigin(origins = "*") //colocar a porta do front
    public class ClienteController {
        @Autowired
        private ClienteServices clienteServices;

        @Autowired
        private JwtService jwtService;
        @Autowired
        private ClienteRepositories clienteRepositories;

        @GetMapping("/me")
        public ResponseEntity<Cliente> findById(@RequestHeader("Authorization")String token) {
            Long userId = jwtService.extractUserId(token.replace("Bearer ", ""));
            Cliente cliente = clienteServices.findById(userId);
            return ResponseEntity.ok().body(cliente);
        }

        @PutMapping("/editar")
        public ResponseEntity<?> atualizar(@RequestHeader("Authorization") String token, @RequestBody Cliente cliente) {
            Long userId = jwtService.extractUserId(token.replace("Bearer ",""));
            cliente.setId(userId);
            this.clienteServices.atualizar(cliente);
            return ResponseEntity.ok().body("cliente atualizado com sucesso");
        }

        @DeleteMapping("/me")
        public ResponseEntity<?> deletar(@RequestHeader("Authorization")String token) {
            Long userId = jwtService.extractUserId(token.replace("Bearer ",""));
            clienteServices.remover(userId);
            return ResponseEntity.ok().body("cliente excluido com sucesso");
        }

        @PostMapping("/me")
        public ResponseEntity<?> login(@RequestBody Cliente loginData) {
            Optional<Cliente> clienteOptional = clienteRepositories.findByEmailCliente(loginData.getEmailCliente());

            if (clienteOptional.isPresent()) {
                Cliente cliente = clienteOptional.get();
                if (cliente.getSenhaCliente().equals(loginData.getSenhaCliente())) {
                    String token = jwtService.generateToken(cliente);

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("nome", cliente.getNomeCliente());

                    return ResponseEntity.ok().body(response);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }



        @PostMapping("/cadastrar")
        public ResponseEntity<?> cadastrar(@RequestBody Cliente cliente) {
            try {
                Cliente novoCliente = clienteServices.criar(cliente);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(novoCliente.getId())
                        .toUri();
                return ResponseEntity.created(location).body(novoCliente);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar o cliente.");
            }
        }

    }
