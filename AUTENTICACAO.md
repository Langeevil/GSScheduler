# GSScheduler - Sistema de Autenticação

## Credenciais Padrão

Ao iniciar a aplicação pela primeira vez, dois usuários são criados automaticamente:

### Usuário Administrador
- **Username:** `admin`
- **Password:** `admin123`
- **Permissões:** Acesso total ao sistema (ROLE_ADMIN)

### Usuário Padrão
- **Username:** `user`
- **Password:** `user123`
- **Permissões:** Acesso limitado (ROLE_USER)

## Como Fazer Login

1. Acesse: `http://localhost:8888/login`
2. Digite o usuário e senha
3. Clique em "Entrar"

## Fluxo de Acesso

- **Página de Login:** `/login`
- **Home (Tasks):** `/api/tasks`
- **Endpoints Admin:** `/api/admin/**` (apenas ROLE_ADMIN)
- **Endpoints de Tarefas:** `/api/tasks/**` (autenticado)

## Estrutura de Autenticação

### Componentes Implementados:

1. **User Entity** - Modelo de usuário com campos:
   - username (único)
   - password (criptografado com BCrypt)
   - email
   - fullName
   - role (ROLE_ADMIN ou ROLE_USER)
   - enabled (ativo/inativo)

2. **UserRepository** - Acesso aos dados de usuários

3. **SecurityConfig** - Configuração de segurança:
   - Autenticação baseada em formulário
   - Autorização por role
   - Criptografia BCrypt
   - Inicialização automática de usuários padrão

4. **AuthController** - Controlador de autenticação

5. **Login Template (Thymeleaf)** - Página de login moderna

## Mudando Senhas

Para alterar a senha de um usuário, você precisa usar um endpoint POST customizado (a ser implementado):

```java
@PostMapping("/api/users/{id}/change-password")
public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
    // Implementar lógica de mudança de senha
}
```

## Notas de Segurança

- As senhas são armazenadas criptografadas com BCrypt
- CSRF está desabilitado em desenvolvimento (habilitar em produção)
- Spring Security está configurado para autenticação por session
- Endpoints específicos requerem autenticação e roles apropriados

## Próximos Passos Recomendados

1. Implementar endpoint de registro de novos usuários
2. Adicionar endpoint de mudança de senha
3. Implementar logout com invalidação de session
4. Adicionar validação de email
5. Implementar autenticação JWT para API REST pura
