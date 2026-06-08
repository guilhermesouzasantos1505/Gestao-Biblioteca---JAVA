package com.gestao.biblioteca.model;

import java.sql.Date;

public class Emprestimo {
    private Integer idEmprestimo;
    private Integer idUsuario;
    private Integer idLivro;
    private Date dataEmprestimo;
    private Date dataDevolucaoPrevista;
    private String status;
    private String nomeUsuario;
    private String tituloLivro;

    public Emprestimo() {}

    public Emprestimo(Integer idEmprestimo, Integer idUsuario, Integer idLivro, Date dataEmprestimo, Date dataDevolucaoPrevista, String status) {
        this.idEmprestimo = idEmprestimo;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.status = status;
    }

    public Integer getIdEmprestimo() { return idEmprestimo; }
    public void setIdEmprestimo(Integer idEmprestimo) { this.idEmprestimo = idEmprestimo; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdLivro() { return idLivro; }
    public void setIdLivro(Integer idLivro) { this.idLivro = idLivro; }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(Date dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public Date getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(Date dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public String getTituloLivro() { return tituloLivro; }
    public void setTituloLivro(String tituloLivro) { this.tituloLivro = tituloLivro; }
}
