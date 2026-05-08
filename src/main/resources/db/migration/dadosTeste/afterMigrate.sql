-- Dados mock para ambiente local (Oracle)
-- Ordem: limpa filhos -> pais; depois insere pais -> filhos

DELETE FROM solicitacao;
DELETE FROM tipo_solicitacao;
DELETE FROM usuario;

INSERT INTO usuario (id, nome, email, setor) VALUES (1, 'Joao Silva', 'joao.silva@empresa.com', 'TI');
INSERT INTO usuario (id, nome, email, setor) VALUES (2, 'Maria Santos', 'maria.santos@empresa.com', 'RH');
INSERT INTO usuario (id, nome, email, setor) VALUES (3, 'Carlos Oliveira', 'carlos.oliveira@empresa.com', 'Financeiro');
INSERT INTO usuario (id, nome, email, setor) VALUES (4, 'Ana Costa', 'ana.costa@empresa.com', 'Operacoes');

INSERT INTO tipo_solicitacao (id, nome) VALUES (1, 'Suporte Tecnico');
INSERT INTO tipo_solicitacao (id, nome) VALUES (2, 'Solicitacao de Recurso');
INSERT INTO tipo_solicitacao (id, nome) VALUES (3, 'Problema de Sistema');
INSERT INTO tipo_solicitacao (id, nome) VALUES (4, 'Consultoria');

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (1, 'Acesso ao sistema negado', 'Usuario nao consegue fazer login no sistema', 'MEDIA', 'ABERTA', 1, 1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (2, 'Instalacao de software', 'Solicito a instalacao da ferramenta X na minha maquina', 'BAIXA', 'EM_APROVACAO', 2, 2);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (3, 'Erro no modulo de relatorio', 'Falha ao gerar exportacoes em PDF e XLS', 'ALTA', 'ABERTA', 3, 3);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (4, 'Consultoria para otimizacao', 'Apoio para melhorar fluxo operacional do setor', 'MEDIA', 'APROVADA', 4, 4);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (5, 'Backup de dados urgente', 'Necessario backup completo antes da atualizacao', 'ALTA', 'CONCLUIDA', 1, 1);

INSERT INTO solicitacao (id, titulo, descricao, prioridade, status, usuario_id, tipo_id)
VALUES (6, 'Conta bloqueada', 'Conta bloqueada apos tentativas invalidas de acesso', 'MEDIA', 'REJEITADA', 2, 1);

COMMIT;

