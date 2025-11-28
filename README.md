# GSScheduler - Documentação do Projeto

## 📋 Visão Geral

GSScheduler é um sistema simples e intuitivo de automação e agendamento de tarefas desenvolvido com **Java Spring Boot** e **Maven**, utilizando **PostgreSQL** como banco de dados.

**Data de Criação:** 18 de novembro de 2025

---

## 🛠️ Stack Tecnológico

### Framework & Core
- **Spring Boot:** 3.5.7
- **Java:** 17 (target)
- **Maven:** 3.9.11

### Dependencies Principais
- **Spring Web:** Para REST APIs e controllers
- **Spring Data JPA:** ORM com Hibernate 6.6.33
- **Spring Security:** Autenticação com BCrypt
- **Thymeleaf:** Template engine para HTML
- **PostgreSQL:** JDBC Driver 42.7.4
- **Flyway:** Migrations de banco de dados
- **Lombok:** Annotations para getters/setters (com implementação manual)

### Banco de Dados
- **PostgreSQL:** localhost:5432/gsscheduler
- **Usuário:** usuario
- **Senha:** usuario123

---

## 📁 Estrutura do Projeto

```
GSScheduler/
├── src/
│   ├── main/
│   │   ├── java/br/com/gabrielsiqueira/GSScheduler/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   └── TaskController.java
│   │   │   ├── exceptions/
│   │   │   │   ├── CustomExceptionResponse.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/
│   │   │   │   ├── Task.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── TaskRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │    ├── MyUserDetailsService.java
│   │   │   │    └── TaskService.java
│   │   │   └── GsSchedulerApplication.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── db/migration/
│   │       │   ├── V1__create_users_and_tasks_tables.sql
│   │       │   ├── V2__insert_default_users.sql
│   │       │   └── V3__reset_default_users.sql
│   │       └── templates/
│   │           ├── dashboard.html
│   │           ├── login.html
│   │           ├── reports.html
│   │           ├── settings.html
│   │           └── tasks.html
│   └── test/
│       └── java/br/com/gabrielsiqueira/GSScheduler/
│           └── GsSchedulerApplicationTests.java
├── pom.xml
├── mvnw / mvnw.cmd
├── AUTENTICACAO.md
├── HELP.md
└── PROJETO.md (este arquivo)
```

---

## 🔐 Autenticação e Usuários

### Credenciais Padrão

O sistema cria automaticamente dois usuários na primeira execução através da migration **V2__insert_default_users.sql**:

| Usuário | Senha | Função |
|---------|-------|--------|
| admin | admin123 | ROLE_ADMIN |
| user | user123 | ROLE_USER |

### Segurança
- Senhas criptografadas com **BCrypt**
- Autenticação baseada em **Spring Security**
- Form-based login na página `/login`
- CSRF desabilitado (desenvolvimento)

---

## 📊 Modelos de Dados

### User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "enabled")
    private boolean enabled;
    
    @Column(name = "roles")
    private String roles;
}
```

### Task Entity
```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description", length = 2000)
    private String description;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

---

## 🔌 APIs REST

### TaskController - `/api/tasks`

#### GET /api/tasks
Retorna lista paginada de tarefas
- **Query Params:**
  - `page` (default: 0)
  - `size` (default: 10)
  - `sort` (default: scheduledAt,desc)
- **Response:** `Page<Task>`

#### GET /api/tasks/{id}
Retorna uma tarefa específica
- **Path Param:** `id` (Long)
- **Response:** `Task`

#### POST /api/tasks
Cria uma nova tarefa (execução assíncrona)
- **Body:** `Task`
- **Response:** `CompletableFuture<Task>`

#### PUT /api/tasks/{id}
Atualiza uma tarefa existente
- **Path Param:** `id` (Long)
- **Body:** `Task`
- **Response:** `Task`

#### DELETE /api/tasks/{id}
Remove uma tarefa
- **Path Param:** `id` (Long)
- **Response:** HTTP 204 No Content

---

## 🎯 Rotas Web (Views)

### AuthController - Navegação

