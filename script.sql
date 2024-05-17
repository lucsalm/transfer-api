
CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY NOT NULL,
    legal_identifier VARCHAR(14) NOT NULL UNIQUE,
    email  VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    password  VARCHAR(100) NOT NULL,
    identifier_type VARCHAR(20) CHECK (identifier_type = 'INDIVIDUAL' OR identifier_type = 'MERCHANT') NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY NOT NULL,
    payer_id SERIAL NOT NULL,
    payee_id SERIAL NOT NULL,
    "value" DECIMAL(10,2) NOT NULL,
    realized_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payer_id) REFERENCES accounts(id),
    FOREIGN KEY (payee_id) REFERENCES accounts(id)
);

INSERT INTO accounts(id,legal_identifier,email,full_name,password,identifier_type,balance) VALUES
    (1,81748010069,'ww@email.com','Walter White ','Heisenberg123','INDIVIDUAL',5000.00),
    (2,37031860000100,'lph@email.com','Los PolLos Hermanos','GusFring123','MERCHANT',10000.00)
ON CONFLICT DO NOTHING;