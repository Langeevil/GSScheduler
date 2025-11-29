-- V8: add profile fields to users
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS first_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS last_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS email VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email) WHERE email IS NOT NULL;
