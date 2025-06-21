package com.temqueser.temqueser.services;

import com.temqueser.temqueser.models.*;
import com.temqueser.temqueser.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepositories inscricaoRepository;

    @Autowired
    private EventoRepositories eventoRepository;

    @Autowired
    private ClienteRepositories clienteRepository;

    @Transactional
    public Inscricao criarInscricao(Long idEvento, Long idCliente, String nomeInscricao) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verifica se já está inscrito
        if (!inscricaoRepository.findByClienteIdAndEventoIdEvento(idCliente, idEvento).isEmpty()) {
            throw new RuntimeException("Cliente já inscrito neste evento");
        }

        Inscricao inscricao = new Inscricao(cliente, evento, 
                nomeInscricao != null ? nomeInscricao : "Inscrição de " + cliente.getNomeCliente());

        return inscricaoRepository.save(inscricao);
    }

    public List<Inscricao> listarTodasInscricoes() {
        return inscricaoRepository.findAll();
    }

    public List<Inscricao> listarInscricoesCompletas() {
        return inscricaoRepository.findAllWithClienteAndEvento();
    }

    public List<Inscricao> listarPorEvento(Long idEvento) {
        return inscricaoRepository.findByEventoIdEvento(idEvento);
    }

    public List<Inscricao> listarPorCliente(Long idCliente) {
        return inscricaoRepository.findByClienteId(idCliente);
    }

    public Optional<Inscricao> buscarPorId(Long id) {
        return inscricaoRepository.findById(id);
    }

    public Optional<Inscricao> buscarPorIdCompleto(Long id) {
        return inscricaoRepository.findByIdWithClienteAndEvento(id);
    }

    @Transactional
    public void deletarInscricao(Long id) {
        inscricaoRepository.deleteById(id);
    }

    @Transactional
    public Inscricao atualizarInscricao(Long id, String nomeInscricao, Boolean pago) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));

        if (nomeInscricao != null) {
            inscricao.setNomeInscricao(nomeInscricao);
        }
        if (pago != null) {
            inscricao.setPago(pago);
        }

        return inscricaoRepository.save(inscricao);
    }

    @Transactional
    public Inscricao atualizarStatusPagamento(Long id, boolean pago) {
        Inscricao inscricao = inscricaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        
        inscricao.setPago(pago);
        return inscricaoRepository.save(inscricao);
    }
}