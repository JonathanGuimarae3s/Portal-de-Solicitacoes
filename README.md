# Portal de Solicitação Interna

API REST em Java Spring Boot para cadastro, acompanhamento e aprovação de solicitações internas de uma empresa.

O projeto foi estruturado para demonstrar fundamentos esperados em uma API Spring: organização por camadas, validação, regras de negócio, Flyway, Oracle Database, segurança com JWT, paginação, tratamento de erros e documentação com OpenAPI.

## Stack

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Spring Security
- JWT com `java-jwt`
- Bean Validation
- Flyway
- Oracle Database
- H2 para testes
- Springdoc OpenAPI/Swagger
- Maven

## Como rodar

Crie um arquivo `dev.env` na raiz do projeto:

```properties
DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
DB_USUARIO=PORTALSOLICITACAO
DB_SENHA=sua_senha
JWT_sECRET=sua-chave-secreta-local
```

Execute com o profile `dev`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

No Windows:

```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

## Documentação da API

Com a aplicação rodando, acesse:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Autenticação

O login retorna um token JWT.

```http
POST /auth/login
```

Exemplo de body:

```json
{
  "email": "admin@empresa.com",
  "senha": "password"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Para acessar endpoints protegidos, envie:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Usuarios de demonstracao

Quando o profile `dev` estiver ativo, o Flyway carrega dados mock em `db/dev/R__dados_mock.sql`.

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | `admin@empresa.com` | `password` |
| GESTOR | `gestor@empresa.com` | `password` |
| USUARIO | `usuario@empresa.com` | `password` |

## Perfis de acesso

- `USUARIO`: cria e consulta solicitações permitidas.
- `GESTOR`: consulta usuários e atua em solicitações.
- `ADMIN`: administra usuários, tipos de solicitação e solicitações.

## Endpoints principais

### Autenticação

| Método | Rota | Acesso |
|---|---|---|
| POST | `/auth/login` | Público |

### Usuários

| Método | Rota | Acesso |
|---|---|---|
| POST | `/usuarios` | Público |
| GET | `/usuarios` | ADMIN, GESTOR |
| PUT | `/usuarios/{id}` | ADMIN |
| PATCH | `/usuarios/{id}/status` | ADMIN |

Body planejado para ativar/desativar usuário:

```json
{
  "ativo": false
}
```

### Tipos de solicitação

| Método | Rota | Acesso |
|---|---|---|
| GET | `/tipos` | ADMIN, GESTOR, USUARIO |
| GET | `/tipos/{id}` | ADMIN, GESTOR, USUARIO |
| POST | `/tipos` | ADMIN |
| PUT | `/tipos/{id}` | ADMIN |
| DELETE | `/tipos/{id}` | ADMIN |

### Solicitações

| Método | Rota | Acesso |
|---|---|---|
| GET | `/solicitacoes` | ADMIN, GESTOR, USUARIO |
| GET | `/solicitacoes/{id}` | ADMIN, GESTOR, USUARIO |
| POST | `/solicitacoes` | ADMIN, GESTOR, USUARIO |
| PUT | `/solicitacoes/{id}` | ADMIN, GESTOR, USUARIO |

## Regras de negócio

- Toda solicitação nova nasce com status `ABERTA`.
- Usuário inativo não pode abrir solicitação.
- Usuário inativo não pode ser vinculado a uma solicitação.
- Usuário deve ser ativado/desativado por endpoint específico com método `PATCH`.
- Apenas `ADMIN` pode ativar ou desativar usuários.
- Solicitação `CONCLUIDA` não pode ser alterada.
- E-mail de usuário deve ser único.
- Tipo de solicitação deve ter nome único.
- Senhas são armazenadas com BCrypt.

Fluxo de status:

```text
ABERTA -> EM_APROVACAO
EM_APROVACAO -> APROVADA
EM_APROVACAO -> REJEITADA
APROVADA -> CONCLUIDA
```

## Banco de dados

As migrations ficam em:

```text
src/main/resources/db/migration
```

O profile `dev` também carrega dados mock em:

```text
src/main/resources/db/dev/R__dados_mock.sql
```

Principais melhorias no banco:

- Flyway para versionamento.
- Constraints para e-mail único, status, prioridade, perfil e ativo.
- Índices para filtros e joins.
- Paginação com query de dados e query de contagem.

## Tratamento de erros

A API usa um handler global para padronizar respostas:

- `400 Bad Request`: regra de negócio ou payload inválido.
- `401 Unauthorized`: credenciais inválidas.
- `403 Forbidden`: usuário autenticado sem permissão.
- `404 Not Found`: recurso inexistente.
- `409 Conflict`: conflito de regra ou constraint do banco.
- `500 Internal Server Error`: erro inesperado.

## Testes

Execute:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

O profile de teste usa H2 em memória para subir o contexto sem depender do Oracle local.