| Rota | Descrição | Template |
|------|-----------|----------|
| `/login` | Página de autenticação | login.html |
| `/` | Redirecionamento para dashboard | - |
| `/dashboard` | Dashboard principal | dashboard.html |
| `/tasks` | Gerenciamento de tarefas | tasks.html |
| `/settings` | Configurações do usuário | settings.html |
| `/reports` | Relatórios e estatísticas | reports.html |

### Autorização
- `/login` e `/` - **Acesso público**
- `/dashboard`, `/tasks`, `/settings`, `/reports` - **Autenticado**
- `/api/admin/**` - **ROLE_ADMIN**
- `/api/tasks/**` - **Autenticado**

---

## 📄 Templates HTML (Thymeleaf)

### login.html
- Página de autenticação com formulário
- Exibe credenciais de teste
- Gradiente roxo com design moderno

### dashboard.html
- Dashboard principal após login
- Exibe saudação personalizada com nome do usuário
- Mostra função/role do usuário
- Cards de navegação para:
  - Minhas Tarefas
  - Configurações
  - Relatórios
- Botão de logout

### tasks.html
- Listagem de tarefas
- Botão para criar nova tarefa
- Estado vazio quando sem tarefas
- Design responsivo

### settings.html
- Perfil do usuário (nome e função)
- Preferências gerais (tema, idioma)
- Notificações
- Autenticação de dois fatores (2FA)
- Configurações de agendamento
- Segurança (alterar senha)

### reports.html
- Filtros por data e status
- Estatísticas de tarefas (total, concluídas, pendentes, taxa)
- Gráfico por status
- Tabela de tarefas por mês
- Design com placeholders para funcionalidades futuras

---

## 🗄️ Banco de Dados

### Migrations (Flyway)

#### V1__create_users_and_tasks_tables.sql
Cria as tabelas do sistema na primeira execução:

**Tabela: users**
- `id` (SERIAL PRIMARY KEY)
- `username` (VARCHAR 100 UNIQUE)
- `password` (VARCHAR 255)
- `enabled` (BOOLEAN DEFAULT TRUE)
- `roles` (VARCHAR 20)
- Índice: `idx_users_username`

**Tabela: tasks**
- `id` (SERIAL PRIMARY KEY)
- `title` (VARCHAR 255 NOT NULL)
- `description` (VARCHAR 2000)
- `status` (VARCHAR 100 NOT NULL)
- `scheduled_at` (TIMESTAMP)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)
- Índice: `idx_tasks_status`

#### V2__insert_default_users.sql
Insere usuários padrão com senhas criptografadas:
- admin / admin123 (ROLE_ADMIN)
- user / user123 (ROLE_USER)
- Usa `ON CONFLICT DO NOTHING` para idempotência

---

## ⚙️ Configuração (application.yaml)

```yaml
server:
  port: 8888

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gsscheduler
    username: usuario
    password: usuario123
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false

  flyway:
    enabled: true
    locations: classpath:db/migration

  security:
    user:
      name: admin
      password: admin123

logging:
  level:
    root: INFO
    org.springframework.security: DEBUG
    org.flywaydb: DEBUG
```

**Notas Importantes:**
- `ddl-auto: validate` - Não cria tabelas (Flyway gerencia o schema)
- `PostgreSQLDialect` - Dialeto correto para PostgreSQL
- `Flyway` ativado com migrations em `db/migration`

---

## 🚀 Como Executar

### Pré-requisitos
1. **Java 17+** instalado
2. **Maven 3.9.11+** instalado
3. **PostgreSQL** rodando em `localhost:5432`
4. Banco de dados `gsscheduler` criado

### Iniciar o Banco de Dados (Docker)
```bash
docker run -d \
  -e POSTGRES_PASSWORD=usuario123 \
  -e POSTGRES_DB=gsscheduler \
  -p 5432:5432 \
  postgres:latest
```

### Compilar o Projeto
```bash
./mvnw clean compile
```

### Executar a Aplicação
```bash
./mvnw spring-boot:run
```

### Acessar a Aplicação
- **URL:** http://localhost:8888/login
- **Usuário:** admin
- **Senha:** admin123

---

## 📝 Serviços Implementados

