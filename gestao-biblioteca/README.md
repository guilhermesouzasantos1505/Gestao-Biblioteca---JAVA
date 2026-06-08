## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Build and Run

- Use `build.bat` to compile the project with Java 17 and include the MySQL JDBC driver.
- Run the application with:
	- `java -cp "lib\mysql-connector-java-8.0.30.jar;bin" com.gestao.biblioteca.view.MainFrame`

## Database

- Configure MySQL credentials in `DB_URL`, `DB_USER`, and `DB_PASSWORD` environment variables, or update `ConnectionFactory` directly.
	- Default uses a local MySQL server.


## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
