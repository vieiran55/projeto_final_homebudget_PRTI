CREATE TABLE IF NOT EXISTS investimentos (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    valor_inicial DECIMAL(10,2) NOT NULL,
    valor_atual DECIMAL(10,2) NOT NULL,
    descricao TEXT,
    data DATE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_investimentos_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);