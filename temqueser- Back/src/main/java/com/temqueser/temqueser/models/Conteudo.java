package com.temqueser.temqueser.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Conteudo")
public class Conteudo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConteudo;

    @Column(name = "titulo_conteudo",length = 150,nullable = false,updatable = true)// permitir altera
    private String tituloConteudo;

    @Column(name = "descricao_conteudo",length = 5000,nullable = false,updatable = true)//permitir alterar
    private String descricaoConteudo;

    @Column(name="data_conteudo",nullable = false,updatable = true)
    private LocalDate dataConteudo;

    @Column(name = "img_url_conteudo",nullable = true) //garante que conteudo será criado sem uma imagemx'
    private String imgUrlConteudo;

    @ManyToMany
    @JoinTable(
            name = "conteudo_socio",
            joinColumns = @JoinColumn(name = "conteudo_id"),
            inverseJoinColumns = @JoinColumn(name = "socio_id")
    )
    private List<Socio> socios;

    public Conteudo(LocalDate dataConteudo, String descricaoConteudo, long idConteudo, String tituloConteudo, String imgUrlConteudo) {
        this.dataConteudo = dataConteudo;
        this.descricaoConteudo = descricaoConteudo;
        this.idConteudo = idConteudo;
        this.tituloConteudo = tituloConteudo;
        this.imgUrlConteudo = imgUrlConteudo;
    }

    public Conteudo(){
        super();
    }

    public Long getIdConteudo() {
        return idConteudo;
    }

    public void setIdConteudo(Long idConteudo) {
        this.idConteudo = idConteudo;
    }

    public String getTituloConteudo() {
        return tituloConteudo;
    }

    public void setTituloConteudo(String tituloConteudo) {
        this.tituloConteudo = tituloConteudo;
    }

    public String getDescricaoConteudo() {
        return descricaoConteudo;
    }

    public void setDescricaoConteudo(String descricaoConteudo) {
        this.descricaoConteudo = descricaoConteudo;
    }

    public LocalDate getDataConteudo() {
        return dataConteudo;
    }

    public void setDataConteudo(LocalDate dataConteudo) {
        this.dataConteudo = dataConteudo;
    }

    public String getImgUrlConteudo() {
        return imgUrlConteudo;
    }

    public void setImgUrlConteudo(String imgUrlConteudo) {
        this.imgUrlConteudo = imgUrlConteudo;
    }

    public List<Socio> getSocios() {
        return socios;
    }

    public void setSocios(List<Socio> socios) {
        this.socios = socios;
    }
}
