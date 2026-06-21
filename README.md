# Academy System

API REST para gerenciamento de uma academia, desenvolvida com foco em **aprendizado progressivo** de conceitos do ecossistema Spring Boot. Cada funcionalidade implementada serve como laboratório prático para consolidar um novo conceito antes de avançar para o próximo.

## Objetivo

O projeto foi criado para estudar e implementar conceitos em sequência, partindo de uma base simples e evoluindo gradualmente. A ideia é que cada camada adicionada (segurança, paginação, validação, documentação etc.) seja estudada em profundidade antes de seguir adiante.

## Tecnologias

- **Java 25**
- **Spring Boot 4.0.6**
- **Spring Data JPA** — persistência e repositórios
- **Spring Security** — autenticação e autorização
- **JWT (jjwt 0.12.6)** — tokens stateless
- **PostgreSQL** — banco de dados relacional
- **Lombok** — redução de boilerplate
- **Bean Validation** — validação de dados de entrada
- **SpringDoc OpenAPI 3 (Swagger UI)** — documentação interativa da API

## Conceitos implementados

| # | Conceito | Status |
|---|----------|--------|
| 1 | CRUD básico (Aluno, Funcionário, Exercícios, Treino) | Feito |
| 2 | Camada de serviço e repositório (JPA) | Feito |
| 3 | DTOs e separação de camadas | Feito |
| 4 | Tratamento de exceções global (`GlobalExceptionHandler`) | Feito |
| 5 | Autenticação com JWT e Spring Security | Feito |
| 6 | Autorização por roles (`ADMIN`, `FUNCIONARIO`, `ALUNO`) | Feito |
| 7 | Paginação de resultados | Feito |
| 8 | Documentação com Swagger / OpenAPI | Feito |
| 9 | Avaliações físicas vinculadas a alunos | Feito |

## Estrutura do projeto

```
src/main/java/com/david/academysystem/
├── config/          # Segurança (JWT filter, SecurityConfiguration, Swagger)
├── controller/      # Endpoints REST
├── database/
│   ├── model/       # Entidades JPA
│   └── repository/  # Interfaces de repositório
├── dto/             # Objetos de transferência de dados (request/response)
├── enums/           # Enumerações (RoleTypeEnum)
├── exception/       # Exceções customizadas e ErrorResponse
├── handler/         # GlobalExceptionHandler
└── service/         # Regras de negócio
```

## Endpoints principais

| Método | Rota | Descrição | Roles |
|--------|------|-----------|-------|
| POST | `/v1/auth/register` | Cadastro de usuário | Público |
| POST | `/v1/auth/login` | Login e geração de token | Público |
| POST | `/v1/alunos` | Criar aluno | FUNCIONARIO, ADMIN |
| GET | `/v1/alunos` | Listar alunos | FUNCIONARIO, ADMIN |
| GET | `/v1/alunos/page/{page}/size/{size}` | Listar alunos paginado | FUNCIONARIO, ADMIN |
| GET | `/v1/alunos/{id}` | Buscar aluno por ID | Dono, FUNCIONARIO, ADMIN |
| PUT | `/v1/alunos/{id}` | Atualizar aluno | Dono, FUNCIONARIO, ADMIN |
| DELETE | `/v1/alunos/{id}` | Excluir aluno | FUNCIONARIO, ADMIN |
| GET | `/v1/alunos/{id}/avaliacao` | Avaliação física do aluno | Dono, FUNCIONARIO, ADMIN |
| CRUD | `/v1/funcionarios` | Gerenciar funcionários | ADMIN |
| CRUD | `/v1/exercicios` | Gerenciar exercícios | FUNCIONARIO, ADMIN |
| CRUD | `/v1/treinos` | Gerenciar treinos | FUNCIONARIO, ADMIN |

A documentação completa e interativa está disponível via Swagger UI em `/swagger-ui.html` após subir a aplicação.

## Como executar

**Pré-requisitos:** Java 25, Maven, PostgreSQL

1. Clone o repositório
2. Configure as variáveis de banco de dados em `src/main/resources/application.properties`
3. Execute:

```bash
./mvnw spring-boot:run
```

4. Acesse a API em `http://localhost:8080` e o Swagger em `http://localhost:8080/swagger-ui.html`

## Próximos conceitos

- Testes de integração com banco real
- Cache com Redis
- Migrations com Flyway
- Deploy em container (Docker)
