package com.temqueser.temqueser.controllers;

import com.temqueser.temqueser.models.Inscricao;
import com.temqueser.temqueser.services.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @PostMapping("/evento/{idEvento}/cliente/{idCliente}")
    public ResponseEntity<Inscricao> criarInscricao(
            @PathVariable Long idEvento,
            @PathVariable Long idCliente,
            @RequestParam(required = false) String nomeInscricao) {
        
        Inscricao inscricao = inscricaoService.criarInscricao(idEvento, idCliente, nomeInscricao);
        return ResponseEntity.ok(inscricao);
    }

    @GetMapping
    public ResponseEntity<List<Inscricao>> listarTodas() {
        return ResponseEntity.ok(inscricaoService.listarTodasInscricoes());
    }

    @GetMapping("/completas")
    public ResponseEntity<List<Inscricao>> listarCompletas() {
        return ResponseEntity.ok(inscricaoService.listarInscricoesCompletas());
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<Inscricao>> listarPorEvento(@PathVariable Long idEvento) {
        return ResponseEntity.ok(inscricaoService.listarPorEvento(idEvento));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Inscricao>> listarPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(inscricaoService.listarPorCliente(idCliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscricao> buscarPorId(@PathVariable Long id) {
        return inscricaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/completa/{id}")
    public ResponseEntity<Inscricao> buscarPorIdCompleto(@PathVariable Long id) {
        return inscricaoService.buscarPorIdCompleto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        inscricaoService.deletarInscricao(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscricao> atualizar(
            @PathVariable Long id,
            @RequestParam(required = false) String nomeInscricao,
            @RequestParam(required = false) Boolean pago) {
        
        return ResponseEntity.ok(inscricaoService.atualizarInscricao(id, nomeInscricao, pago));
    }

    @PutMapping("/{id}/atualizar-pagamento")
    public ResponseEntity<Inscricao> atualizarPagamento(
            @PathVariable Long id,
            @RequestParam boolean pago) {
        
        Inscricao inscricaoAtualizada = inscricaoService.atualizarStatusPagamento(id, pago);
        return ResponseEntity.ok(inscricaoAtualizada);
    }


}