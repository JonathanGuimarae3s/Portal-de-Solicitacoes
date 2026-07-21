ALTER TABLE usuario ADD (
    senha VARCHAR2(255),
    perfil VARCHAR2(30) DEFAULT 'USUARIO' NOT NULL
);

UPDATE usuario
SET senha = '$2a$10$senha_temporaria_hash_bcrypt'
WHERE senha IS NULL;

ALTER TABLE usuario MODIFY senha VARCHAR2(255) NOT NULL;

ALTER TABLE usuario ADD CONSTRAINT usuario_perfil_ck
    CHECK (perfil IN ('ADMIN', 'GESTOR', 'USUARIO'));