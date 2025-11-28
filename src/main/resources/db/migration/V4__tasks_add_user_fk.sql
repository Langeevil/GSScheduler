-- V4: Add user reference to tasks
ALTER TABLE tasks
    ADD COLUMN IF NOT EXISTS user_id BIGINT;

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_user
    FOREIGN KEY (user_id) REFERENCES users(id);

CREATE INDEX IF NOT EXISTS idx_tasks_user ON tasks(user_id);
