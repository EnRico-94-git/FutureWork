-- ============================================
-- FUTURE WORK - SCRIPT AZURE SQL ATUALIZADO
-- Alinha com o novo repositório (sequence + tabela + views + procs)
-- ============================================

------------------------------------------------
-- 0) LIMPAR OBJETOS ANTIGOS (NA ORDEM CERTA)
------------------------------------------------
IF OBJECT_ID('dbo.VW_ESPECIALISTAS_IA', 'V') IS NOT NULL
DROP VIEW dbo.VW_ESPECIALISTAS_IA;
GO

IF OBJECT_ID('dbo.VW_COLABORADORES_REMOTOS', 'V') IS NOT NULL
DROP VIEW dbo.VW_COLABORADORES_REMOTOS;
GO

IF OBJECT_ID('dbo.VW_COLABORADORES_POR_NIVEL_IA', 'V') IS NOT NULL
DROP VIEW dbo.VW_COLABORADORES_POR_NIVEL_IA;
GO

IF OBJECT_ID('dbo.VW_COLABORADORES_ATIVOS', 'V') IS NOT NULL
DROP VIEW dbo.VW_COLABORADORES_ATIVOS;
GO

IF OBJECT_ID('dbo.SP_GERAR_RELATORIO', 'P') IS NOT NULL
DROP PROCEDURE dbo.SP_GERAR_RELATORIO;
GO

IF OBJECT_ID('dbo.SP_ATUALIZAR_HABILIDADES', 'P') IS NOT NULL
DROP PROCEDURE dbo.SP_ATUALIZAR_HABILIDADES;
GO

IF OBJECT_ID('dbo.SP_DESATIVAR_COLABORADOR', 'P') IS NOT NULL
DROP PROCEDURE dbo.SP_DESATIVAR_COLABORADOR;
GO

IF OBJECT_ID('dbo.TRG_COLABORADORES_UPDATE', 'TR') IS NOT NULL
DROP TRIGGER dbo.TRG_COLABORADORES_UPDATE;
GO

IF OBJECT_ID('dbo.TB_COLABORADORES', 'U') IS NOT NULL
DROP TABLE dbo.TB_COLABORADORES;
GO

IF OBJECT_ID('COLABORADORES_SEQ', 'SO') IS NOT NULL
DROP SEQUENCE COLABORADORES_SEQ;
GO

-----------------------------------
-- 1) SEQUENCE (como no novo repo)
-----------------------------------
CREATE SEQUENCE COLABORADORES_SEQ
    AS BIGINT
    START WITH 1
    INCREMENT BY 1;
GO

-----------------------------------
-- 2) TABELA PRINCIPAL
-----------------------------------
CREATE TABLE dbo.TB_COLABORADORES (
                                      ID_COLABORADOR   BIGINT        NOT NULL
                                          CONSTRAINT PK_TB_COLABORADORES PRIMARY KEY,
                                      NOME             NVARCHAR(100)  NOT NULL,
                                      EMAIL            NVARCHAR(150)  NOT NULL UNIQUE,
                                      HABILIDADES      NVARCHAR(500)  NOT NULL,
                                      MODELO_TRABALHO  NVARCHAR(20)   NOT NULL
        CONSTRAINT CHK_MODELO_TRABALHO
            CHECK (MODELO_TRABALHO IN ('REMOTO', 'HIBRIDO', 'PRESENCIAL')),
                                      NIVEL_IA         NVARCHAR(20)   NOT NULL
        CONSTRAINT CHK_NIVEL_IA
            CHECK (NIVEL_IA IN ('INICIANTE', 'USUARIO', 'PARCEIRO', 'ESPECIALISTA')),
                                      ATIVO            BIT            NOT NULL
                                          CONSTRAINT CHK_ATIVO CHECK (ATIVO IN (0,1))
                                          CONSTRAINT DF_TB_COLABORADORES_ATIVO DEFAULT (1),
                                      DATA_CRIACAO     DATETIME2      NOT NULL
                                          CONSTRAINT DF_TB_COLABORADORES_CRIACAO DEFAULT (SYSUTCDATETIME()),
                                      DATA_ATUALIZACAO DATETIME2      NOT NULL
                                          CONSTRAINT DF_TB_COLABORADORES_ATUALIZACAO DEFAULT (SYSUTCDATETIME()),
                                      CONSTRAINT CHK_EMAIL_FORMAT CHECK (EMAIL LIKE '%_@_%._%')
);
GO

-----------------------------------
-- 3) TRIGGER DE AUDITORIA
-----------------------------------
CREATE TRIGGER dbo.TRG_COLABORADORES_UPDATE
    ON dbo.TB_COLABORADORES
    AFTER UPDATE
              AS
BEGIN
    SET NOCOUNT ON;

UPDATE c
SET DATA_ATUALIZACAO = SYSUTCDATETIME()
    FROM dbo.TB_COLABORADORES c
    INNER JOIN inserted i
ON c.ID_COLABORADOR = i.ID_COLABORADOR;
END;
GO

-----------------------------------
-- 4) ÍNDICES
-----------------------------------
CREATE INDEX IDX_COLABORADOR_EMAIL  ON dbo.TB_COLABORADORES(EMAIL);
CREATE INDEX IDX_COLABORADOR_ATIVO  ON dbo.TB_COLABORADORES(ATIVO);
CREATE INDEX IDX_COLABORADOR_MODELO ON dbo.TB_COLABORADORES(MODELO_TRABALHO);
CREATE INDEX IDX_COLABORADOR_NIVEL  ON dbo.TB_COLABORADORES(NIVEL_IA);
GO

