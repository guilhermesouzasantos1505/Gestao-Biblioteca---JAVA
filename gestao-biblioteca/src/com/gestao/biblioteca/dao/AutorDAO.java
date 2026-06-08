package com.gestao.biblioteca.dao;

import com.gestao.biblioteca.model.Autor;
import com.gestao.biblioteca.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public List<Autor> listarTodos() throws SQLException {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT * FROM autores ORDER BY nome ASC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Autor a = new Autor(
                    rs.getInt("id_autor"),
                    rs.getString("nome"),
                    rs.getString("nacionalidade")
                );
                autores.add(a);
            }
        }
        return autores;
    }

    public void salvar(Autor autor) throws SQLException {
        String sql = "INSERT INTO autores (nome, nacionalidade) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    autor.setIdAutor(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Autor autor) throws SQLException {
        String sql = "UPDATE autores SET nome = ?, nacionalidade = ? WHERE id_autor = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setInt(3, autor.getIdAutor());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idAutor) throws SQLException {
        String sql = "DELETE FROM autores WHERE id_autor = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAutor);
            stmt.executeUpdate();
        }
    }

    public Autor buscarPorId(int idAutor) throws SQLException {
        String sql = "SELECT * FROM autores WHERE id_autor = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAutor);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Autor(
                        rs.getInt("id_autor"),
                        rs.getString("nome"),
                        rs.getString("nacionalidade")
                    );
                }
            }
        }
        return null;
    }
}
