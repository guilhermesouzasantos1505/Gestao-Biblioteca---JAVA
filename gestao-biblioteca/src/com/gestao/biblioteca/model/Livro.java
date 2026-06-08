package com.gestao.biblioteca.model;

public class Livro {
    private Integer idLivro;
    private String titulo;
    private String isbn;
    private int anoPublicacao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;

    public Livro() {}

    public Livro(Integer idLivro, String titulo, String isbn, int anoPublicacao, int quantidadeTotal, int quantidadeDisponivel) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    // Getters e Setters
    public Integer getIdLivro() { return idLivro; }
    public void setIdLivro(Integer idLivro) { this.idLivro = idLivro; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(int quantityTotal) { this.quantidadeTotal = quantityTotal; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(int quantidadeDisponivel) { this.quantidadeDisponivel = quantidadeDisponivel; }

    @Override
    public String toString() { 
        return this.titulo; 
    }
}