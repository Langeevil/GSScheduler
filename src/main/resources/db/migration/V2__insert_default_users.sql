-- V2__insert_default_users.sql
-- Insert default admin and user accounts
-- Passwords are already BCrypt encoded:
-- admin123 encoded = $2a$10$Usl8FYkLyY9co9Hcoibc/O2uL9G5WU8uvACkMuPBfi79Iwt6rLHoS
-- user123 encoded = $2a$10$7QhF8y4um7ncNPML2lXBRubALRcxBmb9ZJ4H7rbhOWCmuUg5iIAWi

INSERT INTO users (username, password, enabled, roles) 
VALUES ('admin', '$2a$10$Usl8FYkLyY9co9Hcoibc/O2uL9G5WU8uvACkMuPBfi79Iwt6rLHoS', true, 'ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, enabled, roles) 
VALUES ('user', '$2a$10$7QhF8y4um7ncNPML2lXBRubALRcxBmb9ZJ4H7rbhOWCmuUg5iIAWi', true, 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;
