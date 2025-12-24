CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    keycloak_user_id VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_users_keycloak_user_id ON users (keycloak_user_id);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_username ON users (username);