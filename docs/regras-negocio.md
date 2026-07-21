# Regras de negócio

## Usuário

- O e-mail do usuário deve ser único.
- A senha deve ser armazenada com BCrypt.
- Usuário inativo não pode autenticar.
- Usuário inativo não pode abrir solicitação.
- Usuário inativo não pode ser vinculado a uma solicitação.
- Ativar ou desativar usuário deve ser feito por endpoint específico: `PATCH /usuarios/{id}/status`.
- Apenas `ADMIN` deve ativar ou desativar usuário.
- Ao atualizar usuário, manter o próprio e-mail é permitido.
- Ao atualizar usuário, usar e-mail de outro usuário deve ser bloqueado.
- Perfis permitidos: `ADMIN`, `GESTOR`, `USUARIO`.

## Autenticação e autorização

- `POST /auth/login` é público e retorna um token JWT.
- Requisições protegidas devem enviar `Authorization: Bearer <token>`.
- `POST /usuarios` é público para cadastro inicial.
- `PATCH /usuarios/{id}/status` deve exigir perfil `ADMIN`.
- `ADMIN` pode administrar usuários, tipos e solicitações.
- `GESTOR` pode consultar usuários e atuar em solicitações.
- `USUARIO` pode criar e consultar solicitações permitidas.
- Regra fina recomendada para service:
  - `USUARIO` acessa apenas as próprias solicitações.
  - `GESTOR` acessa apenas solicitações do seu setor.
  - `ADMIN` acessa tudo.

## Solicitação

- Toda solicitação nova nasce com status `ABERTA`.
- `status` e `prioridade` são opcionais na atualização.
- Se `status` for enviado na atualização, ele deve existir no enum `StatusSolicitacao`.
- Se `prioridade` for enviada na atualização, ela deve existir no enum `PrioridadeSolicitacao`.
- Solicitação `CONCLUIDA` não pode ser alterada.

## Fluxo de status

- `ABERTA -> EM_APROVACAO`
- `EM_APROVACAO -> APROVADA`
- `EM_APROVACAO -> REJEITADA`
- `APROVADA -> CONCLUIDA`
- `REJEITADA` não transiciona para outro status.
- `CONCLUIDA` não transiciona para outro status.

## Tipo de solicitação

- O tipo de solicitação deve existir antes de ser vinculado a uma solicitação.
- Nome de tipo de solicitação deve ser único.
- Tipo de solicitação usado por solicitações não deve ser removido sem uma regra explícita.

## Banco de dados

- `usuario.email` deve ter constraint `UNIQUE`.
- `solicitacao.status` deve ter `CHECK` com os valores do enum.
- `solicitacao.prioridade` deve ter `CHECK` com os valores do enum.
- Campos usados em filtros e joins devem ter índices.
