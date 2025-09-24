CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email);