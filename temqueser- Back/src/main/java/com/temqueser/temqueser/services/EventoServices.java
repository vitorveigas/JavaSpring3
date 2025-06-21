package com.temqueser.temqueser.services;


import com.temqueser.temqueser.models.Cliente;
import com.temqueser.temqueser.models.Evento;
import com.temqueser.temqueser.repositories.EventoRepositories;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EventoServices {
    @Autowired
    private EventoRepositories eventoRepositories;

    public Evento findEventoById(Long id){
        if(id==null){
            throw new NullPointerException("o id do evento não pode ser nulo");
        }

        return eventoRepositories.findById(id).orElseThrow(() ->
            new RuntimeException("Evento não encontrado"+id)
        );
    }
    

    //metodo de criar Evento
    
    @Transactional
    public Evento criarEvento(Evento evento){
        if(evento.getIdEvento() != null){
            throw new RuntimeException("o ID deve ser nulo para criar um conteudo.");
        }
        return eventoRepositories.save(evento);
    }

    

    @Transactional
    public Evento atualizarEvento(Evento eventoAtualizado){
        if(eventoAtualizado.getIdEvento() == null){
            throw new IllegalArgumentException("ID do evento é obrigatorio");
        }
        Evento eventoExistente = eventoRepositories.findById(eventoAtualizado.getIdEvento())
        .orElseThrow(() -> new RuntimeException("Evento não encontrado!"));

        eventoExistente.setTituloEvento(eventoAtualizado.getTituloEvento());
        eventoExistente.setImgUrlEvento(eventoAtualizado.getImgUrlEvento());
        eventoExistente.setOrganizadorEvento(eventoAtualizado.getOrganizadorEvento());
        eventoExistente.setHoraEvento(eventoAtualizado.getHoraEvento());
        eventoExistente.setDataEvento(eventoAtualizado.getDataEvento());
        eventoAtualizado.setLocalEvento(eventoAtualizado.getLocalEvento());
        eventoAtualizado.setParceiroEvento(eventoAtualizado.getParceiroEvento());
        eventoAtualizado.setPrecoEvento(eventoAtualizado.getPrecoEvento());
        eventoAtualizado.setDetalheEvento(eventoAtualizado.getDetalheEvento());

        if(eventoAtualizado.getImgUrlEvento() != null){
            eventoExistente.setImgUrlEvento(eventoAtualizado.getImgUrlEvento());
        }
            if(eventoAtualizado.getPrecoEvento()!= null){
                eventoExistente.setPrecoEvento(eventoAtualizado.getPrecoEvento());
            }
        return eventoRepositories.save(eventoExistente);
    }


// metodo de remover evento
    public void removerEvento(Long idEvento) {
        Evento evento = this.findEventoById(idEvento);

        try{
            eventoRepositories.delete(evento);
        }catch(Exception e){
            throw new RuntimeException("Erro ao deletar o evento com ID: " +idEvento);
            
        }
    }

    public List<Evento> ListarTodosEventosPorTitulo(String tituloEvento){
        return eventoRepositories.findByTituloEventoContaining(tituloEvento);
    }

  


}