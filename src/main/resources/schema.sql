CREATE TABLE securities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticker VARCHAR(50) UNIQUE NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('STOCK', 'CALL', 'PUT')),
    strike DECIMAL(10,2),
    maturity DATE
);