### TaskService
- Gerenciamento de tarefas com operações CRUD
- **Métodos principais:**
  - `listAll(Pageable)` - Lista paginada
  - `listByStatus(String status, Pageable)` - Filtra por status
  - `createAsync(Task)` - Criação assíncrona
  - `save(Task)` - Salva tarefa
  - `findById(Long)` - Busca por ID
  - `delete(Long)` - Remove tarefa

### SecurityConfig
- Configuração de autenticação Spring Security
- UserDetailsService customizado
- Carregamento de usuários do banco
- Criptografia BCrypt
- FilterChain com regras de autorização

---

## 🔧 Tratamento de Exceções

### GlobalExceptionHandler
- `@ControllerAdvice` para tratamento global
- Captura todas as exceções genéricas
- Retorna `CustomExceptionResponse` em JSON
- HTTP Status 500 por padrão

### CustomExceptionResponse
- DTO com campos: `timeStamp`, `message`, `details`
- Serialização automática para JSON

---

## 📋 Mudanças Implementadas

### 1. Estrutura e Configuração
- ✅ Criado projeto Maven com Spring Boot 3.5.7
- ✅ Configurado application.yaml com propriedades corretas
- ✅ Integração com PostgreSQL e Flyway

### 2. Camada de Dados
- ✅ Entities: User.java e Task.java com getters/setters manuais
- ✅ Repositories: UserRepository e TaskRepository
- ✅ Migrations Flyway: V1 (schema) e V2 (dados padrão)

### 3. Lógica de Negócio
- ✅ TaskService com CRUD e operações assíncronas
- ✅ Paginação com Spring Data Page/Pageable
- ✅ Timestamps automáticos (createdAt, updatedAt)

### 4. Segurança
- ✅ Spring Security com autenticação por formulário
- ✅ BCrypt para criptografia de senhas
- ✅ Usuários padrão (admin e user)
- ✅ Roles (ROLE_ADMIN, ROLE_USER)

### 5. Controllers
- ✅ TaskController com REST endpoints CRUD
- ✅ AuthController com rotas de navegação
- ✅ GlobalExceptionHandler para erros

### 6. Interface Web (Thymeleaf)
- ✅ login.html - Página de autenticação
- ✅ dashboard.html - Dashboard principal
- ✅ tasks.html - Gerenciamento de tarefas
- ✅ settings.html - Configurações
- ✅ reports.html - Relatórios

---

## 🐛 Problemas Resolvidos

### 1. Lombok não gerava métodos
- **Causa:** Scope=provided conflitava com compilação
- **Solução:** Implementação manual de todos os getters/setters

### 2. YAML com chaves incorretas
- **Problema:** `pa`, `vc`, `hidden method` não eram propriedades Spring válidas
- **Solução:** Corrigidos para `jpa`, `mvc`, etc.

### 3. Hibernate usando dialeto errado
- **Problema:** PostgreSQL10Dialect não existe em Hibernate 6.x
- **Solução:** Alterado para PostgreSQLDialect

### 4. CommandLineRunner tentava inserir antes de Flyway
- **Problema:** Tabelas não existiam quando migrations rodavam
- **Solução:** Remover CommandLineRunner, deixar Flyway gerenciar

### 5. Login exibia JSON em vez de HTML
- **Problema:** Redirecionava para `/api/tasks` (REST endpoint)
- **Solução:** Criar `/dashboard` que retorna template HTML

---

## 📈 Próximos Passos (Future Work)

- [ ] Implementar criação de tarefas na UI (POST form)
- [ ] Conectar API REST com JavaScript AJAX
- [ ] Implementar gráficos em relatórios
- [ ] Adicionar testes unitários e integração
- [ ] Implementar dois fatores (2FA)
- [ ] Adicionar notificações por email
- [ ] Criar sistema de categorias para tarefas
- [ ] Implementar busca e filtros avançados
- [ ] CSRF tokens para produção
- [ ] Rate limiting nas APIs

---

## 📚 Referências

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Flyway Documentation](https://flywaydb.org/)
- [Thymeleaf Guide](https://www.thymeleaf.org/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

---

**Última atualização:** 18 de novembro de 2025
