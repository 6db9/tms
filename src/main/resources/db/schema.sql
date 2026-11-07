-- Users
CREATE TABLE IF NOT EXISTS users (
     id INT AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(50) NOT NULL UNIQUE,
     password_hash VARCHAR(255) NOT NULL,
     salt VARCHAR(64) NOT NULL,
     active BOOLEAN NOT NULL DEFAULT TRUE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Roles
CREATE TABLE IF NOT EXISTS roles (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(30) NOT NULL UNIQUE
);

-- User roles
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id INT NOT NULL,
                                          role_id INT NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
    );

-- insert roles
INSERT IGNORE INTO roles (name) VALUES
  ('ADMIN'), ('COORD'), ('COMMERCIAL'), ('FINANCIER'), ('READER');

-- Clients
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

-- Vehicles
CREATE TABLE IF NOT EXISTS vehicles (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        plate VARCHAR(20) NOT NULL UNIQUE,
                                        type VARCHAR(50),
                                        capacity_kg DECIMAL(10,2),
                                        capacity_m3 DECIMAL(10,2),
                                        archived BOOLEAN NOT NULL DEFAULT FALSE
);

-- Drivers
CREATE TABLE IF NOT EXISTS drivers (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       full_name VARCHAR(100) NOT NULL,
                                       phone VARCHAR(30),
                                       license_number VARCHAR(50),
                                       license_valid_until DATE,
                                       archived BOOLEAN NOT NULL DEFAULT FALSE
);

-- Demandes
CREATE TABLE IF NOT EXISTS requests (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        type ENUM('TRANSPORT_ONLY', 'SALE_WITH_DELIVERY') NOT NULL,
                                        client_id INT NOT NULL,
                                        state ENUM('DRAFT', 'VALIDATED', 'CLOSED', 'CANCELLED') NOT NULL DEFAULT 'DRAFT',
                                        load_description VARCHAR(255),    -- Description of the goods
                                        quantity INT,                     -- Number of items
                                        weight_per_item DECIMAL(10,2),    -- Weight of one item
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Missions
CREATE TABLE IF NOT EXISTS missions (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        request_id INT NOT NULL,
                                        vehicle_id INT,
                                        driver_id INT,
                                        state ENUM('TO_PLAN', 'PLANNED', 'IN_PROGRESS', 'ARRIVED', 'WAITING', 'UNLOADING', 'DONE', 'CANCELLED') NOT NULL DEFAULT 'TO_PLAN',
                                        planned_start DATETIME,
                                        planned_end DATETIME,
                                        actual_arrival DATETIME NULL,
                                        actual_start DATETIME NULL,
                                        actual_end DATETIME NULL,
                                        FOREIGN KEY (request_id) REFERENCES requests(id),
                                        FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
                                        FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

-- Paiements
CREATE TABLE IF NOT EXISTS payments (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        request_id INT NOT NULL,
                                        amount DECIMAL(12,2) NOT NULL,
                                        state ENUM('ANNOUNCED', 'PENDING', 'CONFIRMED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'ANNOUNCED',
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (request_id) REFERENCES requests(id)
);

-- Dépenses
CREATE TABLE IF NOT EXISTS expenses (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        mission_id INT NOT NULL,
                                        category VARCHAR(50) NOT NULL,
                                        amount DECIMAL(12,2) NOT NULL,
                                        expense_date DATE NOT NULL,
                                        reference VARCHAR(100),
                                        FOREIGN KEY (mission_id) REFERENCES missions(id)
);

-- Audit Log
CREATE TABLE IF NOT EXISTS audit_log (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         user_id INT,
                                         action VARCHAR(255) NOT NULL,
                                         entity_type VARCHAR(50),
                                         entity_id INT,
                                         old_value TEXT,
                                         new_value TEXT,
                                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (user_id) REFERENCES users(id)
);