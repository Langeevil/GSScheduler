-- V6: add two-factor preference flag
ALTER TABLE user_settings
    ADD COLUMN IF NOT EXISTS two_factor_enabled BOOLEAN NOT NULL DEFAULT FALSE;
