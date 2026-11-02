CREATE DATABASE IF NOT EXISTS tms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tms;

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS roles (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(30) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id INT NOT NULL,
                                          role_id INT NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
    );

INSERT IGNORE INTO roles (name) VALUES
  ('ADMIN'), ('COORD'), ('COMMERCIAL'), ('FINANCIER'), ('READER');

CREATE TABLE IF NOT EXISTS clients (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
                                       phone VARCHAR(30),
                                       email VARCHAR(100),
                                       address VARCHAR(255),
                                       tax_id VARCHAR(50),
                                       archived BOOLEAN NOT NULL DEFAULT FALSE,
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS vehicles (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        plate VARCHAR(20) NOT NULL UNIQUE,
                                        type VARCHAR(50),
                                        capacity_kg DECIMAL(10,2),
                                        capacity_m3 DECIMAL(10,2),
                                        archived BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS drivers (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       full_name VARCHAR(100) NOT NULL,
                                       phone VARCHAR(30),
                                       license_number VARCHAR(50),
                                       license_valid_until DATE,
                                       archived BOOLEAN NOT NULL DEFAULT FALSE
);

-- F3: Demandes
CREATE TABLE IF NOT EXISTS requests (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        type ENUM('TRANSPORT_ONLY', 'SALE_WITH_DELIVERY') NOT NULL,
                                        client_id INT NOT NULL,
                                        state ENUM('DRAFT', 'VALIDATED', 'CLOSED', 'CANCELLED') NOT NULL DEFAULT 'DRAFT',
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (client_id) REFERENCES clients(id)
);