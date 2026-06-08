package com.gestao.biblioteca.dao;

import com.gestao.biblioteca.model.Livro;
import com.gestao.biblioteca.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // CREATE - Inserir registro
    public void salvar(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, isbn, ano_publicacao, quantidade_total, quantidade_disponivel) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getIsbn());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setInt(4, livro.getQuantidadeTotal());
            stmt.setInt(5, livro.getQuantidadeDisponivel());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livro.setIdLivro(generatedKeys.getInt(1));
                }
            }
        }
    }

    // READ - Listar todos os registros
    public List<Livro> listarTodos() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros ORDER BY id_livro DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Livro l = new Livro(
                    rs.getInt("id_livro"),
                    rs.getString("titulo"),
                    rs.getString("isbn"),
                    rs.getInt("ano_publicacao"),
                    rs.getInt("quantidade_total"),
                    rs.getInt("quantidade_disponivel")
                );
                livros.add(l);
            }
        }
        return livros;
    }

    // UPDATE - Atualizar dados existentes
    public void atualizar(Livro livro) throws SQLException {
        String sql = "UPDATE livros SET titulo = ?, isbn = ?, ano_publicacao = ?, quantidade_total = ?, quantidade_disponivel = ? WHERE id_livro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getIsbn());
            stmt.setInt(3, livro.getAnoPublicacao());
            stmt.setInt(4, livro.getQuantidadeTotal());
            stmt.setInt(5, livro.getQuantidadeDisponivel());
            stmt.setInt(6, livro.getIdLivro());
            stmt.executeUpdate();
        }
    }

    // DELETE - Remover registro
    public void deletar(int idLivro) throws SQLException {
        String sql = "DELETE FROM livros WHERE id_livro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public Livro buscarPorId(int idLivro) throws SQLException {
        String sql = "SELECT * FROM livros WHERE id_livro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(
                            rs.getInt("id_livro"),
                            rs.getString("titulo"),
                            rs.getString("isbn"),
                            rs.getInt("ano_publicacao"),
                            rs.getInt("quantidade_total"),
                            rs.getInt("quantidade_disponivel")
                    );
                }
            }
        }
        return null;
    }

    public void ajustarQuantidadeDisponivel(int idLivro, int delta) throws SQLException {
        String sql = "UPDATE livros SET quantidade_disponivel = quantidade_disponivel + ? WHERE id_livro = ? AND quantidade_disponivel + ? >= 0";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, idLivro);
            stmt.setInt(3, delta);
            int rows = stmt.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Falha ao ajustar a quantidade disponível do livro.");
            }
        }
    }

    public List<Integer> listarAutoresPorLivro(int idLivro) throws SQLException {
        List<Integer> idsAutores = new ArrayList<>();
        String sql = "SELECT id_autor FROM livro_autor WHERE id_livro = ? ORDER BY id_autor";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    idsAutores.add(rs.getInt("id_autor"));
                }
            }
        }
        return idsAutores;
    }

    public void atualizarAutores(int idLivro, int idAutor) throws SQLException {
        String deleteSql = "DELETE FROM livro_autor WHERE id_livro = ?";
        String insertSql = "INSERT INTO livro_autor (id_livro, id_autor) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, idLivro);
                deleteStmt.executeUpdate();
            }

            if (idAutor > 0) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, idLivro);
                    insertStmt.setInt(2, idAutor);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
}
