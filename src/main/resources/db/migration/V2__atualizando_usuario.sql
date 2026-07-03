ALTER TABLE usuario ADD (
    ativo NUMBER(1) DEFAULT 1 NOT NULL
    );

ALTER TABLE usuario ADD CONSTRAINT usuario_ativo_ck
    CHECK (ativo IN (0, 1));