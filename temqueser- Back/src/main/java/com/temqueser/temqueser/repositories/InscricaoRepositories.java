package com.temqueser.temqueser.repositories;

import com.temqueser.temqueser.models.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InscricaoRepositories extends JpaRepository<Inscricao, Long> {

    // Busca por ID do evento (usando idEvento)
    List<Inscricao> findByEventoIdEvento(Long idEvento);

    // Busca por ID do cliente
    List<Inscricao> findByClienteId(Long clienteId);

    // Busca específica por cliente e evento
    List<Inscricao> findByClienteIdAndEventoIdEvento(Long clienteId, Long eventoId);

    // Busca todas com relacionamentos (otimizado)
    @Query("SELECT i FROM Inscricao i JOIN FETCH i.cliente JOIN FETCH i.evento")
    List<Inscricao> findAllWithClienteAndEvento();

    // Busca por ID com relacionamentos (otimizado)
    @Query("SELECT i FROM Inscricao i JOIN FETCH i.cliente JOIN FETCH i.evento WHERE i.id = :id")
    Optional<Inscricao> findByIdWithClienteAndEvento(@Param("id") Long id);
}