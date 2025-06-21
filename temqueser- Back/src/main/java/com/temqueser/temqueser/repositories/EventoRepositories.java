package com.temqueser.temqueser.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.temqueser.temqueser.models.Evento;
@Repository
public interface EventoRepositories extends JpaRepository<Evento,Long> {
    boolean existsById(Long id);
    List<Evento> findByTituloEventoContaining(String tituloEvento);

    @Query("SELECT e FROM Evento e JOIN e.clientes c WHERE c.id = :clienteId")
    List<Evento> findEventosByClienteId(@Param("clienteId") Long clienteId);
}
