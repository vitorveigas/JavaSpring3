package com.temqueser.temqueser.repositories;

import com.temqueser.temqueser.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClienteRepositories extends JpaRepository<Cliente, Long> {
    // Todos os métodos já disponíveis
    Optional<Cliente> findByEmailCliente(String emailCliente);
    boolean existsByEmailCliente(String emailCliente);
}
