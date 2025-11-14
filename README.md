# 🚀 Future Work - Sistema de Gestão de Colaboradores

<div align="center">

![Java](https://img.shields.io/badge/Java-11-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.5-brightgreen?style=for-the-badge&logo=spring)
![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=for-the-badge&logo=oracle)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=for-the-badge&logo=apache-maven)
![Status](https://img.shields.io/badge/Status-Ativo-success?style=for-the-badge)

**Sistema completo para gestão de colaboradores com foco no futuro do trabalho**

[Funcionalidades](#-funcionalidades) •
[Tecnologias](#-tecnologias) •
[Instalação](#-instalação) •
[Uso](#-como-usar) •
[API](#-api-rest) •
[Contribuir](#-contribuindo)

</div>

---

## 📋 Sobre o Projeto

O **Future Work** é uma aplicação web desenvolvida em Spring Boot que permite gerenciar colaboradores considerando as novas dinâmicas do mercado de trabalho. O sistema contempla:

- **Modelos de Trabalho**: Remoto, Híbrido e Presencial
- **Níveis de IA**: Classificação do domínio de Inteligência Artificial (Iniciante, Usuário, Parceiro, Especialista)
- **Gestão de Habilidades**: Registro e busca por competências técnicas
- **Interface Web Completa**: Páginas responsivas com Thymeleaf e Bootstrap
- **API REST**: Endpoints para integração com outras aplicações

---

## ✨ Funcionalidades

### 🖥️ Interface Web (Thymeleaf)

- ✅ Listagem paginada de colaboradores
- ✅ Criação e edição de colaboradores
- ✅ Visualização detalhada de informações
- ✅ Exclusão lógica (soft delete)
- ✅ Filtros de ordenação e paginação
- ✅ Validação de formulários em tempo real
- ✅ Mensagens de feedback (sucesso/erro)
- ✅ Design responsivo com Bootstrap 5

### 🔌 API REST

- ✅ CRUD completo de colaboradores
- ✅ Busca por modelo de trabalho
- ✅ Busca por habilidades específicas
- ✅ Paginação e ordenação customizável
- ✅ Health check endpoint
- ✅ Tratamento global de erros
- ✅ Validações automáticas
- ✅ CORS habilitado

---

## 🛠️ Tecnologias

### Backend
- **Java 11** - Linguagem de programação
- **Spring Boot 2.7.5** - Framework principal
  - Spring Data JPA - Persistência de dados
  - Spring Web - APIs REST
  - Spring Security - Segurança
  - Spring Validation - Validações
- **Hibernate** - ORM para mapeamento objeto-relacional
- **Maven** - Gerenciamento de dependências

### Frontend
- **Thymeleaf** - Template engine
- **Bootstrap 5.1.3** - Framework CSS
- **JavaScript** - Interatividade

### Banco de Dados
- **Oracle Database** (12c ou superior)
- **JDBC Driver** - Conexão com Oracle

### Ferramentas de Desenvolvimento
- **Spring DevTools** - Hot reload
- **JUnit 5** - Testes unitários
- **Mockito** - Mocks para testes

---

## 📁 Estrutura do Projeto

```
future-work-web/
├── src/
│   ├── main/
│   │   ├── java/com/example/FutureWork/
│   │   │   ├── config/              # Configurações
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/          # Controllers
│   │   │   │   ├── ColaboradorController.java       (Web)
│   │   │   │   ├── ColaboradorRestController.java   (API)
│   │   │   │   └── HomeController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   └── ColaboradorDTO.java
│   │   │   ├── exception/           # Tratamento de erros
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/               # Entidades JPA
│   │   │   │   ├── Colaborador.java
│   │   │   │   ├── ModeloTrabalho.java  (enum)
│   │   │   │   └── NivelIA.java         (enum)
│   │   │   ├── repository/          # Acesso a dados
│   │   │   │   └── ColaboradorRepository.java
│   │   │   ├── service/             # Lógica de negócio
│   │   │   │   └── ColaboradorService.java
│   │   │   └── FutureWorkApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   └── js/
│   │       │       └── script.js
│   │       ├── templates/
│   │       │   ├── colaboradores/
│   │       │   │   ├── formulario.html
│   │       │   │   ├── lista.html
│   │       │   │   └── visualizar.html
│   │       │   ├── fragments/
│   │       │   │   ├── header.html
│   │       │   │   └── footer.html
│   │       │   └── index.html
│   │       ├── application.yml
│   │       ├── messages.properties
│   │       └── messages_pt_BR.properties
│   └── test/
│       └── java/com/example/FutureWork/
│           ├── FutureWorkApplicationTests.java
│           └── service/
│               └── ColaboradorServiceTest.java
├── pom.xml
├── README.md
└── database-setup.sql
```

---

## ⚙️ Instalação

### Pré-requisitos

Certifique-se de ter instalado:

- ✅ **JDK 11** ou superior ([Download](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html))
- ✅ **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- ✅ **Oracle Database** 12c ou superior ([Download](https://www.oracle.com/database/technologies/oracle-database-software-downloads.html))
- ✅ **Git** (opcional) ([Download](https://git-scm.com/downloads))
- ✅ IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

### Passo 1: Clone o Repositório

```bash
git clone https://github.com/seu-usuario/future-work-web.git
cd future-work-web
```

### Passo 2: Configure o Banco de Dados

#### 2.1 Crie o usuário no Oracle

Conecte-se ao Oracle como SYSDBA:

```sql
sqlplus / as sysdba
```

Execute os comandos:

```sql
CREATE USER futurework_user IDENTIFIED BY futurework123;
GRANT CONNECT, RESOURCE, DBA TO futurework_user;
GRANT UNLIMITED TABLESPACE TO futurework_user;
EXIT;
```

#### 2.2 Execute o script de criação

```bash
sqlplus futurework_user/futurework123@localhost:1521/XE

@database-setup.sql
```

### Passo 3: Configure a Aplicação

Edite o arquivo `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE  # Ajuste se necessário
    username: futurework_user
    password: futurework123                    # MUDE em produção!
```

### Passo 4: Compile o Projeto

```bash
mvn clean install
```

### Passo 5: Execute a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

---

## 🚀 Como Usar

### Interface Web

#### Página Inicial
Acesse: http://localhost:8080

![Home Page](https://via.placeholder.com/800x400?text=Future+Work+Home)

#### Gerenciar Colaboradores
Acesse: http://localhost:8080/colaboradores

**Funcionalidades disponíveis:**
- 📋 Ver lista de todos os colaboradores
- ➕ Adicionar novo colaborador
- ✏️ Editar colaborador existente
- 👁️ Visualizar detalhes completos
- 🗑️ Excluir colaborador (soft delete)
- 🔍 Filtrar e ordenar resultados
- 📄 Navegar entre páginas

#### Criar Novo Colaborador

1. Clique em **"Novo Colaborador"**
2. Preencha o formulário:
   - **Nome**: Nome completo (2-100 caracteres)
   - **Email**: Email válido e único
   - **Habilidades**: Liste as competências (até 500 caracteres)
   - **Modelo de Trabalho**: Remoto, Híbrido ou Presencial
   - **Nível de IA**: Iniciante, Usuário, Parceiro ou Especialista
3. Clique em **"Criar"**

---

## 📡 API REST

### Base URL

```
http://localhost:8080/api/colaboradores
```

### Endpoints Disponíveis

#### 1. Listar Colaboradores (com paginação)

```http
GET /api/colaboradores?page=0&size=10&sort=nome
```

**Parâmetros Query:**
- `page` (opcional): Número da página (padrão: 0)
- `size` (opcional): Itens por página (padrão: 10)
- `sort` (opcional): Campo de ordenação (padrão: nome)

**Resposta (200 OK):**
```json
{
  "colaboradores": [...],
  "paginaAtual": 0,
  "totalItens": 50,
  "totalPaginas": 5,
  "tamanhoPagina": 10,
  "ordenacao": "nome"
}
```

#### 2. Buscar Colaborador por ID

```http
GET /api/colaboradores/{id}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "habilidades": "Java, Spring Boot, Microservices",
  "modeloTrabalho": "REMOTO",
  "nivelIA": "ESPECIALISTA",
  "ativo": true,
  "dataCriacao": "2024-01-15T10:30:00",
  "dataAtualizacao": "2024-01-15T10:30:00"
}
```

#### 3. Criar Colaborador

```http
POST /api/colaboradores
Content-Type: application/json
```

**Body:**
```json
{
  "nome": "Maria Santos",
  "email": "maria@example.com",
  "habilidades": "Python, Machine Learning, TensorFlow",
  "modeloTrabalho": "HIBRIDO",
  "nivelIA": "PARCEIRO"
}
```

**Resposta (201 Created):**
```json
{
  "mensagem": "Colaborador criado com sucesso!",
  "colaborador": {
    "id": 2,
    "nome": "Maria Santos",
    ...
  }
}
```

#### 4. Atualizar Colaborador

```http
PUT /api/colaboradores/{id}
Content-Type: application/json
```

**Body:** (mesmo formato do POST)

**Resposta (200 OK):**
```json
{
  "mensagem": "Colaborador atualizado com sucesso!",
  "colaborador": {...}
}
```

#### 5. Excluir Colaborador

```http
DELETE /api/colaboradores/{id}
```

**Resposta (200 OK):**
```json
{
  "mensagem": "Colaborador removido com sucesso!"
}
```

#### 6. Buscar por Modelo de Trabalho

```http
GET /api/colaboradores/modelo/{modelo}
```

**Modelos disponíveis:** `REMOTO`, `HIBRIDO`, `PRESENCIAL`

**Exemplo:**
```http
GET /api/colaboradores/modelo/REMOTO
```

#### 7. Health Check

```http
GET /api/colaboradores/health
```

**Resposta (200 OK):**
```json
{
  "status": "OK",
  "mensagem": "API Colaboradores está funcionando!",
  "timestamp": "2024-01-15T14:30:00"
}
```

### Exemplos com cURL

#### Criar Colaborador
```bash
curl -X POST http://localhost:8080/api/colaboradores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pedro Costa",
    "email": "pedro@example.com",
    "habilidades": "JavaScript, React, Node.js",
    "modeloTrabalho": "REMOTO",
    "nivelIA": "USUARIO"
  }'
```

#### Listar Todos
```bash
curl http://localhost:8080/api/colaboradores
```

#### Buscar por ID
```bash
curl http://localhost:8080/api/colaboradores/1
```

#### Atualizar
```bash
curl -X PUT http://localhost:8080/api/colaboradores/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pedro Costa Silva",
    "email": "pedro.silva@example.com",
    "habilidades": "JavaScript, React, Node.js, Vue.js",
    "modeloTrabalho": "HIBRIDO",
    "nivelIA": "PARCEIRO"
  }'
```

#### Excluir
```bash
curl -X DELETE http://localhost:8080/api/colaboradores/1
```

### Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Recurso excluído (se usar) |
| 400 | Bad Request - Dados inválidos ou email duplicado |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

---

## 📊 Modelo de Dados

### Entidade: Colaborador

| Campo | Tipo | Descrição | Restrições |
|-------|------|-----------|------------|
| id | Long | Identificador único | PK, Auto-increment |
| nome | String | Nome completo | Not null, 2-100 chars |
| email | String | Email | Not null, Unique, Valid email |
| habilidades | String | Competências | Not null, Max 500 chars |
| modeloTrabalho | Enum | Modelo de trabalho | Not null, (REMOTO/HIBRIDO/PRESENCIAL) |
| nivelIA | Enum | Nível de IA | Not null, (INICIANTE/USUARIO/PARCEIRO/ESPECIALISTA) |
| ativo | Boolean | Status | Not null, Default: true |
| dataCriacao | Timestamp | Data de criação | Auto |
| dataAtualizacao | Timestamp | Última atualização | Auto |

### Enumerações

**ModeloTrabalho:**
- `REMOTO` - Trabalho 100% remoto
- `HIBRIDO` - Mescla presencial e remoto
- `PRESENCIAL` - Trabalho presencial

**NivelIA:**
- `INICIANTE` - Conhecimento básico
- `USUARIO` - Utiliza ferramentas de IA
- `PARCEIRO` - Colabora com IA no trabalho
- `ESPECIALISTA` - Domínio avançado

---

## 🧪 Testes

### Executar Testes

```bash
mvn test
```

### Testes Implementados

- ✅ Testes unitários do Service
- ✅ Mock do Repository
- ✅ Validação de regras de negócio
- ✅ Testes de email duplicado
- ✅ Testes de soft delete

### Cobertura

Os testes cobrem:
- Criação de colaboradores
- Validação de email único
- Atualização de dados
- Exclusão lógica (soft delete)
- Busca por ID
- Listagem com paginação

---

## 🔐 Segurança

### Configurações Atuais

- Spring Security está configurado
- CSRF desabilitado (para facilitar API)
- Sessions stateless
- Todas as rotas permitidas (modo desenvolvimento)

### Para Produção

⚠️ **IMPORTANTE**: Antes de colocar em produção:

1. **Habilite autenticação**:
```java
.authorizeRequests()
    .antMatchers("/api/**").authenticated()
    .anyRequest().permitAll()
```

2. **Configure HTTPS**
3. **Use variáveis de ambiente** para senhas
4. **Habilite CSRF** se necessário
5. **Implemente JWT** para autenticação de API

---

## 🐛 Troubleshooting

### Problema: Aplicação não inicia

**Erro:** `Port 8080 already in use`

**Solução:** Mude a porta no `application.yml`:
```yaml
server:
  port: 8081
```

---

### Problema: Erro de conexão com banco

**Erro:** `Unable to acquire JDBC Connection`

**Solução:** Verifique:
1. Oracle está rodando: `lsnrctl status`
2. Credenciais corretas no `application.yml`
3. URL de conexão correta
4. Firewall não está bloqueando porta 1521

---

### Problema: Tabela não existe

**Erro:** `Table or view does not exist`

**Solução:** Execute o script SQL:
```bash
sqlplus futurework_user/futurework123@localhost:1521/XE
@database-setup.sql
```

---

### Problema: Email duplicado

**Erro:** `Email já cadastrado`

**Solução:** Este é o comportamento esperado. Use outro email ou atualize o existente.

---

## 📈 Melhorias Futuras

### Planejado para v2.0

- [ ] Autenticação JWT
- [ ] Upload de foto do colaborador
- [ ] Relatórios em PDF
- [ ] Dashboard com gráficos (Chart.js)
- [ ] Exportação para Excel
- [ ] Filtros avançados de busca
- [ ] Histórico de alterações
- [ ] Notificações por email
- [ ] Integração com LinkedIn
- [ ] API de estatísticas agregadas

### Planejado para v3.0

- [ ] Dockerização completa
- [ ] CI/CD com GitHub Actions
- [ ] Testes de integração
- [ ] Frontend React separado
- [ ] WebSockets para atualizações em tempo real
- [ ] Multi-tenancy

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Diretrizes

- Siga o padrão de código existente
- Adicione testes para novas funcionalidades
- Atualize a documentação
- Use commits descritivos

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Autor

**Enrico do Nascimento Ferreira Galdino**
RM552082 - FIAP - Análise e Desenvolvimento de Sistemas

---


<div align="center">

**⭐ Se este projeto te ajudou, considere dar uma estrela! ⭐**

Desenvolvido com ❤️ para o futuro do trabalho

</div>
