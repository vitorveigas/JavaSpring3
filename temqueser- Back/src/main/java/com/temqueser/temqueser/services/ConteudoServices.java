package com.temqueser.temqueser.services;

import com.temqueser.temqueser.models.Conteudo;
import com.temqueser.temqueser.repositories.ConteudoRepositories;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConteudoServices {
    @Autowired
    private ConteudoRepositories conteudoRepositories;

    public Conteudo findConteudoById(Long id) {
        if(id==null){
            throw new NullPointerException("o id do conteudo não pode ser nulo.");
        }

        return conteudoRepositories.findById(id).orElseThrow(() ->
                new RuntimeException("Conteudo não encontrado"+id));
    }
    //metodo de criar Conteudo
    @Transactional
    public Conteudo criarConteudo(Conteudo conteudo) {
        if(conteudo.getIdConteudo() != null){
            throw new RuntimeException("o ID deve ser nulo para criar um novo conteudo.");
        }
        return conteudoRepositories.save(conteudo);
    }

    //metodo de atualizar conteudo
    @Transactional
    public Conteudo atualizarConteudo(Conteudo conteudoAtualizado) {
        // Validação obrigatória
        if (conteudoAtualizado.getIdConteudo() == null) {
            throw new IllegalArgumentException("ID do conteúdo é obrigatório");
        }
    
        // Busca o conteúdo existente
        Conteudo conteudoExistente = conteudoRepositories.findById(conteudoAtualizado.getIdConteudo())
                .orElseThrow(() -> new RuntimeException("Conteúdo não encontrado"));
    
        // Atualiza APENAS os campos permitidos (nunca o ID)
        conteudoExistente.setTituloConteudo(conteudoAtualizado.getTituloConteudo());
        conteudoExistente.setDescricaoConteudo(conteudoAtualizado.getDescricaoConteudo());
        conteudoExistente.setDataConteudo(conteudoAtualizado.getDataConteudo());
        
        // Campo opcional
        if (conteudoAtualizado.getImgUrlConteudo() != null) {
            conteudoExistente.setImgUrlConteudo(conteudoAtualizado.getImgUrlConteudo());
        }
    
        return conteudoRepositories.save(conteudoExistente);
    }
    //metodo de remover conteudo
    public void removerConteudo(Long idConteudo) {
        Conteudo conteudo = this.findConteudoById(idConteudo);

        try{
            conteudoRepositories.delete(conteudo);
        }catch(Exception e){
            throw new RuntimeException("Erro ao deletar o conteudo com o ID: "+idConteudo);
        }

    }

    //metodo de listar todos os conteudos por titulo
    public List<Conteudo> ListarTodosConteudosPorTitulo(String tituloConteudo) {
        return conteudoRepositories.findByTituloConteudoContaining(tituloConteudo);
    }
}
