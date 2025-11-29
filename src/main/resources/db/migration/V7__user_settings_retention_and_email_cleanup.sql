-- V7: remove email notifications toggle and replace retain_history with retention_days
ALTER TABLE user_settings
    DROP COLUMN IF EXISTS email_notifications,
    DROP COLUMN IF EXISTS retain_history,
    ADD COLUMN IF NOT EXISTS retention_days INT NOT NULL DEFAULT 30;
