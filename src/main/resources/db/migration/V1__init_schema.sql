-- ============================================================
-- V1__init_schema.sql
-- FitTrack initial database schema
-- ============================================================

-- USERS
CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(150) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    date_of_birth   DATE,
    height_cm       NUMERIC(5,2),
    weight_kg       NUMERIC(5,2),
    role            VARCHAR(20)  NOT NULL DEFAULT 'USER',
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    streak_days     INTEGER      NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);

-- WORKOUT SESSIONS
CREATE TABLE workout_sessions (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id          UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name             VARCHAR(100) NOT NULL,
    workout_type     VARCHAR(30)  NOT NULL,
    started_at       TIMESTAMP    NOT NULL,
    ended_at         TIMESTAMP,
    duration_minutes INTEGER,
    calories_burned  INTEGER,
    notes            VARCHAR(500),
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_workout_user_date ON workout_sessions(user_id, started_at);

-- EXERCISE LOGS
CREATE TABLE exercise_logs (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workout_session_id  UUID         NOT NULL REFERENCES workout_sessions(id) ON DELETE CASCADE,
    exercise_name       VARCHAR(100) NOT NULL,
    sets                INTEGER,
    reps                INTEGER,
    weight_kg           NUMERIC(6,2),
    duration_seconds    INTEGER,
    distance_km         NUMERIC(6,3),
    order_index         INTEGER      NOT NULL DEFAULT 0
);

-- NUTRITION LOGS
CREATE TABLE nutrition_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    log_date        DATE         NOT NULL,
    meal_type       VARCHAR(20)  NOT NULL,
    food_name       VARCHAR(150) NOT NULL,
    quantity_grams  NUMERIC(7,2) NOT NULL,
    calories        INTEGER      NOT NULL,
    protein_g       NUMERIC(6,2),
    carbs_g         NUMERIC(6,2),
    fat_g           NUMERIC(6,2),
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_nutrition_user_date ON nutrition_logs(user_id, log_date);
