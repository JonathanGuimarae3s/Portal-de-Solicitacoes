# Portal de Solicitação Interna — Frontend

Frontend do Portal de Solicitação Interna, desenvolvido com Vue 3, TypeScript, Vite, Pinia, Vue Router, PrimeVue e Tailwind CSS.

## Executar o projeto

Crie o arquivo `.env`:

```properties
VITE_API_URL=http://localhost:8080
```

Instale as dependências e inicie o servidor:

```bash
npm install
npm run dev
```

Validação para produção:

```bash
npm run build
```

## Arquitetura

O projeto utiliza organização por funcionalidades. Cada módulo concentra suas páginas, serviços, stores, componentes e tipos.

```text
src/
  assets/                 # estilos e arquivos estáticos importados
  core/                   # infraestrutura compartilhada da aplicação
    http/                 # cliente Axios e configuração de autenticação
  modules/                # funcionalidades de negócio
    auth/
      pages/
      services/
      stores/
      types/
    dashboard/
      components/
      pages/
      services/
      stores/
      types/
    solicitacoes/
      pages/
    usuarios/
      pages/
  router/                 # rotas e guards de navegação
  shared/                 # componentes e layouts reutilizáveis
    components/
    layouts/
  App.vue
  main.ts
```

## Responsabilidades

- `core`: integrações técnicas usadas por vários módulos, sem regras de interface.
- `modules`: código pertencente a uma funcionalidade específica.
- `services`: comunicação HTTP e adaptação das respostas da API.
- `stores`: estado reativo e coordenação dos casos de uso da tela.
- `types`: contratos TypeScript do módulo.
- `shared`: componentes visuais realmente reutilizáveis.
- `router`: definição das rotas e proteção de páginas autenticadas.

Componentes específicos devem permanecer dentro do próprio módulo. Um componente só deve ir para `shared` quando puder ser reutilizado sem depender de uma regra de negócio específica.
