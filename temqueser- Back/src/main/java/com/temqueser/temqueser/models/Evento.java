package com.temqueser.temqueser.models;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "Evento")
public class Evento {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long idEvento;

@Column(name = "titulo_evento",length = 150,nullable = false,updatable = true) //permitir alterações
    private String tituloEvento;

@Column(name = "detalhe_evento", length = 300, nullable = false,updatable = true)
    private String detalheEvento;

@Column(name = "data_evento",length = 100,nullable = false, updatable = true)
    private LocalDate dataEvento;

@Column(name = "organizador_evento",length = 100, nullable = false,updatable = true)
    private String organizadorEvento;
    
@Column(name = "parceiro_evento",length = 100, nullable = true,updatable = true)
    private String parceiroEvento;   

@Column(name = "local_evento",length = 100, nullable = true,updatable = true)
    private String localEvento;   

@Column(name = "hora_evento", length = 6, nullable = false,updatable = true)
    private LocalTime horaEvento;

 @Column(name = "img_url_evento",nullable = true) //garante que conteudo será criado sem uma imagemx'
    private String imgUrlEvento;

@Column(name = "preco_evento", nullable = true)
private Double precoEvento;



@ManyToMany
@JoinTable(
    name = "evento_socio",
    joinColumns = @JoinColumn(name = "evento_id"),
    inverseJoinColumns = @JoinColumn(name = "socio_id")
)
private List<Socio> socios;

@ManyToMany
@JoinTable(
    name = "evento_cliente",
    joinColumns = @JoinColumn(name = "evento_id"),
    inverseJoinColumns = @JoinColumn(name = "cliente_id")
)
 private List<Cliente> clientes = new ArrayList<>();





    


    public Evento(String tituloEvento,String detalheEvento,long idEvento, String organizadorEvento, String parceiroEvento, String localEvento, LocalTime horaEvento, String imgUrlEvento, Double precoEvento ){
        this.tituloEvento = tituloEvento;
        this.detalheEvento = detalheEvento;
        this.idEvento = idEvento;
        this.organizadorEvento = organizadorEvento;
        this.parceiroEvento = parceiroEvento;
        this.localEvento = localEvento;
        this.horaEvento = horaEvento;
        this.imgUrlEvento = imgUrlEvento;
        this.precoEvento = precoEvento;
    }

    public Evento(){
        super();
    }


public Long getIdEvento() {
    return idEvento;
}


public void setIdEvento(Long idEvento) {
    this.idEvento = idEvento;
}


public Double getPrecoEvento() {
    return precoEvento;
}

public void setPrecoEvento(Double precoEvento) {
    this.precoEvento = precoEvento;
}

public String getTituloEvento() {
    return tituloEvento;
}






public void setTituloEvento(String tituloEvento) {
    this.tituloEvento = tituloEvento;
}


public LocalDate getDataEvento() {
    return dataEvento;
}


public void setDataEvento(LocalDate dataEvento) {
    this.dataEvento = dataEvento;
}


public String getOrganizadorEvento() {
    return organizadorEvento;
}


public void setOrganizadorEvento(String organizadorEvento) {
    this.organizadorEvento = organizadorEvento;
}


public String getParceiroEvento() {
    return parceiroEvento;
}


public void setParceiroEvento(String parceiroEvento) {
    this.parceiroEvento = parceiroEvento;
}


public String getLocalEvento() {
    return localEvento;
}


public void setLocalEvento(String localEvento) {
    this.localEvento = localEvento;
}


public LocalTime getHoraEvento() {
    return horaEvento;
}


public void setHoraEvento(LocalTime horaEvento) {
    this.horaEvento = horaEvento;
}


public String getImgUrlEvento() {
    return imgUrlEvento;
}


public void setImgUrlEvento(String imgUrlEvento) {
    this.imgUrlEvento = imgUrlEvento;
}


public List<Socio> getSocios() {
    return socios;
}


public void setSocios(List<Socio> socios) {
    this.socios = socios;
}

public String getDetalheEvento() {
    return detalheEvento;
}

public void setDetalheEvento(String detalheEvento) {
    this.detalheEvento = detalheEvento;
}
  public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    
}