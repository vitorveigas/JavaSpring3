package com.temqueser.temqueser.repositories;

import com.temqueser.temqueser.models.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConteudoRepositories extends JpaRepository<Conteudo,Long> {
    boolean existsById(Long id);
    List<Conteudo> findByTituloConteudoContaining(String tituloConteudo);
}
