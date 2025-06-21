package com.temqueser.temqueser.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "nome_inscricao", nullable = false)
    private String nomeInscricao;

    @Column(name = "pago", nullable = false)
    private boolean pago = false;

    // Construtores
    public Inscricao() {
    }

    public Inscricao(Cliente cliente, Evento evento, String nomeInscricao) {
        this.cliente = cliente;
        this.evento = evento;
        this.nomeInscricao = nomeInscricao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getNomeInscricao() {
        return nomeInscricao;
    }

    public void setNomeInscricao(String nomeInscricao) {
        this.nomeInscricao = nomeInscricao;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }
}