-----------------------------------
-- 5) VIEWS (adaptadas de views.sql)
-----------------------------------
CREATE VIEW dbo.VW_COLABORADORES_ATIVOS AS
SELECT
    ID_COLABORADOR,
    NOME,
    EMAIL,
    HABILIDADES,
    MODELO_TRABALHO,
    NIVEL_IA,
    DATA_CRIACAO
FROM dbo.TB_COLABORADORES
WHERE ATIVO = 1;
GO

CREATE VIEW dbo.VW_COLABORADORES_POR_NIVEL_IA AS
SELECT
    NIVEL_IA,
    COUNT(*) AS TOTAL
FROM dbo.TB_COLABORADORES
WHERE ATIVO = 1
GROUP BY NIVEL_IA;
GO

CREATE VIEW dbo.VW_COLABORADORES_REMOTOS AS
SELECT
    ID_COLABORADOR,
    NOME,
    EMAIL,
    NIVEL_IA
FROM dbo.TB_COLABORADORES
WHERE ATIVO = 1
  AND MODELO_TRABALHO = 'REMOTO';
GO

CREATE VIEW dbo.VW_ESPECIALISTAS_IA AS
SELECT
    ID_COLABORADOR,
    NOME,
    EMAIL,
    MODELO_TRABALHO
FROM dbo.TB_COLABORADORES
WHERE ATIVO = 1
  AND NIVEL_IA = 'ESPECIALISTA';
GO

-----------------------------------
-- 6) PROCEDURES (adaptadas de procedures.sql)
-----------------------------------
CREATE PROCEDURE dbo.SP_DESATIVAR_COLABORADOR
    @P_ID_COLABORADOR BIGINT
AS
BEGIN
    SET NOCOUNT ON;

UPDATE dbo.TB_COLABORADORES
SET ATIVO = 0
WHERE ID_COLABORADOR = @P_ID_COLABORADOR;
END;
GO

CREATE PROCEDURE dbo.SP_ATUALIZAR_HABILIDADES
    @P_ID_COLABORADOR   BIGINT,
    @P_NOVAS_HABILIDADES NVARCHAR(500)
AS
BEGIN
    SET NOCOUNT ON;

UPDATE dbo.TB_COLABORADORES
SET HABILIDADES = @P_NOVAS_HABILIDADES
WHERE ID_COLABORADOR = @P_ID_COLABORADOR;
END;
GO

CREATE PROCEDURE dbo.SP_GERAR_RELATORIO
    AS
BEGIN
    SET NOCOUNT ON;

SELECT
    ID_COLABORADOR,
    NOME,
    EMAIL,
    NIVEL_IA,
    MODELO_TRABALHO,
    DATA_CRIACAO
FROM dbo.TB_COLABORADORES
WHERE ATIVO = 1
ORDER BY DATA_CRIACAO DESC;
END;
GO

-----------------------------------
-- 7) DADOS DE EXEMPLO (pode ajustar se quiser)
-----------------------------------
INSERT INTO dbo.TB_COLABORADORES
    (ID_COLABORADOR, NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES
(NEXT VALUE FOR COLABORADORES_SEQ, 'João Silva',      'joao.silva@futurework.com',      'Java, Spring Boot, Microservices, Docker',              'REMOTO',     'ESPECIALISTA', 1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Maria Santos',    'maria.santos@futurework.com',    'Python, Machine Learning, TensorFlow, Pandas',          'HIBRIDO',    'PARCEIRO',     1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Pedro Costa',     'pedro.costa@futurework.com',     'JavaScript, React, Node.js, MongoDB',                  'REMOTO',     'USUARIO',      1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Ana Oliveira',    'ana.oliveira@futurework.com',    'SQL, Oracle, PostgreSQL, Data Modeling',               'PRESENCIAL', 'ESPECIALISTA', 1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Carlos Mendes',   'carlos.mendes@futurework.com',   'DevOps, Kubernetes, CI/CD, AWS',                       'HIBRIDO',    'PARCEIRO',     1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Julia Ferreira',  'julia.ferreira@futurework.com',  'UI/UX Design, Figma, Adobe XD, HTML/CSS',              'REMOTO',     'USUARIO',      1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Roberto Lima',    'roberto.lima@futurework.com',    'C#, .NET Core, Azure, SQL Server',                     'PRESENCIAL', 'ESPECIALISTA', 1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Fernanda Souza',  'fernanda.souza@futurework.com',  'Agile, Scrum, Kanban, Jira',                           'HIBRIDO',    'PARCEIRO',     1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Lucas Almeida',   'lucas.almeida@futurework.com',   'PHP, Laravel, MySQL, WordPress',                       'REMOTO',     'INICIANTE',    1),
(NEXT VALUE FOR COLABORADORES_SEQ, 'Beatriz Rodrigues','beatriz.rodrigues@futurework.com',
                                                            'Data Science, R, Statistics, Tableau',             'HIBRIDO',    'USUARIO',      1);
GO

-----------------------------------
-- 8) PERMISSÕES BÁSICAS
-----------------------------------
GRANT SELECT, INSERT, UPDATE, DELETE ON dbo.TB_COLABORADORES         TO PUBLIC;
GRANT SELECT ON dbo.VW_COLABORADORES_ATIVOS                          TO PUBLIC;
GRANT SELECT ON dbo.VW_COLABORADORES_POR_NIVEL_IA                    TO PUBLIC;
GRANT SELECT ON dbo.VW_COLABORADORES_REMOTOS                         TO PUBLIC;
GRANT SELECT ON dbo.VW_ESPECIALISTAS_IA                              TO PUBLIC;
GO

-----------------------------------
-- 9) VERIFICAÇÃO RÁPIDA
-----------------------------------
SELECT 'Objetos criados com sucesso!' AS STATUS;

SELECT COUNT(*) AS TOTAL_REGISTROS
FROM dbo.TB_COLABORADORES;
GO
