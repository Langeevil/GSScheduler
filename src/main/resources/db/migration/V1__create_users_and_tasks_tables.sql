-- V1__create_users_and_tasks_tables.sql
-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    roles VARCHAR(20) NOT NULL
);

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(100) NOT NULL,
    scheduled_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create index on username for faster queries
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Create index on task status for faster queries
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);
