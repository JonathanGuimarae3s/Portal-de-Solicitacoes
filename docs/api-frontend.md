# Documentação da API para o Frontend Vue

Esta documentação resume os contratos que o frontend Vue precisa consumir da API **Portal de Solicitação Interna**.

## Base URL

Ambiente local:

```text
http://localhost:8080
```

No frontend, configure em `.env`:

```properties
VITE_API_BASE_URL=http://localhost:8080
```

## Autenticação

A API usa autenticação via **JWT Bearer Token**.

Após o login, salve o token no frontend e envie em todas as requisições protegidas:

```http
Authorization: Bearer {token}
```

Sugestão no Vue:

```js
localStorage.setItem('token', response.data.token)
localStorage.setItem('usuario', JSON.stringify(response.data.usuarioAutenticado))
localStorage.setItem('expiresInMillis', response.data.expiresInMillis)
```

## Usuários de desenvolvimento

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | `admin@empresa.com` | `password` |
| GESTOR | `gestor@empresa.com` | `password` |
| USUARIO | `usuario@empresa.com` | `password` |

## Formato de paginação

Endpoints paginados retornam:

```json
{
  "dados": [],
  "pagina": 0,
  "tamanho": 10,
  "totalDados": 30,
  "totalPaginas": 3
}
```

Parâmetros aceitos:

```text
?page=0&size=10
```

## Login

### POST `/auth/login`

Acesso: público.

Request:

```json
{
  "email": "admin@empresa.com",
  "senha": "password"
}
```

