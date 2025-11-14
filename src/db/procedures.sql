CREATE OR REPLACE PROCEDURE SP_CRIAR_COLABORADOR(
    p_nome IN VARCHAR2,
    p_email IN VARCHAR2,
    p_habilidades IN VARCHAR2,
    p_modelo_trabalho IN VARCHAR2,
    p_nivel_ia IN VARCHAR2,
    p_id_colaborador OUT NUMBER
) AS
BEGIN
    -- Inserir colaborador
INSERT INTO TB_COLABORADORES (
    NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA
) VALUES (
             p_nome, p_email, p_habilidades, p_modelo_trabalho, p_nivel_ia
         ) RETURNING ID_COLABORADOR INTO p_id_colaborador;

COMMIT;

DBMS_OUTPUT.PUT_LINE('Colaborador criado com ID: ' || p_id_colaborador);

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        RAISE_APPLICATION_ERROR(-20001, 'Email já cadastrado no sistema');
WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20002, 'Erro ao criar colaborador: ' || SQLERRM);
END;
/

-- ============================================
-- 2. PROCEDURE - ATUALIZAR COLABORADOR
-- ============================================
CREATE OR REPLACE PROCEDURE SP_ATUALIZAR_COLABORADOR(
    p_id_colaborador IN NUMBER,
    p_nome IN VARCHAR2,
    p_email IN VARCHAR2,
    p_habilidades IN VARCHAR2,
    p_modelo_trabalho IN VARCHAR2,
    p_nivel_ia IN VARCHAR2
) AS
    v_count NUMBER;
BEGIN
    -- Verificar se colaborador existe
SELECT COUNT(*) INTO v_count
FROM TB_COLABORADORES
WHERE ID_COLABORADOR = p_id_colaborador AND ATIVO = 1;

IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Colaborador não encontrado');
END IF;

    -- Atualizar colaborador
UPDATE TB_COLABORADORES SET
                            NOME = p_nome,
                            EMAIL = p_email,
                            HABILIDADES = p_habilidades,
                            MODELO_TRABALHO = p_modelo_trabalho,
                            NIVEL_IA = p_nivel_ia
WHERE ID_COLABORADOR = p_id_colaborador;

COMMIT;

DBMS_OUTPUT.PUT_LINE('Colaborador ' || p_id_colaborador || ' atualizado com sucesso');

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        RAISE_APPLICATION_ERROR(-20001, 'Email já cadastrado para outro colaborador');
WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20004, 'Erro ao atualizar colaborador: ' || SQLERRM);
END;
/

-- ============================================
-- 3. PROCEDURE - DESATIVAR COLABORADOR (SOFT DELETE)
-- ============================================
CREATE OR REPLACE PROCEDURE SP_DESATIVAR_COLABORADOR(
    p_id_colaborador IN NUMBER
) AS
    v_count NUMBER;
BEGIN
    -- Verificar se colaborador existe
SELECT COUNT(*) INTO v_count
FROM TB_COLABORADORES
WHERE ID_COLABORADOR = p_id_colaborador;

IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Colaborador não encontrado');
END IF;

    -- Desativar (soft delete)
UPDATE TB_COLABORADORES
SET ATIVO = 0
WHERE ID_COLABORADOR = p_id_colaborador;

COMMIT;

DBMS_OUTPUT.PUT_LINE('Colaborador ' || p_id_colaborador || ' desativado com sucesso');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20005, 'Erro ao desativar colaborador: ' || SQLERRM);
END;
/

-- ============================================
-- 4. FUNCTION - CONTAR COLABORADORES ATIVOS
-- ============================================
CREATE OR REPLACE FUNCTION FN_TOTAL_COLABORADORES_ATIVOS
RETURN NUMBER
IS
    v_total NUMBER;
BEGIN
SELECT COUNT(*) INTO v_total
FROM TB_COLABORADORES
WHERE ATIVO = 1;

RETURN v_total;
END;
/

-- ============================================
-- 5. FUNCTION - CONTAR POR MODELO DE TRABALHO
-- ============================================
CREATE OR REPLACE FUNCTION FN_TOTAL_POR_MODELO(
    p_modelo_trabalho IN VARCHAR2
) RETURN NUMBER
IS
    v_total NUMBER;
BEGIN
SELECT COUNT(*) INTO v_total
FROM TB_COLABORADORES
WHERE MODELO_TRABALHO = p_modelo_trabalho AND ATIVO = 1;

RETURN v_total;
END;
/

-- ============================================
-- 6. FUNCTION - CONTAR POR NÍVEL DE IA
-- ============================================
CREATE OR REPLACE FUNCTION FN_TOTAL_POR_NIVEL_IA(
    p_nivel_ia IN VARCHAR2
) RETURN NUMBER
IS
    v_total NUMBER;
BEGIN
SELECT COUNT(*) INTO v_total
FROM TB_COLABORADORES
WHERE NIVEL_IA = p_nivel_ia AND ATIVO = 1;

RETURN v_total;
END;
/

-- ============================================
-- 7. FUNCTION - BUSCAR COLABORADORES POR HABILIDADE
-- ============================================
CREATE OR REPLACE FUNCTION FN_BUSCAR_POR_HABILIDADE(
    p_habilidade IN VARCHAR2
) RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
OPEN v_cursor FOR
SELECT ID_COLABORADOR, NOME, EMAIL, HABILIDADES,
       MODELO_TRABALHO, NIVEL_IA
