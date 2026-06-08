# Relatório de Modelagem e Regras de Negócio

## Objetivo do Projeto
Aplicação de gestão de biblioteca em Java Swing com persistência em MySQL via JDBC. O sistema suporta cadastro e manutenção de livros, autores, usuários e empréstimos.

## Entidades Principais
- `Livro`
  - Atributos: `id_livro`, `titulo`, `isbn`, `ano_publicacao`, `quantidade_total`, `quantidade_disponivel`
  - Regras de integridade:
    - `isbn` é único
    - `ano_publicacao` deve ser positivo e até 2026
    - `quantidade_total` não pode ser negativa
    - `quantidade_disponivel` deve estar entre 0 e `quantidade_total`

- `Autor`
  - Atributos: `id_autor`, `nome`, `nacionalidade`
  - Regra de integridade: nome deve ter pelo menos 2 caracteres válidos

- `Usuario`
  - Atributos: `id_usuario`, `nome`, `email`, `telefone`
  - Regra de integridade: nome deve ter pelo menos 3 caracteres
  - `email` é único

- `Emprestimo`
  - Atributos: `id_emprestimo`, `id_usuario`, `id_livro`, `data_emprestimo`, `data_devolucao_prevista`, `status`
  - Status válidos: `ATIVO`, `DEVOLVIDO`, `ATRASADO`
  - Relações: cada empréstimo referencia um usuário e um livro

- `livro_autor`
  - Tabela associativa entre `Livro` e `Autor`
  - Relacionamento N:N: um livro pode ter vários autores; um autor pode ter vários livros

## Relacionamentos
- `Livro` 1 — N `Emprestimo`
- `Usuario` 1 — N `Emprestimo`
- `Livro` N — N `Autor` via `livro_autor`

## Regras de Negócio Implementadas
- Ao criar um empréstimo com status `ATIVO`, a quantidade disponível do livro é decrementada em 1.
- Ao devolver um empréstimo (`ATIVO` → `DEVOLVIDO`), a quantidade disponível do livro é incrementada em 1.
- Não é permitido criar empréstimo se `quantidade_disponivel` do livro for 0.
- A alteração de livro em um empréstimo já existente não é permitida.
- Ao excluir um empréstimo ativo, o exemplar volta a ficar disponível.
- A exclusão de autor ou livro remove automaticamente suas associações na tabela `livro_autor` (cascade delete).

## Implementação Técnica
- Persistência com JDBC e `ConnectionFactory` em `src/com/gestao/biblioteca/util/ConnectionFactory.java`
- DAOs separados por entidade em `src/com/gestao/biblioteca/dao/`
- Modelos em `src/com/gestao/biblioteca/model/`
- Interface gráfica em Swing em `src/com/gestao/biblioteca/view/MainFrame.java`
- Banco de dados definido em `db/schema.sql`

## Artefatos de Documentação
- `docs/DER.puml` — Diagrama entidade-relacionamento
- `docs/modelo_logico.puml` — Modelo lógico das tabelas
- `docs/diagrama_classes.puml` — Diagrama de classes Java
- `docs/GestaoBibliotecaDER.png` — Imagem gerada do DER
- `docs/GestaoBibliotecaLogico.png` — Imagem gerada do modelo lógico

## Observações de Teste
- O script `db/schema.sql` insere dados de teste para autores, livros, usuários e um empréstimo ativo.
- A view `v_relatorio_emprestimos` permite consultar facilmente os relatórios de empréstimos com nome do usuário e título do livro.

## Considerações Finais
O design prioriza separação de camadas (modelo, DAO, view) e integridade referencial no banco. As regras de disponibilidade de livros garantem que empréstimos ativos não deixem o sistema em estado inconsistente.
