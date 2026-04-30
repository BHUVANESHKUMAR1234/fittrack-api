-- ============================================================
-- V2__seed_dev_data.sql
-- Sample data for local development only.
-- Password for all users: Test@1234 (BCrypt encoded)
-- ============================================================

INSERT INTO users (id, email, password, first_name, last_name, role, is_active, streak_days)
VALUES
  (gen_random_uuid(),
   'admin@fittrack.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   'Admin', 'User', 'ADMIN', true, 0),
  (gen_random_uuid(),
   'demo@fittrack.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   'Demo', 'User', 'USER', true, 5);
