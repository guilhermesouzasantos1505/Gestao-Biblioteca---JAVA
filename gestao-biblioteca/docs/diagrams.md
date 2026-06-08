Diagrams and rendering

Files in this folder:

- `DER.puml` — Entidade-Relacionamento (DER) para o banco de dados.
- `modelo_logico.puml` — Modelo lógico relacional (tabelas, chaves, constraints).
- `diagrama_classes.puml` — Diagrama de classes (model + DAO).

How to render (two options):

1) Using PlantUML jar locally

```bash
# download plantuml.jar from https://plantuml.com/download
java -jar plantuml.jar diagrama_classes.puml
```

This will produce PNG/SVG files next to the `.puml` sources.

2) Using Docker (convenient on Windows with Docker Desktop)

```bash
docker run --rm -v "%CD%/docs":/workspace plantuml/plantuml DER.puml
```

3) Online

- Copy the contents of a `.puml` file to https://www.plantuml.com/plantuml/ to preview and export.

Notes:

- The `.puml` files are intentionally textual so you can tweak labels/atributos antes de exportar.
- If preferir, eu posso gerar PNGs diretamente e adicioná-los aqui — confirme se quer que eu gere imagens também.
