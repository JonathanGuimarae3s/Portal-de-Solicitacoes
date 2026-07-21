ALTER TABLE usuario
    ADD CONSTRAINT uk_usuario_email UNIQUE (email);

ALTER TABLE solicitacao
    ADD CONSTRAINT solicitacao_status_ck
        CHECK (status IN ('ABERTA', 'EM_APROVACAO', 'APROVADA', 'REJEITADA', 'CONCLUIDA'));

ALTER TABLE solicitacao
    ADD CONSTRAINT solicitacao_prioridade_ck
        CHECK (prioridade IN ('BAIXA', 'MEDIA', 'ALTA'));

CREATE INDEX idx_usuario_setor
    ON usuario (setor);

CREATE INDEX idx_solicitacao_status
    ON solicitacao (status);

CREATE INDEX idx_solicitacao_prioridade
    ON solicitacao (prioridade);

CREATE INDEX idx_solicitacao_usuario
    ON solicitacao (usuario_id);

CREATE INDEX idx_solicitacao_tipo
    ON solicitacao (tipo_id);

-- artigos sobre indice e como melhorar a performaca
-- https://docs.oracle.com/en/database/oracle/oracle-database/19/cncpt/indexes-and-index-organized-tables.html
-- https://use-the-index-luke.com/