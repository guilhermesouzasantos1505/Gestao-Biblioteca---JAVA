-- ============================================================================
-- SCRIPT DE CRIAÇÃO DO BANCO DE DADOS (schema.sql)
-- TEMA: GESTÃO DE BIBLIOTECA (LIVROS, AUTORES, EMPRÉSTIMOS E USUÁRIOS)
-- ============================================================================

DROP DATABASE IF EXISTS gestao_biblioteca;
CREATE DATABASE gestao_biblioteca;
USE gestao_biblioteca;

-- 1. TABELA: AUTORES
CREATE TABLE autores (
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    nacionalidade VARCHAR(100),
    CONSTRAINT chk_nome_autor CHECK (LENGTH(TRIM(nome)) >= 2)
);

-- 2. TABELA: LIVROS
CREATE TABLE livros (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    ano_publicacao INT NOT NULL,
    quantidade_total INT NOT NULL DEFAULT 1,
    quantidade_disponivel INT NOT NULL DEFAULT 1,
    CONSTRAINT chk_ano CHECK (ano_publicacao > 0 AND ano_publicacao <= 2026),
    CONSTRAINT chk_qtd_total CHECK (quantidade_total >= 0),
    CONSTRAINT chk_qtd_disp CHECK (quantidade_disponivel >= 0 AND quantidade_disponivel <= quantidade_total)
);

-- 3. TABELA ASSOCIATIVA (N:N): LIVRO_AUTOR
CREATE TABLE livro_autor (
    id_livro INT,
    id_autor INT,
    PRIMARY KEY (id_livro, id_autor),
    FOREIGN KEY (id_livro) REFERENCES livros(id_livro) ON DELETE CASCADE,
    FOREIGN KEY (id_autor) REFERENCES autores(id_autor) ON DELETE CASCADE
);

-- 4. TABELA: USUARIOS
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    CONSTRAINT chk_nome_usuario CHECK (LENGTH(TRIM(nome)) >= 3)
);

-- 5. TABELA: EMPRESTIMOS (Relacionamento N:N entre Livros e Usuários com atributos)
CREATE TABLE emprestimos (
    id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_livro INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE RESTRICT,
    FOREIGN KEY (id_livro) REFERENCES livros(id_livro) ON DELETE RESTRICT,
    CONSTRAINT chk_status CHECK (status IN ('ATIVO', 'DEVOLVIDO', 'ATRASADO'))
);

-- ============================================================================
-- INSERÇÃO DE DADOS DE TESTE (POPULAÇÃO INICIAL EXIGIDA)
-- ============================================================================

INSERT INTO autores (nome, nacionalidade) VALUES 
('J.K. Rowling', 'Britânica'),
('George R.R. Martin', 'Americana'),
('Machado de Assis', 'Brasileira');

INSERT INTO livros (titulo, isbn, ano_publicacao, quantidade_total, quantidade_disponivel) VALUES 
('Harry Potter e a Pedra Filosofal', '9788532511010', 1997, 5, 5),
('A Guerra dos Tronos', '9788544102923', 1996, 3, 2),
('Dom Casmurro', '9788594318619', 1899, 4, 4);

INSERT INTO livro_autor (id_livro, id_autor) VALUES 
(1, 1),
(2, 2),
(3, 3);

INSERT INTO usuarios (nome, email, telefone) VALUES 
('Guilherme de Souza', 'guilherme@email.com', '(61) 99999-1111'),
('Eduardo Henrique', 'eduardo@email.com', '(61) 99999-2222'),
('Yann Ricky', 'yann@email.com', '(61) 99999-3333');

INSERT INTO emprestimos (id_usuario, id_livro, data_emprestimo, data_devolucao_prevista, status) VALUES 
(1, 2, '2026-06-01', '2026-06-15', 'ATIVO');

-- VIEW REQUISITADA PARA CONSULTAS E RELATÓRIOS NO VÍDEO
CREATE OR REPLACE VIEW v_relatorio_emprestimos AS
SELECT 
    e.id_emprestimo,
    u.nome AS nome_usuario,
    l.titulo AS titulo_livro,
    e.data_emprestimo,
    e.data_devolucao_prevista,
    e.status
FROM emprestimos e
JOIN usuarios u ON e.id_usuario = u.id_usuario
JOIN livros l ON e.id_livro = l.id_livro;