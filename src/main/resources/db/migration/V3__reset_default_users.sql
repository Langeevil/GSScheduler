-- Reset default users with new BCrypt hashes
DELETE FROM users WHERE username IN ('admin', 'user');

INSERT INTO users (username, password, enabled, roles)
VALUES ('admin', '$2a$10$Usl8FYkLyY9co9Hcoibc/O2uL9G5WU8uvACkMuPBfi79Iwt6rLHoS', true, 'ROLE_ADMIN');

INSERT INTO users (username, password, enabled, roles)
VALUES ('user', '$2a$10$7QhF8y4um7ncNPML2lXBRubALRcxBmb9ZJ4H7rbhOWCmuUg5iIAWi', true, 'ROLE_USER');
