package com.temqueser.temqueser.services;

import com.temqueser.temqueser.models.Cliente;
import com.temqueser.temqueser.repositories.ClienteRepositories;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServices {

    @Autowired
    private ClienteRepositories clienteRepositories;


    public Cliente findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do cliente não pode ser nulo.");
        }

        return clienteRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }


    @Transactional
    public Cliente criar(Cliente cliente) {
        if (cliente.getId() != null) {
            throw new IllegalArgumentException("O ID deve ser nulo ao criar um novo cliente.");
        }
        if(clienteRepositories.existsByEmailCliente(cliente.getEmailCliente())){
            throw new IllegalArgumentException("Já existe um cliente com este email");
        }

        return clienteRepositories.save(cliente);
    }


    @Transactional
    public Cliente atualizar(Cliente cliente) {
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("O ID do cliente não pode ser nulo ao atualizar.");
        }

        Cliente clienteExistente = this.findById(cliente.getId());
        clienteExistente.setSenhaCliente(cliente.getSenhaCliente());
        clienteExistente.setEmailCliente(cliente.getEmailCliente());
        clienteExistente.setNomeCliente(cliente.getNomeCliente());
        clienteExistente.setTelefoneCliente(cliente.getTelefoneCliente());

        return this.clienteRepositories.save(clienteExistente);
    }



    public void remover(Long id) {
        Cliente cliente = this.findById(id);

        try {
            clienteRepositories.delete(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar deletar o cliente com ID: " + id);
        }
    }

    //sem @Transactional pois não faz alteração ao Banco de Dados
    public Cliente autenticar(String email, String senha) {
        Cliente cliente = clienteRepositories.findByEmailCliente(email).orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado com o email: " + email));

        if(!cliente.getSenhaCliente().equals(senha)){
            throw new IllegalArgumentException("Senha incorreta.");
        }
        return cliente;
    }

}
