PROMPT ========================================
PROMPT FUTURE WORK - INSTALAÇÃO DO BANCO DE DADOS
PROMPT ========================================
PROMPT

-- Verificar se está conectado como SYSDBA
PROMPT Verificando permissões...
SELECT USER FROM DUAL;

PROMPT
PROMPT Criando usuário futurework_user...

-- Remover usuário se já existir (CUIDADO!)
-- DROP USER futurework_user CASCADE;

-- Criar usuário
CREATE USER futurework_user IDENTIFIED BY futurework123
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON USERS;

PROMPT Usuário criado com sucesso!
PROMPT

-- Conceder permissões
PROMPT Concedendo permissões...

GRANT CONNECT TO futurework_user;
GRANT RESOURCE TO futurework_user;
GRANT CREATE SESSION TO futurework_user;
GRANT CREATE TABLE TO futurework_user;
GRANT CREATE VIEW TO futurework_user;
GRANT CREATE SEQUENCE TO futurework_user;
GRANT CREATE TRIGGER TO futurework_user;
GRANT CREATE PROCEDURE TO futurework_user;
GRANT CREATE SYNONYM TO futurework_user;
GRANT UNLIMITED TABLESPACE TO futurework_user;

-- Permissões adicionais (opcional, mas útil)
GRANT SELECT_CATALOG_ROLE TO futurework_user;
GRANT EXECUTE ON DBMS_OUTPUT TO futurework_user;
GRANT EXECUTE ON DBMS_SQL TO futurework_user;

PROMPT Permissões concedidas com sucesso!
PROMPT

-- ============================================
-- MENSAGEM IMPORTANTE
-- ============================================
PROMPT ========================================
PROMPT INSTALAÇÃO PARTE 1 CONCLUÍDA!
PROMPT ========================================
PROMPT
PROMPT Agora execute os seguintes passos:
PROMPT
PROMPT 1. Conecte-se como futurework_user:
PROMPT    CONNECT futurework_user/futurework123@localhost:1521/XE
PROMPT
PROMPT 2. Execute os scripts na seguinte ordem:
PROMPT    @db/schema.sql
PROMPT    @db/data.sql
PROMPT    @db/procedures.sql
PROMPT    @db/views.sql
PROMPT
PROMPT ========================================
PROMPT

-- ============================================
-- VERIFICAÇÃO
-- ============================================
PROMPT Verificando usuário criado...
SELECT USERNAME, ACCOUNT_STATUS, DEFAULT_TABLESPACE, CREATED
FROM DBA_USERS
WHERE USERNAME = 'FUTUREWORK_USER';

PROMPT
PROMPT Verificando privilégios...
SELECT PRIVILEGE
FROM DBA_SYS_PRIVS
WHERE GRANTEE = 'FUTUREWORK_USER'
ORDER BY PRIVILEGE;

PROMPT
PROMPT ========================================
PROMPT Setup inicial concluído!
PROMPT ========================================