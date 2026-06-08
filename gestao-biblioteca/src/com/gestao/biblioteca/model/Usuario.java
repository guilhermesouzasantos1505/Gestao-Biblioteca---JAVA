package com.gestao.biblioteca.model;

public class Usuario {
    private Integer idUsuario;
    private String nome;
    private String email;
    private String telefone;

    public Usuario() {}

    public Usuario(Integer idUsuario, String nome, String email, String telefone) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
