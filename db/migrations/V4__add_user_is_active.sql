-- ===================================================================
-- Migration V4: Add is_active column to users for Admin user management
-- ===================================================================

ALTER TABLE users ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

CREATE INDEX IF NOT EXISTS idx_users_is_active ON users(is_active);
