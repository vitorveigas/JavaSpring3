package com.temqueser.temqueser.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cliente")
public class Cliente {

    public interface createCliente {}
    public interface updateCliente {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "email_cliente", length = 100, nullable = false)
    private String emailCliente; 


    @Column(name = "telefone_cliente", length = 12, nullable = false)
    private String telefoneCliente;

    @Column(name = "nome_cliente", length = 100, nullable = false)
    private String nomeCliente;

    @Column(name = "senha_cliente", length = 30, nullable = false)
    private String senhaCliente;

    @Column(name = "cpf_cliente", nullable = false, unique = true)
    private String cpfCliente;



    @ManyToMany(mappedBy = "clientes")
    private List<Evento> eventos = new ArrayList<>(); // Inicialize a lista




    public Cliente(Long id, String emailCliente, String telefone_cliente, String nome_cliente, String senha_cliente, String cpf_cliente) {
        this.id = id;
        this.emailCliente = emailCliente;
        this.telefoneCliente = telefone_cliente;
        this.nomeCliente = nome_cliente;
        this.senhaCliente = senha_cliente;
        this.cpfCliente = cpf_cliente;
    }

    public Cliente() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getSenhaCliente() {
        return senhaCliente;
    }

    public void setSenhaCliente(String senhaCliente) {
        this.senhaCliente = senhaCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

     public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    // Método auxiliar para adicionar evento
    public void addEvento(Evento evento) {
        this.eventos.add(evento);
        evento.getClientes().add(this);
    }

    // Método auxiliar para remover evento
    public void removeEvento(Evento evento) {
        this.eventos.remove(evento);
        evento.getClientes().remove(this);
    }
}
