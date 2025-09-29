CREATE TABLE IF NOT EXISTS users
(
    id          UUID PRIMARY KEY,
    keycloak_id UUID         NOT NULL, -- links to Keycloak identity
    email       VARCHAR(255) NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL, -- 'RIDER' or 'DRIVER'
    created_at  TIMESTAMP DEFAULT now()
);