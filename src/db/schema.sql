-- ============================================
-- 1. LIMPAR OBJETOS EXISTENTES (OPCIONAL)
-- ============================================
-- CUIDADO: Descomente apenas se quiser recriar tudo do zero!
-- DROP TABLE TB_COLABORADORES CASCADE CONSTRAINTS;
-- DROP SEQUENCE COLABORADORES_SEQ;

-- ============================================
-- 2. CRIAR SEQUENCE PARA IDs AUTOMÁTICOS
-- ============================================
CREATE SEQUENCE COLABORADORES_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================
-- 3. CRIAR TABELA PRINCIPAL
-- ============================================
CREATE TABLE TB_COLABORADORES (
    -- Identificador único
                                  ID_COLABORADOR NUMBER PRIMARY KEY,

    -- Informações pessoais
                                  NOME VARCHAR2(100) NOT NULL,
                                  EMAIL VARCHAR2(150) NOT NULL UNIQUE,

    -- Informações profissionais
                                  HABILIDADES VARCHAR2(500) NOT NULL,
                                  MODELO_TRABALHO VARCHAR2(20) NOT NULL,
                                  NIVEL_IA VARCHAR2(20) NOT NULL,

    -- Controle
                                  ATIVO NUMBER(1) DEFAULT 1 NOT NULL,
                                  DATA_CRIACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  DATA_ATUALIZACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
                                  CONSTRAINT CHK_MODELO_TRABALHO CHECK (
                                      MODELO_TRABALHO IN ('REMOTO', 'HIBRIDO', 'PRESENCIAL')
                                      ),
                                  CONSTRAINT CHK_NIVEL_IA CHECK (
                                      NIVEL_IA IN ('INICIANTE', 'USUARIO', 'PARCEIRO', 'ESPECIALISTA')
                                      ),
                                  CONSTRAINT CHK_ATIVO CHECK (ATIVO IN (0, 1)),
                                  CONSTRAINT CHK_EMAIL_FORMAT CHECK (
                                      REGEXP_LIKE(EMAIL, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
                                      )
);

-- ============================================
-- 4. CRIAR ÍNDICES PARA PERFORMANCE
-- ============================================

-- Índice único no email (já criado pela constraint UNIQUE, mas explicitamos)
CREATE UNIQUE INDEX IDX_COLABORADOR_EMAIL ON TB_COLABORADORES(EMAIL);

-- Índice para filtro por colaboradores ativos
CREATE INDEX IDX_COLABORADOR_ATIVO ON TB_COLABORADORES(ATIVO);

-- Índice para filtro por modelo de trabalho
CREATE INDEX IDX_COLABORADOR_MODELO ON TB_COLABORADORES(MODELO_TRABALHO);

-- Índice para filtro por nível de IA
CREATE INDEX IDX_COLABORADOR_NIVEL ON TB_COLABORADORES(NIVEL_IA);

-- Índice composto para buscas frequentes
CREATE INDEX IDX_COLABORADOR_ATIVO_MODELO ON TB_COLABORADORES(ATIVO, MODELO_TRABALHO);

-- Índice para busca por habilidades (text search)
CREATE INDEX IDX_COLABORADOR_HABILIDADES ON TB_COLABORADORES(HABILIDADES);

-- Índice para ordenação por data de criação
CREATE INDEX IDX_COLABORADOR_DATA_CRIACAO ON TB_COLABORADORES(DATA_CRIACAO DESC);

-- ============================================
-- 5. COMENTÁRIOS NAS COLUNAS (DOCUMENTAÇÃO)
-- ============================================
COMMENT ON TABLE TB_COLABORADORES IS 'Tabela principal de colaboradores do sistema Future Work';

COMMENT ON COLUMN TB_COLABORADORES.ID_COLABORADOR IS 'Identificador único do colaborador';
COMMENT ON COLUMN TB_COLABORADORES.NOME IS 'Nome completo do colaborador (2-100 caracteres)';
COMMENT ON COLUMN TB_COLABORADORES.EMAIL IS 'Email único e válido do colaborador';
COMMENT ON COLUMN TB_COLABORADORES.HABILIDADES IS 'Habilidades técnicas do colaborador separadas por vírgula';
COMMENT ON COLUMN TB_COLABORADORES.MODELO_TRABALHO IS 'Modelo de trabalho: REMOTO, HIBRIDO ou PRESENCIAL';
COMMENT ON COLUMN TB_COLABORADORES.NIVEL_IA IS 'Nível de conhecimento em IA: INICIANTE, USUARIO, PARCEIRO ou ESPECIALISTA';
COMMENT ON COLUMN TB_COLABORADORES.ATIVO IS 'Indicador se o colaborador está ativo (1) ou inativo (0)';
COMMENT ON COLUMN TB_COLABORADORES.DATA_CRIACAO IS 'Data e hora de criação do registro';
COMMENT ON COLUMN TB_COLABORADORES.DATA_ATUALIZACAO IS 'Data e hora da última atualização';

-- ============================================
-- 6. TRIGGERS PARA AUTOMAÇÕES
-- ============================================

-- Trigger para auto-incremento do ID
CREATE OR REPLACE TRIGGER TRG_COLABORADORES_ID
BEFORE INSERT ON TB_COLABORADORES
FOR EACH ROW
BEGIN
    IF :NEW.ID_COLABORADOR IS NULL THEN
SELECT COLABORADORES_SEQ.NEXTVAL INTO :NEW.ID_COLABORADOR FROM DUAL;
END IF;
END;
/

-- Trigger para atualizar DATA_ATUALIZACAO automaticamente
CREATE OR REPLACE TRIGGER TRG_COLABORADORES_UPDATE
BEFORE UPDATE ON TB_COLABORADORES
                  FOR EACH ROW
BEGIN
    :NEW.DATA_ATUALIZACAO := CURRENT_TIMESTAMP;
END;
/

-- Trigger para log de exclusões (opcional - auditoria)
CREATE OR REPLACE TRIGGER TRG_COLABORADORES_DELETE
BEFORE UPDATE OF ATIVO ON TB_COLABORADORES
    FOR EACH ROW
    WHEN (NEW.ATIVO = 0 AND OLD.ATIVO = 1)
BEGIN
    -- Aqui você pode gravar em uma tabela de auditoria
    DBMS_OUTPUT.PUT_LINE('Colaborador ' || :OLD.NOME || ' foi desativado em ' || SYSTIMESTAMP);
END;
/

-- ============================================
-- VERIFICAÇÃO
-- ============================================
SELECT 'Schema criado com sucesso!' AS STATUS FROM DUAL;
SELECT COUNT(*) AS TOTAL_TABELAS FROM USER_TABLES WHERE TABLE_NAME = 'TB_COLABORADORES';
SELECT COUNT(*) AS TOTAL_INDICES FROM USER_INDEXES WHERE TABLE_NAME = 'TB_COLABORADORES';
SELECT COUNT(*) AS TOTAL_TRIGGERS FROM USER_TRIGGERS WHERE TABLE_NAME = 'TB_COLABORADORES';