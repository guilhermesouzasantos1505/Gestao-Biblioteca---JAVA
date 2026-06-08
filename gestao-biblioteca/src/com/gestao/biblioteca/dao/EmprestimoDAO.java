package com.gestao.biblioteca.dao;

import com.gestao.biblioteca.model.Emprestimo;
import com.gestao.biblioteca.model.Livro;
import com.gestao.biblioteca.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    private final LivroDAO livroDAO = new LivroDAO();

    public void salvar(Emprestimo emprestimo) throws SQLException {
        if (!livroDisponivel(emprestimo.getIdLivro())) {
            throw new SQLException("O livro selecionado não possui exemplares disponíveis para empréstimo.");
        }

        String sql = "INSERT INTO emprestimos (id_usuario, id_livro, data_emprestimo, data_devolucao_prevista, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, emprestimo.getIdUsuario());
            stmt.setInt(2, emprestimo.getIdLivro());
            stmt.setDate(3, emprestimo.getDataEmprestimo());
            stmt.setDate(4, emprestimo.getDataDevolucaoPrevista());
            stmt.setString(5, emprestimo.getStatus());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprestimo.setIdEmprestimo(generatedKeys.getInt(1));
                }
            }

            if ("ATIVO".equalsIgnoreCase(emprestimo.getStatus())) {
                livroDAO.ajustarQuantidadeDisponivel(emprestimo.getIdLivro(), -1);
            }
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.id_emprestimo, e.id_usuario, u.nome AS nome_usuario, e.id_livro, l.titulo AS titulo_livro, e.data_emprestimo, e.data_devolucao_prevista, e.status " +
                     "FROM emprestimos e " +
                     "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                     "JOIN livros l ON e.id_livro = l.id_livro " +
                     "ORDER BY e.id_emprestimo DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Emprestimo e = new Emprestimo(
                        rs.getInt("id_emprestimo"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_livro"),
                        rs.getDate("data_emprestimo"),
                        rs.getDate("data_devolucao_prevista"),
                        rs.getString("status")
                );
                e.setNomeUsuario(rs.getString("nome_usuario"));
                e.setTituloLivro(rs.getString("titulo_livro"));
                emprestimos.add(e);
            }
        }
        return emprestimos;
    }

    public List<Emprestimo> listarRelatorio() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM v_relatorio_emprestimos ORDER BY id_emprestimo DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Emprestimo e = new Emprestimo();
                e.setIdEmprestimo(rs.getInt("id_emprestimo"));
                e.setNomeUsuario(rs.getString("nome_usuario"));
                e.setTituloLivro(rs.getString("titulo_livro"));
                e.setDataEmprestimo(rs.getDate("data_emprestimo"));
                e.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista"));
                e.setStatus(rs.getString("status"));
                emprestimos.add(e);
            }
        }
        return emprestimos;
    }

    public Emprestimo buscarPorId(int idEmprestimo) throws SQLException {
        String sql = "SELECT e.id_emprestimo, e.id_usuario, u.nome AS nome_usuario, e.id_livro, l.titulo AS titulo_livro, e.data_emprestimo, e.data_devolucao_prevista, e.status " +
                     "FROM emprestimos e " +
                     "JOIN usuarios u ON e.id_usuario = u.id_usuario " +
                     "JOIN livros l ON e.id_livro = l.id_livro " +
                     "WHERE e.id_emprestimo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmprestimo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Emprestimo e = new Emprestimo(
                            rs.getInt("id_emprestimo"),
                            rs.getInt("id_usuario"),
                            rs.getInt("id_livro"),
                            rs.getDate("data_emprestimo"),
                            rs.getDate("data_devolucao_prevista"),
                            rs.getString("status")
                    );
                    e.setNomeUsuario(rs.getString("nome_usuario"));
                    e.setTituloLivro(rs.getString("titulo_livro"));
                    return e;
                }
            }
        }
        return null;
    }

    public void atualizar(Emprestimo emprestimo) throws SQLException {
        Emprestimo atual = buscarPorId(emprestimo.getIdEmprestimo());
        if (atual == null) {
            throw new SQLException("Empréstimo não encontrado.");
        }

        if (!atual.getIdLivro().equals(emprestimo.getIdLivro())) {
            throw new SQLException("Alterar o livro de um empréstimo existente não é permitido.");
        }

        if ("ATIVO".equalsIgnoreCase(atual.getStatus()) && "DEVOLVIDO".equalsIgnoreCase(emprestimo.getStatus())) {
            livroDAO.ajustarQuantidadeDisponivel(emprestimo.getIdLivro(), 1);
        } else if ("DEVOLVIDO".equalsIgnoreCase(atual.getStatus()) && "ATIVO".equalsIgnoreCase(emprestimo.getStatus())) {
            if (!livroDisponivel(emprestimo.getIdLivro())) {
                throw new SQLException("Não há exemplares disponíveis para reativar o empréstimo.");
            }
            livroDAO.ajustarQuantidadeDisponivel(emprestimo.getIdLivro(), -1);
        }

        String sql = "UPDATE emprestimos SET id_usuario = ?, data_emprestimo = ?, data_devolucao_prevista = ?, status = ? WHERE id_emprestimo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getIdUsuario());
            stmt.setDate(2, emprestimo.getDataEmprestimo());
            stmt.setDate(3, emprestimo.getDataDevolucaoPrevista());
            stmt.setString(4, emprestimo.getStatus());
            stmt.setInt(5, emprestimo.getIdEmprestimo());
            stmt.executeUpdate();
        }
    }

    public void deletar(int idEmprestimo) throws SQLException {
        Emprestimo atual = buscarPorId(idEmprestimo);
        if (atual == null) {
            throw new SQLException("Empréstimo não encontrado.");
        }

        if ("ATIVO".equalsIgnoreCase(atual.getStatus())) {
            livroDAO.ajustarQuantidadeDisponivel(atual.getIdLivro(), 1);
        }

        String sql = "DELETE FROM emprestimos WHERE id_emprestimo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmprestimo);
            stmt.executeUpdate();
        }
    }

    private boolean livroDisponivel(int idLivro) throws SQLException {
        Livro livro = livroDAO.buscarPorId(idLivro);
        return livro != null && livro.getQuantidadeDisponivel() > 0;
    }
}
