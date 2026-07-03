-- Dados mock para ambiente local Oracle
-- Máximo: 30 solicitações

DELETE
FROM solicitacao;
DELETE
FROM tipo_solicitacao;
DELETE
FROM usuario;

-- USUARIOS
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (1, 'Joao Silva', 'joao.silva@empresa.com', 'TI', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (2, 'Maria Santos', 'maria.santos@empresa.com', 'RH', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (3, 'Carlos Oliveira', 'carlos.oliveira@empresa.com', 'Financeiro', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (4, 'Ana Costa', 'ana.costa@empresa.com', 'Operacoes', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (5, 'Bruno Almeida', 'bruno.almeida@empresa.com', 'Comercial', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (6, 'Fernanda Lima', 'fernanda.lima@empresa.com', 'Marketing', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (7, 'Rafael Mendes', 'rafael.mendes@empresa.com', 'TI', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (8, 'Juliana Rocha', 'juliana.rocha@empresa.com', 'Juridico', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (9, 'Pedro Henrique', 'pedro.henrique@empresa.com', 'Compras', 1);
INSERT INTO usuario (id, nome, email, setor, ativo)
VALUES (10, 'Camila Ferreira', 'camila.ferreira@empresa.com', 'Atendimento', 1);

-- TIPOS DE SOLICITACAO
INSERT INTO tipo_solicitacao (id, nome)
VALUES (1, 'Suporte Tecnico');
INSERT INTO tipo_solicitacao (id, nome)
VALUES (2, 'Solicitacao de Recurso');
INSERT INTO tipo_solicitacao (id, nome)
VALUES (3, 'Problema de Sistema');
INSERT INTO tipo_solicitacao (id, nome)
VALUES (4, 'Consultoria');
INSERT INTO tipo_solicitacao (id, nome)
VALUES (5, 'Acesso a Sistema');
INSERT INTO tipo_solicitacao (id, nome)
VALUES (6, 'Manutencao de Equipamento');

-- SOLICITACOES: exatamente 30 registros
INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (1, 'Acesso ao sistema negado', 'Usuario nao consegue fazer login no sistema corporativo', 'MEDIA', 'ABERTA', 1,
        1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (2, 'Instalacao de software', 'Solicito a instalacao de ferramenta de trabalho', 'BAIXA', 'EM_APROVACAO', 2, 2);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (3, 'Erro no modulo de relatorio', 'Falha ao gerar exportacoes em PDF e XLS', 'ALTA', 'ABERTA', 3, 3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (4, 'Consultoria para otimizacao', 'Apoio para melhorar fluxo operacional do setor', 'MEDIA', 'APROVADA', 4, 4);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (5, 'Backup de dados urgente', 'Necessario backup completo antes da atualizacao', 'ALTA', 'CONCLUIDA', 1, 1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (6, 'Conta bloqueada', 'Conta bloqueada apos tentativas invalidas de acesso', 'MEDIA', 'REJEITADA', 2, 1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (7, 'Criacao de acesso', 'Novo colaborador precisa de acesso ao portal interno', 'MEDIA', 'ABERTA', 5, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (8, 'Alteracao de permissao', 'Usuario precisa de permissao adicional no sistema', 'ALTA', 'EM_APROVACAO', 6, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (9, 'Impressora sem conexao', 'Impressora do setor nao aparece na rede', 'BAIXA', 'ABERTA', 7, 6);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (10, 'Erro ao anexar arquivo', 'Sistema apresenta falha ao anexar documento PDF', 'MEDIA', 'ABERTA', 8, 3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (11, 'Solicitacao de notebook', 'Colaborador precisa de notebook para trabalho externo', 'MEDIA', 'EM_APROVACAO',
        9, 2);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (12, 'Liberacao de acesso financeiro', 'Necessario acesso ao modulo financeiro para consulta', 'ALTA',
        'APROVADA', 3, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (13, 'Sistema lento', 'Usuarios relatam lentidao ao abrir tela de solicitacoes', 'ALTA', 'ABERTA', 10, 3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (14, 'Troca de mouse', 'Mouse com defeito precisa ser substituido', 'BAIXA', 'CONCLUIDA', 4, 6);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (15, 'Acesso ao ambiente de homologacao', 'Solicito acesso ao ambiente de testes', 'MEDIA', 'APROVADA', 7, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (16, 'Recuperacao de senha', 'Usuario esqueceu a senha de acesso ao portal interno', 'BAIXA', 'CONCLUIDA', 1, 1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (17, 'Erro ao salvar solicitacao', 'Ao finalizar cadastro, sistema retorna erro inesperado', 'ALTA', 'ABERTA', 2,
        3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (18, 'Solicitacao de monitor adicional', 'Colaborador solicita segundo monitor para produtividade', 'BAIXA',
        'EM_APROVACAO', 5, 2);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (19, 'Orientacao sobre fluxo interno', 'Setor precisa de orientacao sobre abertura de solicitacoes', 'MEDIA',
        'CONCLUIDA', 6, 4);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (20, 'Permissao indevida identificada', 'Usuario possui acesso a modulo que nao deveria acessar', 'ALTA',
        'ABERTA', 8, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (21, 'Cadastro de usuario externo', 'Necessario cadastrar fornecedor com acesso limitado', 'MEDIA',
        'EM_APROVACAO', 9, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (22, 'Problema com VPN', 'Usuario nao consegue conectar na VPN corporativa', 'ALTA', 'ABERTA', 10, 1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (23, 'Atualizacao de software', 'Solicito atualizacao da ferramenta de atendimento', 'BAIXA', 'APROVADA', 2, 2);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (24, 'Falha no envio de email', 'Sistema nao envia notificacoes automaticas por email', 'ALTA', 'ABERTA', 3, 3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (25, 'Consultoria para painel gerencial', 'Apoio para definir indicadores do setor', 'MEDIA', 'CONCLUIDA', 4, 4);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (26, 'Acesso ao modulo de compras', 'Usuario precisa consultar pedidos e fornecedores', 'MEDIA', 'APROVADA', 9,
        5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (27, 'Teclado com defeito', 'Teclado apresenta falhas em algumas teclas', 'BAIXA', 'CONCLUIDA', 6, 6);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (28, 'Revisao de permissao da equipe', 'Solicito revisao dos acessos da equipe de operacoes', 'MEDIA',
        'EM_APROVACAO', 4, 5);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (29, 'Erro ao pesquisar solicitacoes', 'Filtro por setor retorna resultado incorreto', 'MEDIA', 'ABERTA', 7, 3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (30, 'Usuario inativo tentando acessar', 'Foi identificado acesso indevido no portal interno', 'ALTA',
        'REJEITADA', 10, 5);

COMMIT;