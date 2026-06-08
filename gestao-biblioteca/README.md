# Gestão de Biblioteca em Java

Este projeto é uma aplicação desktop em Java Swing para gerenciar livros, autores, usuários e empréstimos de uma biblioteca, com persistência em MySQL usando JDBC.

## O que o sistema faz

- Cadastrar e listar livros
- Cadastrar e listar autores
- Cadastrar e listar usuários
- Registrar empréstimos e devoluções
- Exibir um relatório de empréstimos

## Estrutura do projeto

- `src/`: código-fonte Java
- `lib/`: dependências externas, como o driver JDBC do MySQL
- `bin/`: arquivos compilados
- `db/`: script SQL do banco
- `docs/`: diagramas e documentação

## Requisitos

Antes de rodar o projeto em outra máquina, confirme que possui:

- Java 17 ou superior
- MySQL Server instalado e em execução
- Git (opcional, apenas para clonar o projeto)

## Como rodar em outra máquina

### 1. Clone ou copie o projeto

```bash
git clone https://github.com/guilhermesouzasantos1505/Gestao-Biblioteca---JAVA.git
```

Ou copie a pasta do projeto para a máquina destino.

### 2. Instale e configure o MySQL

1. Instale o MySQL Server.
2. Crie um usuário com acesso ao banco.
3. Crie o banco com o script disponível em `db/schema.sql`.

### 3. Configure as variáveis de ambiente

O projeto utiliza as variáveis de ambiente abaixo para conectar ao banco:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

> Se preferir, você também pode ajustar a conexão diretamente no arquivo `src/com/gestao/biblioteca/util/ConnectionFactory.java`.

### 4. Compile o projeto

No Windows, execute:

./build.bat


Isso compila a aplicação com o driver JDBC do MySQL.

### 5. Execute a aplicação

No Windows, use:

```bat
java -cp "lib\mysql-connector-java-8.0.30.jar;bin" com.gestao.biblioteca.view.MainFrame
```

Ou abra o arquivo "MainFrame.java" e o execute

### 6. Verifique o banco

O script em `db/schema.sql` já cria as tabelas e insere dados de exemplo para teste.

## Observações importantes

- O projeto foi desenvolvido para Java 17.
- A porta padrão usada no exemplo é a porta 3307 do MySQL.
- Se o MySQL estiver em outra porta ou com outra senha, ajuste `DB_URL` e `DB_PASSWORD`.

## Documentação adicional

A pasta `docs/` contém diagramas e o relatório do projeto.

