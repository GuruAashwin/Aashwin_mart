-- ===================================================================
-- Migration V3: Add Status Index on Orders for Fast Filtering
-- ===================================================================

CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
