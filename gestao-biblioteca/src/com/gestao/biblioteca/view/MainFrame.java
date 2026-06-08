package com.gestao.biblioteca.view;

import com.gestao.biblioteca.dao.AutorDAO;
import com.gestao.biblioteca.dao.EmprestimoDAO;
import com.gestao.biblioteca.dao.LivroDAO;
import com.gestao.biblioteca.dao.UsuarioDAO;
import com.gestao.biblioteca.model.Autor;
import com.gestao.biblioteca.model.Emprestimo;
import com.gestao.biblioteca.model.Livro;
import com.gestao.biblioteca.model.Usuario;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final AutorDAO autorDAO = new AutorDAO();
    private final LivroDAO livroDAO = new LivroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    private JTable tableAutores;
    private DefaultTableModel tableModelAutores;
    private JTextField txtAutorId, txtAutorNome, txtAutorNacionalidade;
    private JButton btnAutorSalvar, btnAutorExcluir, btnAutorLimpar;

    private JTable tableLivros;
    private DefaultTableModel tableModelLivros;
    private JTextField txtLivroId, txtTitulo, txtIsbn, txtAno, txtQtdTotal, txtQtdDisp;
    private JComboBox<Autor> cbLivroAutor;
    private JButton btnLivroSalvar, btnLivroExcluir, btnLivroLimpar;

    private JTable tableUsuarios;
    private DefaultTableModel tableModelUsuarios;
    private JTextField txtUsuarioId, txtUsuarioNome, txtUsuarioEmail, txtUsuarioTelefone;
    private JButton btnUsuarioSalvar, btnUsuarioExcluir, btnUsuarioLimpar;

    private JTable tableEmprestimos;
    private DefaultTableModel tableModelEmprestimos;
    private JTextField txtEmprestimoId, txtDataEmprestimo, txtDataDevolucao;
    private JComboBox<Usuario> cbEmprestimoUsuario;
    private JComboBox<Livro> cbEmprestimoLivro;
    private JComboBox<String> cbEmprestimoStatus;
    private JButton btnEmprestimoSalvar, btnEmprestimoExcluir, btnEmprestimoLimpar;

    private JTable tableRelatorio;
    private DefaultTableModel tableModelRelatorio;

    private final List<Usuario> usuariosCache = new ArrayList<>();
    private final List<Livro> livrosCache = new ArrayList<>();
    private final List<Autor> autoresCache = new ArrayList<>();
    private final List<Emprestimo> emprestimosCache = new ArrayList<>();

    public MainFrame() {
        setTitle("Sistema de Gestao de Biblioteca");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel panelLivros = buildLivroPanel();
        JPanel panelAutores = buildAutorPanel();
        JPanel panelUsuarios = buildUsuarioPanel();
        JPanel panelEmprestimos = buildEmprestimoPanel();
        JPanel panelRelatorio = buildRelatorioPanel();

        tabbedPane.addTab("Livros", panelLivros);
        tabbedPane.addTab("Autores", panelAutores);
        tabbedPane.addTab("Usuarios", panelUsuarios);
        tabbedPane.addTab("Emprestimos", panelEmprestimos);
        tabbedPane.addTab("Relatorio", panelRelatorio);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() == panelRelatorio) {
                    carregarRelatorioTabela();
                }
            }
        });

        setContentPane(tabbedPane);
        carregarTodosDados();
    }

    private JPanel buildLivroPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Livros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        txtLivroId = new JTextField(5);
        txtLivroId.setEditable(false);
        gbc.gridx = 1; formPanel.add(txtLivroId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Titulo*:"), gbc);
        txtTitulo = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("ISBN*:"), gbc);
        txtIsbn = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtIsbn, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Ano*:"), gbc);
        txtAno = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtAno, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Qtd Total*:"), gbc);
        txtQtdTotal = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtQtdTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Qtd Disp*:"), gbc);
        txtQtdDisp = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtQtdDisp, gbc);

        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Autor*:"), gbc);
        cbLivroAutor = new JComboBox<>();
        gbc.gridx = 1; formPanel.add(cbLivroAutor, gbc);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnLivroSalvar = new JButton("Salvar / Atualizar");
        btnLivroExcluir = new JButton("Excluir");
        btnLivroLimpar = new JButton("Limpar");
        actionsPanel.add(btnLivroSalvar);
        actionsPanel.add(btnLivroExcluir);
        actionsPanel.add(btnLivroLimpar);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        formPanel.add(actionsPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        String[] colunas = {"ID", "Titulo", "ISBN", "Ano", "Qtd Total", "Qtd Disp"};
        tableModelLivros = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableLivros = new JTable(tableModelLivros);
        JScrollPane scrollPane = new JScrollPane(tableLivros);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Livros Cadastrados"));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnLivroSalvar.addActionListener(e -> executarSalvarLivro());
        btnLivroExcluir.addActionListener(e -> executarExcluirLivro());
        btnLivroLimpar.addActionListener(e -> limparCamposLivros());
        tableLivros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherFormularioLivro();
            }
        });

        return panel;
    }

    private JPanel buildAutorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Autores"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        txtAutorId = new JTextField(5);
        txtAutorId.setEditable(false);
        gbc.gridx = 1; formPanel.add(txtAutorId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Nome*:"), gbc);
        txtAutorNome = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtAutorNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Nacionalidade:"), gbc);
        txtAutorNacionalidade = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtAutorNacionalidade, gbc);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnAutorSalvar = new JButton("Salvar / Atualizar");
        btnAutorExcluir = new JButton("Excluir");
        btnAutorLimpar = new JButton("Limpar");
        actionsPanel.add(btnAutorSalvar);
        actionsPanel.add(btnAutorExcluir);
        actionsPanel.add(btnAutorLimpar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(actionsPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        String[] colunas = {"ID", "Nome", "Nacionalidade"};
        tableModelAutores = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableAutores = new JTable(tableModelAutores);
        JScrollPane scrollPane = new JScrollPane(tableAutores);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Autores Cadastrados"));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnAutorSalvar.addActionListener(e -> executarSalvarAutor());
        btnAutorExcluir.addActionListener(e -> executarExcluirAutor());
        btnAutorLimpar.addActionListener(e -> limparCamposAutores());
        tableAutores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherFormularioAutor();
            }
        });

        return panel;
    }

    private JPanel buildUsuarioPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Usuarios"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        txtUsuarioId = new JTextField(5);
        txtUsuarioId.setEditable(false);
        gbc.gridx = 1; formPanel.add(txtUsuarioId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Nome*:"), gbc);
        txtUsuarioNome = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtUsuarioNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Email*:"), gbc);
        txtUsuarioEmail = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtUsuarioEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Telefone:"), gbc);
        txtUsuarioTelefone = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtUsuarioTelefone, gbc);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnUsuarioSalvar = new JButton("Salvar / Atualizar");
        btnUsuarioExcluir = new JButton("Excluir");
        btnUsuarioLimpar = new JButton("Limpar");
        actionsPanel.add(btnUsuarioSalvar);
        actionsPanel.add(btnUsuarioExcluir);
        actionsPanel.add(btnUsuarioLimpar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(actionsPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        String[] colunas = {"ID", "Nome", "Email", "Telefone"};
        tableModelUsuarios = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableUsuarios = new JTable(tableModelUsuarios);
        JScrollPane scrollPane = new JScrollPane(tableUsuarios);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Usuarios Cadastrados"));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnUsuarioSalvar.addActionListener(e -> executarSalvarUsuario());
        btnUsuarioExcluir.addActionListener(e -> executarExcluirUsuario());
        btnUsuarioLimpar.addActionListener(e -> limparCamposUsuarios());
        tableUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherFormularioUsuario();
            }
        });

        return panel;
    }

    private JPanel buildEmprestimoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Emprestimos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        txtEmprestimoId = new JTextField(5);
        txtEmprestimoId.setEditable(false);
        gbc.gridx = 1; formPanel.add(txtEmprestimoId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Usuario*:"), gbc);
        cbEmprestimoUsuario = new JComboBox<>();
        gbc.gridx = 1; formPanel.add(cbEmprestimoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Livro*:"), gbc);
        cbEmprestimoLivro = new JComboBox<>();
        gbc.gridx = 1; formPanel.add(cbEmprestimoLivro, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Data Emprestimo* (yyyy-MM-dd):"), gbc);
        txtDataEmprestimo = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtDataEmprestimo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Data Devolucao* (yyyy-MM-dd):"), gbc);
        txtDataDevolucao = new JTextField(18);
        gbc.gridx = 1; formPanel.add(txtDataDevolucao, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Status*:"), gbc);
        cbEmprestimoStatus = new JComboBox<>(new String[] {"ATIVO", "DEVOLVIDO", "ATRASADO"});
        gbc.gridx = 1; formPanel.add(cbEmprestimoStatus, gbc);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnEmprestimoSalvar = new JButton("Salvar / Atualizar");
        btnEmprestimoExcluir = new JButton("Excluir");
        btnEmprestimoLimpar = new JButton("Limpar");
        actionsPanel.add(btnEmprestimoSalvar);
        actionsPanel.add(btnEmprestimoExcluir);
        actionsPanel.add(btnEmprestimoLimpar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(actionsPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        String[] colunas = {"ID", "Usuario", "Livro", "Data Emprestimo", "Data Devolucao", "Status"};
        tableModelEmprestimos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableEmprestimos = new JTable(tableModelEmprestimos);
        JScrollPane scrollPane = new JScrollPane(tableEmprestimos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Emprestimos Cadastrados"));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnEmprestimoSalvar.addActionListener(e -> executarSalvarEmprestimo());
        btnEmprestimoExcluir.addActionListener(e -> executarExcluirEmprestimo());
        btnEmprestimoLimpar.addActionListener(e -> limparCamposEmprestimos());
        tableEmprestimos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherFormularioEmprestimo();
            }
        });

        return panel;
    }

    private JPanel buildRelatorioPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel("Relatorio de Emprestimos (view v_relatorio_emprestimos)");
        JButton btnAtualizar = new JButton("Atualizar Relatorio");
        top.add(label, BorderLayout.WEST);
        top.add(btnAtualizar, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        String[] colunas = {"ID", "Usuario", "Livro", "Data Emprestimo", "Data Devolucao", "Status"};
        tableModelRelatorio = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableRelatorio = new JTable(tableModelRelatorio);
        JScrollPane scrollPane = new JScrollPane(tableRelatorio);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Relatorio de Emprestimos"));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnAtualizar.addActionListener(e -> carregarRelatorioTabela());
        return panel;
    }

    private void carregarTodosDados() {
        carregarAutoresTabela();
        carregarComboAutores();
        carregarLivrosTabela();
        carregarUsuariosTabela();
        carregarComboUsuarios();
        carregarComboLivros();
        carregarEmprestimosTabela();
        carregarRelatorioTabela();
    }

    private void carregarLivrosTabela() {
        tableModelLivros.setRowCount(0);
        try {
            List<Livro> livros = livroDAO.listarTodos();
            livrosCache.clear();
            livrosCache.addAll(livros);
            for (Livro l : livros) {
                tableModelLivros.addRow(new Object[]{
                        l.getIdLivro(),
                        l.getTitulo(),
                        l.getIsbn(),
                        l.getAnoPublicacao(),
                        l.getQuantidadeTotal(),
                        l.getQuantidadeDisponivel()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarAutoresTabela() {
        tableModelAutores.setRowCount(0);
        try {
            List<Autor> autores = autorDAO.listarTodos();
            for (Autor a : autores) {
                tableModelAutores.addRow(new Object[]{
                        a.getIdAutor(),
                        a.getNome(),
                        a.getNacionalidade()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar autores: " + e.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarUsuariosTabela() {
        tableModelUsuarios.setRowCount(0);
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            usuariosCache.clear();
            usuariosCache.addAll(usuarios);
            for (Usuario u : usuarios) {
                tableModelUsuarios.addRow(new Object[]{
                        u.getIdUsuario(),
                        u.getNome(),
                        u.getEmail(),
                        u.getTelefone()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuarios: " + e.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarEmprestimosTabela() {
        tableModelEmprestimos.setRowCount(0);
        try {
            List<Emprestimo> emprestimos = emprestimoDAO.listarTodos();
            emprestimosCache.clear();
            emprestimosCache.addAll(emprestimos);
            for (Emprestimo e : emprestimos) {
                tableModelEmprestimos.addRow(new Object[]{
                        e.getIdEmprestimo(),
                        e.getNomeUsuario(),
                        e.getTituloLivro(),
                        e.getDataEmprestimo(),
                        e.getDataDevolucaoPrevista(),
                        e.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar emprestimos: " + e.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarRelatorioTabela() {
        tableModelRelatorio.setRowCount(0);
        try {
            List<Emprestimo> relatorio = emprestimoDAO.listarRelatorio();
            for (Emprestimo e : relatorio) {
                tableModelRelatorio.addRow(new Object[]{
                        e.getIdEmprestimo(),
                        e.getNomeUsuario(),
                        e.getTituloLivro(),
                        e.getDataEmprestimo(),
                        e.getDataDevolucaoPrevista(),
                        e.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar relatorio: " + e.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarComboAutores() {
        cbLivroAutor.removeAllItems();
        autoresCache.clear();
        try {
            List<Autor> autores = autorDAO.listarTodos();
            autoresCache.addAll(autores);
            for (Autor a : autores) {
                cbLivroAutor.addItem(a);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar autores para o livro: " + e.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarComboUsuarios() {
        cbEmprestimoUsuario.removeAllItems();
        for (Usuario u : usuariosCache) {
            cbEmprestimoUsuario.addItem(u);
        }
    }

    private void carregarComboLivros() {
        cbEmprestimoLivro.removeAllItems();
        for (Livro l : livrosCache) {
            cbEmprestimoLivro.addItem(l);
        }
    }

    private void executarSalvarLivro() {
        if (txtTitulo.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty() || txtAno.getText().trim().isEmpty() || cbLivroAutor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatorios do livro e selecione um autor.", "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int ano = Integer.parseInt(txtAno.getText().trim());
            int qtdTotal = Integer.parseInt(txtQtdTotal.getText().trim());
            int qtdDisp = Integer.parseInt(txtQtdDisp.getText().trim());

            if (qtdDisp > qtdTotal) {
                JOptionPane.showMessageDialog(this, "A quantidade disponivel nao pode exceder a quantidade total.", "Regra de Negocio", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Autor autorSelecionado = (Autor) cbLivroAutor.getSelectedItem();
            Livro livro = new Livro();
            livro.setTitulo(txtTitulo.getText().trim());
            livro.setIsbn(txtIsbn.getText().trim());
            livro.setAnoPublicacao(ano);
            livro.setQuantidadeTotal(qtdTotal);
            livro.setQuantidadeDisponivel(qtdDisp);

            if (txtLivroId.getText().isEmpty()) {
                livroDAO.salvar(livro);
                livroDAO.atualizarAutores(livro.getIdLivro(), autorSelecionado.getIdAutor());
                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso.");
            } else {
                livro.setIdLivro(Integer.parseInt(txtLivroId.getText()));
                livroDAO.atualizar(livro);
                livroDAO.atualizarAutores(livro.getIdLivro(), autorSelecionado.getIdAutor());
                JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso.");
            }
            limparCamposLivros();
            carregarLivrosTabela();
            carregarComboLivros();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe valores numericos validos para ano e quantidades.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar livro: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executarExcluirLivro() {
        if (txtLivroId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um livro antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o livro selecionado?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                livroDAO.deletar(Integer.parseInt(txtLivroId.getText()));
                JOptionPane.showMessageDialog(this, "Livro excluido com sucesso.");
                limparCamposLivros();
                carregarLivrosTabela();
                carregarComboLivros();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Nao foi possivel excluir o livro: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarSalvarAutor() {
        if (txtAutorNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome do autor.", "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Autor autor = new Autor();
            autor.setNome(txtAutorNome.getText().trim());
            autor.setNacionalidade(txtAutorNacionalidade.getText().trim());

            if (txtAutorId.getText().isEmpty()) {
                autorDAO.salvar(autor);
                JOptionPane.showMessageDialog(this, "Autor cadastrado com sucesso.");
            } else {
                autor.setIdAutor(Integer.parseInt(txtAutorId.getText()));
                autorDAO.atualizar(autor);
                JOptionPane.showMessageDialog(this, "Autor atualizado com sucesso.");
            }
            limparCamposAutores();
            carregarAutoresTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar autor: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executarExcluirAutor() {
        if (txtAutorId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um autor antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o autor selecionado?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                autorDAO.deletar(Integer.parseInt(txtAutorId.getText()));
                JOptionPane.showMessageDialog(this, "Autor excluido com sucesso.");
                limparCamposAutores();
                carregarAutoresTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Nao foi possivel excluir o autor: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarSalvarUsuario() {
        if (txtUsuarioNome.getText().trim().isEmpty() || txtUsuarioEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome e email do usuario.", "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNome(txtUsuarioNome.getText().trim());
            usuario.setEmail(txtUsuarioEmail.getText().trim());
            usuario.setTelefone(txtUsuarioTelefone.getText().trim());

            if (txtUsuarioId.getText().isEmpty()) {
                usuarioDAO.salvar(usuario);
                JOptionPane.showMessageDialog(this, "Usuario cadastrado com sucesso.");
            } else {
                usuario.setIdUsuario(Integer.parseInt(txtUsuarioId.getText()));
                usuarioDAO.atualizar(usuario);
                JOptionPane.showMessageDialog(this, "Usuario atualizado com sucesso.");
            }
            limparCamposUsuarios();
            carregarUsuariosTabela();
            carregarComboUsuarios();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuario: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executarExcluirUsuario() {
        if (txtUsuarioId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o usuario selecionado?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                usuarioDAO.deletar(Integer.parseInt(txtUsuarioId.getText()));
                JOptionPane.showMessageDialog(this, "Usuario excluido com sucesso.");
                limparCamposUsuarios();
                carregarUsuariosTabela();
                carregarComboUsuarios();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Nao foi possivel excluir o usuario: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarSalvarEmprestimo() {
        if (cbEmprestimoUsuario.getSelectedItem() == null || cbEmprestimoLivro.getSelectedItem() == null || txtDataEmprestimo.getText().trim().isEmpty() || txtDataDevolucao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione usuario, livro e datas para o emprestimo.", "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = (Usuario) cbEmprestimoUsuario.getSelectedItem();
            Livro livro = (Livro) cbEmprestimoLivro.getSelectedItem();
            Date dataEmprestimo = Date.valueOf(LocalDate.parse(txtDataEmprestimo.getText().trim()));
            Date dataDevolucao = Date.valueOf(LocalDate.parse(txtDataDevolucao.getText().trim()));
            String status = cbEmprestimoStatus.getSelectedItem().toString();

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setIdUsuario(usuario.getIdUsuario());
            emprestimo.setIdLivro(livro.getIdLivro());
            emprestimo.setDataEmprestimo(dataEmprestimo);
            emprestimo.setDataDevolucaoPrevista(dataDevolucao);
            emprestimo.setStatus(status);

            if (txtEmprestimoId.getText().isEmpty()) {
                emprestimoDAO.salvar(emprestimo);
                JOptionPane.showMessageDialog(this, "Emprestimo registrado com sucesso.");
            } else {
                emprestimo.setIdEmprestimo(Integer.parseInt(txtEmprestimoId.getText()));
                emprestimoDAO.atualizar(emprestimo);
                JOptionPane.showMessageDialog(this, "Emprestimo atualizado com sucesso.");
            }

            limparCamposEmprestimos();
            carregarEmprestimosTabela();
            carregarLivrosTabela();
            carregarComboLivros();
            carregarRelatorioTabela();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "As datas devem estar no formato yyyy-MM-dd.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar emprestimo: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executarExcluirEmprestimo() {
        if (txtEmprestimoId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um emprestimo antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este emprestimo?", "Confirmacao", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                emprestimoDAO.deletar(Integer.parseInt(txtEmprestimoId.getText()));
                JOptionPane.showMessageDialog(this, "Emprestimo excluido com sucesso.");
                limparCamposEmprestimos();
                carregarEmprestimosTabela();
                carregarLivrosTabela();
                carregarComboLivros();
                carregarRelatorioTabela();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir emprestimo: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void preencherFormularioLivro() {
        int row = tableLivros.getSelectedRow();
        if (row != -1) {
            int idLivro = Integer.parseInt(tableModelLivros.getValueAt(row, 0).toString());
            txtLivroId.setText(String.valueOf(idLivro));
            txtTitulo.setText(tableModelLivros.getValueAt(row, 1).toString());
            txtIsbn.setText(tableModelLivros.getValueAt(row, 2).toString());
            txtAno.setText(tableModelLivros.getValueAt(row, 3).toString());
            txtQtdTotal.setText(tableModelLivros.getValueAt(row, 4).toString());
            txtQtdDisp.setText(tableModelLivros.getValueAt(row, 5).toString());
            selecionarAutorDoLivro(idLivro);
        }
    }

    private void selecionarAutorDoLivro(int idLivro) {
        try {
            List<Integer> idsAutores = livroDAO.listarAutoresPorLivro(idLivro);
            if (idsAutores.isEmpty()) {
                cbLivroAutor.setSelectedIndex(-1);
                return;
            }
            for (int i = 0; i < cbLivroAutor.getItemCount(); i++) {
                Autor autor = cbLivroAutor.getItemAt(i);
                if (autor != null && idsAutores.contains(autor.getIdAutor())) {
                    cbLivroAutor.setSelectedIndex(i);
                    return;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar autor do livro: " + ex.getMessage(), "Erro JDBC", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormularioAutor() {
        int row = tableAutores.getSelectedRow();
        if (row != -1) {
            txtAutorId.setText(tableModelAutores.getValueAt(row, 0).toString());
            txtAutorNome.setText(tableModelAutores.getValueAt(row, 1).toString());
            txtAutorNacionalidade.setText(tableModelAutores.getValueAt(row, 2).toString());
        }
    }

    private void preencherFormularioUsuario() {
        int row = tableUsuarios.getSelectedRow();
        if (row != -1) {
            txtUsuarioId.setText(tableModelUsuarios.getValueAt(row, 0).toString());
            txtUsuarioNome.setText(tableModelUsuarios.getValueAt(row, 1).toString());
            txtUsuarioEmail.setText(tableModelUsuarios.getValueAt(row, 2).toString());
            txtUsuarioTelefone.setText(tableModelUsuarios.getValueAt(row, 3).toString());
        }
    }

    private void preencherFormularioEmprestimo() {
        int row = tableEmprestimos.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tableModelEmprestimos.getValueAt(row, 0).toString());
            Emprestimo emprestimo = buscarEmprestimoPorId(id);
            if (emprestimo != null) {
                txtEmprestimoId.setText(emprestimo.getIdEmprestimo().toString());
                txtDataEmprestimo.setText(emprestimo.getDataEmprestimo().toString());
                txtDataDevolucao.setText(emprestimo.getDataDevolucaoPrevista().toString());
                cbEmprestimoStatus.setSelectedItem(emprestimo.getStatus());
                selectUsuarioCombo(emprestimo.getIdUsuario());
                selectLivroCombo(emprestimo.getIdLivro());
            }
        }
    }

    private void selectUsuarioCombo(int idUsuario) {
        for (int i = 0; i < cbEmprestimoUsuario.getItemCount(); i++) {
            Usuario item = cbEmprestimoUsuario.getItemAt(i);
            if (item != null && item.getIdUsuario().equals(idUsuario)) {
                cbEmprestimoUsuario.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectLivroCombo(int idLivro) {
        for (int i = 0; i < cbEmprestimoLivro.getItemCount(); i++) {
            Livro item = cbEmprestimoLivro.getItemAt(i);
            if (item != null && item.getIdLivro().equals(idLivro)) {
                cbEmprestimoLivro.setSelectedIndex(i);
                return;
            }
        }
    }

    private Emprestimo buscarEmprestimoPorId(int idEmprestimo) {
        for (Emprestimo e : emprestimosCache) {
            if (e.getIdEmprestimo() != null && e.getIdEmprestimo().equals(idEmprestimo)) {
                return e;
            }
        }
        return null;
    }

    private void limparCamposLivros() {
        txtLivroId.setText("");
        txtTitulo.setText("");
        txtIsbn.setText("");
        txtAno.setText("");
        txtQtdTotal.setText("");
        txtQtdDisp.setText("");
        cbLivroAutor.setSelectedIndex(-1);
        tableLivros.clearSelection();
    }

    private void limparCamposAutores() {
        txtAutorId.setText("");
        txtAutorNome.setText("");
        txtAutorNacionalidade.setText("");
        tableAutores.clearSelection();
    }

    private void limparCamposUsuarios() {
        txtUsuarioId.setText("");
        txtUsuarioNome.setText("");
        txtUsuarioEmail.setText("");
        txtUsuarioTelefone.setText("");
        tableUsuarios.clearSelection();
    }

    private void limparCamposEmprestimos() {
        txtEmprestimoId.setText("");
        txtDataEmprestimo.setText("");
        txtDataDevolucao.setText("");
        cbEmprestimoUsuario.setSelectedIndex(-1);
        cbEmprestimoLivro.setSelectedIndex(-1);
        cbEmprestimoStatus.setSelectedIndex(0);
        tableEmprestimos.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
