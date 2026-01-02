CREATE TABLE clients (
id CHAR(36) NOT NULL PRIMARY KEY,
names VARCHAR(80) NOT NULL,
lastnames VARCHAR(80) NOT NULL,
age INT NOT NULL,
birthdate CURRENT_TIMESTAMP NOT NULL,
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT chk_edad CHECK (age >= 0 AND age <= 130)
);

CREATE INDEX idx_clients_lastnames ON clientes(apellido);

-- Uncomment if queries need to be optimized by date of birth...
-- CREATE INDEX idx_clients_birthdate ON clientes(birthdate);