Response `200 OK`:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresInMillis": 7200000,
  "usuarioAutenticado": {
    "nome": "Administrador Demo",
    "email": "admin@empresa.com",
    "perfil": "ADMIN",
    "setor": "TI"
  }
}
```

Observações para o frontend:

- `expiresInMillis` é a duração do token em milissegundos.
- Para 2 horas, o valor esperado é `7200000`.
- Use `perfil` para controlar menus e botões visíveis.
- Quando o token expirar, redirecione para `/login`.

## Usuários

### POST `/usuarios`

Acesso: público.

Cria um usuário.

Request:

```json
{
  "nome": "João Silva",
  "email": "joao.silva@empresa.com",
  "senha": "password",
  "perfil": "USUARIO",
  "setor": "TI"
}
```

Response `201 Created`:

```json
{
  "id": 11,
  "nome": "João Silva",
  "email": "joao.silva@empresa.com",
  "setor": "TI",
  "ativo": true
}
```

### GET `/usuarios`

Acesso: `ADMIN`, `GESTOR`.

Lista usuários com paginação.

Query params:

```text
page=0
size=10
setor=TI
```

`setor` é opcional.

Response `200 OK`:

```json
{
  "dados": [
    {
      "id": 1,
      "nome": "Administrador Demo",
      "email": "admin@empresa.com",
      "setor": "TI",
      "ativo": true
    }
  ],
  "pagina": 0,
  "tamanho": 10,
  "totalDados": 1,
  "totalPaginas": 1
}
```

### PUT `/usuarios/{id}`

Acesso: `ADMIN`.

Atualiza usuário.

Request:

```json
{
  "nome": "Administrador Demo",
  "email": "admin@empresa.com",
  "senha": "password",
  "perfil": "ADMIN",
  "setor": "TI"
}
```

Response `200 OK`:

```json
{
  "id": 1,
  "nome": "Administrador Demo",
  "email": "admin@empresa.com",
  "setor": "TI",
  "ativo": true
}
```

### PATCH `/usuarios/{id}/status`

Acesso: `ADMIN`.

Ativa ou desativa usuário.

Request:

```json
{
  "ativo": false
}
```

Response `200 OK`:

```json
{
  "id": 1,
  "nome": "Administrador Demo",
  "email": "admin@empresa.com",
  "setor": "TI",
  "ativo": false
}
```

## Tipos de solicitação

### GET `/tipos`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Lista tipos com paginação.

Query params:

```text
page=0&size=20
```

Response:

```json
{
  "dados": [
    {
      "id": 1,
      "nome": "Suporte Técnico"
    }
  ],
  "pagina": 0,
  "tamanho": 20,
  "totalDados": 1,
  "totalPaginas": 1
}
```

### GET `/tipos/{id}`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Response:

```json
{
  "id": 1,
  "nome": "Suporte Técnico"
}
```

### POST `/tipos`

Acesso: `ADMIN`.

Request:

```json
{
  "nome": "Acesso a Sistema"
}
```

Response `201 Created`:

```json
{
  "id": 7,
  "nome": "Acesso a Sistema"
}
```

### PUT `/tipos/{id}`

Acesso: `ADMIN`.

Request:

```json
{
  "nome": "Suporte Técnico"
}
```

Response:

```json
{
  "id": 1,
  "nome": "Suporte Técnico"
}
```

### DELETE `/tipos/{id}`

Acesso: `ADMIN`.

Response:

```text
204 No Content
```

## Solicitações

Valores aceitos:

Prioridade:

```text
BAIXA
MEDIA
ALTA
```

Status:

```text
ABERTA
EM_APROVACAO
APROVADA
REJEITADA
CONCLUIDA
```

Fluxo permitido:

```text
ABERTA -> EM_APROVACAO
EM_APROVACAO -> APROVADA
EM_APROVACAO -> REJEITADA
APROVADA -> CONCLUIDA
```

### GET `/solicitacoes`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Lista solicitações com paginação e filtros opcionais.

Query params:

```text
page=0
size=10
status=ABERTA
prioridade=MEDIA
```

Response:

```json
{
  "dados": [
    {
      "id": 1,
      "titulo": "Acesso ao sistema negado",
      "descricao": "Usuário não consegue fazer login no sistema corporativo",
      "prioridade": "MEDIA",
      "status": "ABERTA",
      "dataCriacao": "2026-08-08T10:30:00",
      "usuario": {
        "id": 1,
        "nome": "Administrador Demo",
        "email": "admin@empresa.com",
        "setor": "TI"
      },
      "tipo": {
        "id": 1,
        "nome": "Suporte Técnico"
      }
    }
  ],
  "pagina": 0,
  "tamanho": 10,
  "totalDados": 1,
  "totalPaginas": 1
}
```

### GET `/solicitacoes/{id}`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Response:

```json
{
  "id": 1,
  "titulo": "Acesso ao sistema negado",
  "descricao": "Usuário não consegue fazer login no sistema corporativo",
  "prioridade": "MEDIA",
  "status": "ABERTA",
  "dataCriacao": "2026-08-08T10:30:00",
  "usuario": {
    "id": 1,
    "nome": "Administrador Demo",
    "email": "admin@empresa.com",
    "setor": "TI"
  },
  "tipo": {
    "id": 1,
    "nome": "Suporte Técnico"
  }
}
```

### GET `/solicitacoes/resumo`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Retorna somente os campos necessários para a tabela. Aceita os mesmos parâmetros de paginação e os
filtros opcionais `status` e `prioridade` de `GET /solicitacoes`.

Response:

```json
{
  "dados": [
    {
      "id": 1,
      "titulo": "Acesso ao sistema negado",
      "status": "ABERTA",
      "prioridade": "MEDIA"
    }
  ],
  "pagina": 0,
  "tamanho": 10,
  "totalDados": 1,
  "totalPaginas": 1
}
```

### GET `/solicitacoes/contabilizaSolicitacoes`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Retorna os indicadores utilizados no dashboard.

Response:

```json
{
  "total": 30,
  "emAprovacao": 8,
  "aprovadas": 12,
  "usuariosAtivos": 0
}
```

No estado atual da API, `usuariosAtivos` ainda é retornado como `0` pelo controller.

### POST `/solicitacoes`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Cria solicitação.

Request:

```json
{
  "titulo": "Acesso ao portal interno",
  "descricao": "Solicito acesso ao portal interno para executar minhas atividades.",
  "prioridade": "MEDIA",
  "usuario": {
    "id": 3
  },
  "tipo": {
    "id": 1
  }
}
```

Observação:

- Não envie `status` na criação se quiser seguir a regra padrão.
- Toda nova solicitação nasce como `ABERTA`.

Response `201 Created`:

```json
{
  "id": 31,
  "titulo": "Acesso ao portal interno",
  "descricao": "Solicito acesso ao portal interno para executar minhas atividades.",
  "prioridade": "MEDIA",
  "status": "ABERTA",
  "dataCriacao": "2026-08-08T10:30:00",
  "usuario": {
    "id": 3,
    "nome": "Usuario Demo",
    "email": "usuario@empresa.com",
    "setor": "Financeiro"
  },
  "tipo": {
    "id": 1,
    "nome": "Suporte Técnico"
  }
}
```

### PUT `/solicitacoes/{id}`

Acesso: `ADMIN`, `GESTOR`, `USUARIO`.

Atualiza solicitação.

Request:

```json
{
  "titulo": "Acesso ao sistema negado",
  "descricao": "Usuário não consegue fazer login no sistema corporativo.",
  "prioridade": "MEDIA",
  "status": "EM_APROVACAO",
  "dataCriacao": "2026-08-08T10:30:00",
  "usuario": {
    "id": 1
  },
  "tipo": {
    "id": 1
  }
}
```

Response:

```json
{
  "id": 1,
  "titulo": "Acesso ao sistema negado",
  "descricao": "Usuário não consegue fazer login no sistema corporativo.",
  "prioridade": "MEDIA",
  "status": "EM_APROVACAO",
  "usuario": {
    "id": 1,
    "nome": "Administrador Demo",
    "email": "admin@empresa.com",
    "setor": "TI"
  },
  "tipo": {
    "id": 1,
    "nome": "Suporte Técnico"
  }
}
```

## Tratamento de erros

Formato padrão:

```json
{
  "status": 400,
  "timestamp": "2026-08-01T10:30:00",
  "type": "/dados-invalidos",
  "title": "Dados inválidos",
  "detail": "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
  "userMessage": "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
  "fields": [
    {
      "name": "email",
      "userMessage": "deve ser um endereço de e-mail bem formado"
    }
  ]
}
```

Status importantes para o frontend:

| Status | Como tratar |
|---|---|
| 400 | Mostrar mensagem de validação/regra de negócio |
| 401 | Redirecionar para login ou mostrar credenciais inválidas |
| 403 | Mostrar "Você não tem permissão" |
| 404 | Mostrar "Registro não encontrado" |
| 409 | Mostrar conflito, por exemplo e-mail já cadastrado |
| 500 | Mostrar erro inesperado |

## Sugestão de arquivos no Vue

```text
src/
  services/
    api/
      http.js
      authService.js
      usuarioService.js
      tipoService.js
      solicitacaoService.js
  stores/
    authStore.js
```

Exemplo de `http.js`:

```js
import axios from 'axios'

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})
```

Exemplo de `authService.js`:

```js
import { http } from './http'

export function login(payload) {
  return http.post('/auth/login', payload)
}
```

## Ordem recomendada para implementar no frontend

1. Criar `http.js` com Axios.
2. Criar `authService.js`.
3. Criar `authStore.js` com Pinia.
4. Implementar tela de login.
5. Salvar `token`, `usuarioAutenticado` e `expiresInMillis`.
6. Proteger rotas com Vue Router.
7. Criar layout principal com menu lateral.
8. Implementar listagem de solicitações.
9. Implementar usuários.
10. Implementar tipos de solicitação.
