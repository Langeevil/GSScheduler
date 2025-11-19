-- V2__insert_default_users.sql
-- Insert default admin and user accounts
-- Passwords are already BCrypt encoded:
-- admin123 encoded = $2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm
-- user123 encoded = $2a$10$DQv5rKoR5eXFUKXxUxK7puxRqFfQ8SpLa1iUx24dXdU4f5j5esjwm

INSERT INTO users (username, password, enabled, roles) 
VALUES ('admin', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', true, 'ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, enabled, roles) 
VALUES ('user', '$2a$10$DQv5rKoR5eXFUKXxUxK7puxRqFfQ8SpLa1iUx24dXdU4f5j5esjwm', true, 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;
