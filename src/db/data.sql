-- Colaborador 1 - Backend Especialista
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'João Silva',
           'joao.silva@futurework.com',
           'Java, Spring Boot, Microservices, Docker, Kubernetes, Oracle, REST API',
           'REMOTO',
           'ESPECIALISTA',
           1
       );

-- Colaborador 2 - Data Science
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Maria Santos',
           'maria.santos@futurework.com',
           'Python, Machine Learning, TensorFlow, Pandas, NumPy, Data Analysis',
           'HIBRIDO',
           'PARCEIRO',
           1
       );

-- Colaborador 3 - Frontend
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Pedro Costa',
           'pedro.costa@futurework.com',
           'JavaScript, React, Vue.js, Node.js, TypeScript, HTML5, CSS3',
           'REMOTO',
           'USUARIO',
           1
       );

-- Colaborador 4 - Database
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Ana Oliveira',
           'ana.oliveira@futurework.com',
           'SQL, Oracle Database, PostgreSQL, Data Modeling, PL/SQL, Performance Tuning',
           'PRESENCIAL',
           'ESPECIALISTA',
           1
       );

-- Colaborador 5 - DevOps
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Carlos Mendes',
           'carlos.mendes@futurework.com',
           'DevOps, Kubernetes, CI/CD, AWS, Azure, Jenkins, Terraform, Ansible',
           'HIBRIDO',
           'PARCEIRO',
           1
       );

-- Colaborador 6 - UX/UI
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Julia Ferreira',
           'julia.ferreira@futurework.com',
           'UI/UX Design, Figma, Adobe XD, Sketch, Prototyping, User Research',
           'REMOTO',
           'USUARIO',
           1
       );

-- Colaborador 7 - .NET
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Roberto Lima',
           'roberto.lima@futurework.com',
           'C#, .NET Core, ASP.NET, Azure, SQL Server, Entity Framework, Web API',
           'PRESENCIAL',
           'ESPECIALISTA',
           1
       );

-- Colaborador 8 - Agile/Scrum
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Fernanda Souza',
           'fernanda.souza@futurework.com',
           'Agile, Scrum Master, Kanban, Jira, Confluence, Team Management',
           'HIBRIDO',
           'PARCEIRO',
           1
       );

-- Colaborador 9 - PHP
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Lucas Almeida',
           'lucas.almeida@futurework.com',
           'PHP, Laravel, Symfony, MySQL, WordPress, API Development',
           'REMOTO',
           'INICIANTE',
           1
       );

-- Colaborador 10 - Data Analytics
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Beatriz Rodrigues',
           'beatriz.rodrigues@futurework.com',
           'Data Science, R, Statistics, Tableau, Power BI, Data Visualization',
           'HIBRIDO',
           'USUARIO',
           1
       );

-- Colaborador 11 - Mobile
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Rafael Martins',
           'rafael.martins@futurework.com',
           'Flutter, Dart, React Native, iOS, Android, Mobile UI/UX',
           'REMOTO',
           'PARCEIRO',
           1
       );

-- Colaborador 12 - Security
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Camila Pereira',
           'camila.pereira@futurework.com',
           'Cybersecurity, Penetration Testing, OWASP, Security Audit, Firewall',
           'PRESENCIAL',
           'ESPECIALISTA',
           1
       );

-- Colaborador 13 - QA
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Bruno Carvalho',
           'bruno.carvalho@futurework.com',
           'QA Testing, Selenium, Automation Testing, JUnit, TestNG, Bug Tracking',
           'HIBRIDO',
           'USUARIO',
           1
       );

-- Colaborador 14 - Cloud Architect
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Patricia Gomes',
           'patricia.gomes@futurework.com',
           'AWS, Azure, GCP, Cloud Architecture, Microservices, Serverless',
           'REMOTO',
           'ESPECIALISTA',
           1
       );

-- Colaborador 15 - Business Analyst
INSERT INTO TB_COLABORADORES (NOME, EMAIL, HABILIDADES, MODELO_TRABALHO, NIVEL_IA, ATIVO)
VALUES (
           'Marcelo Ribeiro',
           'marcelo.ribeiro@futurework.com',
           'Business Analysis, Requirements Gathering, Process Modeling, BPMN, SQL',
           'PRESENCIAL',
           'INICIANTE',
           1
       );

-- ============================================
-- COMMIT DAS TRANSAÇÕES
-- ============================================
COMMIT;

-- ============================================
-- VERIFICAÇÃO
-- ============================================
SELECT 'Dados inseridos com sucesso!' AS STATUS FROM DUAL;
SELECT COUNT(*) AS TOTAL_COLABORADORES FROM TB_COLABORADORES;
SELECT MODELO_TRABALHO, COUNT(*) AS TOTAL
FROM TB_COLABORADORES
GROUP BY MODELO_TRABALHO
ORDER BY TOTAL DESC;

SELECT NIVEL_IA, COUNT(*) AS TOTAL
FROM TB_COLABORADORES
GROUP BY NIVEL_IA
ORDER BY
    CASE NIVEL_IA
        WHEN 'INICIANTE' THEN 1
        WHEN 'USUARIO' THEN 2
        WHEN 'PARCEIRO' THEN 3
        WHEN 'ESPECIALISTA' THEN 4
        END;