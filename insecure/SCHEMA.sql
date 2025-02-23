DROP DATABASE IF EXISTS vulnerable_db;
CREATE DATABASE vulnerable_db;
USE vulnerable_db;

CREATE TABLE users (
    id VARCHAR(10) PRIMARY KEY,  -- Allows text-based injection
    username VARCHAR(255),  -- Allows XSS and SQL Injection
    password VARCHAR(255),  -- Weak hashing, allows easy cracking
    balance VARCHAR(255)  -- Allows text-based injection
);

-- Insert users with plain MD5 (Weak Hashing)
INSERT INTO users (id, username, password, balance) VALUES ('1', 'admin', MD5('password123'), '25.00');
INSERT INTO users (id, username, password, balance) VALUES ('2', 'user', MD5('userpass'), '21.25');

-- Create transactions table (XSS Vulnerability)
CREATE TABLE transactions (
    id VARCHAR(10) PRIMARY KEY,  -- No AUTO_INCREMENT (Allows injection)
    username VARCHAR(255),  -- Allows XSS injection
    details TEXT,  -- Allows JavaScript execution
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -- Insert a stored XSS payload
-- INSERT INTO transactions (id, username, details) 
-- VALUES ('3', 'admin', '<script>alert("XSS Stored Attack");</script>');