FROM TB_COLABORADORES
WHERE UPPER(HABILIDADES) LIKE '%' || UPPER(p_habilidade) || '%'
  AND ATIVO = 1
ORDER BY NOME;

RETURN v_cursor;
END;
/

-- ============================================
-- 8. PROCEDURE - GERAR RELATÓRIO
-- ============================================
CREATE OR REPLACE PROCEDURE SP_GERAR_RELATORIO
AS
    v_total_ativos NUMBER;
    v_total_remotos NUMBER;
    v_total_hibridos NUMBER;
    v_total_presenciais NUMBER;
BEGIN
    -- Totais
SELECT COUNT(*) INTO v_total_ativos
FROM TB_COLABORADORES WHERE ATIVO = 1;

SELECT COUNT(*) INTO v_total_remotos
FROM TB_COLABORADORES WHERE MODELO_TRABALHO = 'REMOTO' AND ATIVO = 1;

SELECT COUNT(*) INTO v_total_hibridos
FROM TB_COLABORADORES WHERE MODELO_TRABALHO = 'HIBRIDO' AND ATIVO = 1;

SELECT COUNT(*) INTO v_total_presenciais
FROM TB_COLABORADORES WHERE MODELO_TRABALHO = 'PRESENCIAL' AND ATIVO = 1;

-- Exibir relatório
DBMS_OUTPUT.PUT_LINE('========================================');
    DBMS_OUTPUT.PUT_LINE('RELATÓRIO DE COLABORADORES - FUTURE WORK');
    DBMS_OUTPUT.PUT_LINE('========================================');
    DBMS_OUTPUT.PUT_LINE('Total de Colaboradores Ativos: ' || v_total_ativos);
    DBMS_OUTPUT.PUT_LINE('----------------------------------------');
    DBMS_OUTPUT.PUT_LINE('Por Modelo de Trabalho:');
    DBMS_OUTPUT.PUT_LINE('  - Remoto: ' || v_total_remotos);
    DBMS_OUTPUT.PUT_LINE('  - Híbrido: ' || v_total_hibridos);
    DBMS_OUTPUT.PUT_LINE('  - Presencial: ' || v_total_presenciais);
    DBMS_OUTPUT.PUT_LINE('========================================');
END;
/

-- ============================================
-- 9. PROCEDURE - BACKUP DE COLABORADOR
-- ============================================
CREATE OR REPLACE PROCEDURE SP_BACKUP_COLABORADOR(
    p_id_colaborador IN NUMBER
) AS
    v_nome VARCHAR2(100);
    v_email VARCHAR2(150);
    v_habilidades VARCHAR2(500);
BEGIN
SELECT NOME, EMAIL, HABILIDADES
INTO v_nome, v_email, v_habilidades
FROM TB_COLABORADORES
WHERE ID_COLABORADOR = p_id_colaborador;

DBMS_OUTPUT.PUT_LINE('=== BACKUP DO COLABORADOR ===');
    DBMS_OUTPUT.PUT_LINE('ID: ' || p_id_colaborador);
    DBMS_OUTPUT.PUT_LINE('Nome: ' || v_nome);
    DBMS_OUTPUT.PUT_LINE('Email: ' || v_email);
    DBMS_OUTPUT.PUT_LINE('Habilidades: ' || v_habilidades);
    DBMS_OUTPUT.PUT_LINE('Data: ' || TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS'));

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Colaborador não encontrado: ID ' || p_id_colaborador);
END;
/

-- ============================================
-- EXEMPLOS DE USO
-- ============================================

-- Exemplo 1: Criar colaborador
/*
DECLARE
    v_novo_id NUMBER;
BEGIN
    SP_CRIAR_COLABORADOR(
        p_nome => 'Teste Usuario',
        p_email => 'teste@exemplo.com',
        p_habilidades => 'Java, Python',
        p_modelo_trabalho => 'REMOTO',
        p_nivel_ia => 'USUARIO',
        p_id_colaborador => v_novo_id
    );
    DBMS_OUTPUT.PUT_LINE('Novo ID: ' || v_novo_id);
END;
/
*/

-- Exemplo 2: Contar colaboradores ativos
/*
SELECT FN_TOTAL_COLABORADORES_ATIVOS() AS TOTAL FROM DUAL;
*/

-- Exemplo 3: Contar por modelo
/*
SELECT
    FN_TOTAL_POR_MODELO('REMOTO') AS REMOTOS,
    FN_TOTAL_POR_MODELO('HIBRIDO') AS HIBRIDOS,
    FN_TOTAL_POR_MODELO('PRESENCIAL') AS PRESENCIAIS
FROM DUAL;
*/

-- Exemplo 4: Gerar relatório
/*
BEGIN
    SP_GERAR_RELATORIO();
END;
/
*/

-- ============================================
-- VERIFICAÇÃO
-- ============================================
SELECT 'Procedures e Functions criadas com sucesso!' AS STATUS FROM DUAL;
SELECT OBJECT_NAME, OBJECT_TYPE, STATUS
FROM USER_OBJECTS
WHERE OBJECT_TYPE IN ('PROCEDURE', 'FUNCTION')
  AND OBJECT_NAME LIKE '%COLABORADOR%'
ORDER BY OBJECT_TYPE, OBJECT_NAME;