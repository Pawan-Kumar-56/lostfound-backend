-- Create Database
CREATE DATABASE IF NOT EXISTS lost_found_db;
USE lost_found_db;

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20),
    year VARCHAR(20),
    department VARCHAR(50),
    role VARCHAR(20) DEFAULT 'USER',
    is_email_verified BOOLEAN DEFAULT FALSE,
    otp_code VARCHAR(10),
    otp_expiry TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Items Table
CREATE TABLE IF NOT EXISTS items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    location VARCHAR(200) NOT NULL,
    item_date DATE NOT NULL,
    item_type VARCHAR(20) NOT NULL,
    contact_info VARCHAR(200) NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDING',
    user_id BIGINT NOT NULL,
    posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create Indexes for Better Performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_items_user_id ON items(user_id);
CREATE INDEX idx_items_item_type ON items(item_type);
CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_posted_at ON items(posted_at);

-- Insert Sample Data (Optional)
INSERT INTO users (full_name, email, password, is_email_verified, year, department) 
VALUES ('John Doe', 'john.doe@nitkkr.ac.in', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8sX.2bVhK/a2', TRUE, '3rd Year', 'Computer Engineering')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO items (item_name, description, category, location, item_date, item_type, contact_info, user_id) 
VALUES ('iPhone 13 Pro', 'Black iPhone 13 Pro with blue case, lost near library', 'ELECTRONICS', 'Library', '2024-01-15', 'LOST', 'john.doe@nitkkr.ac.in', 1)
ON DUPLICATE KEY UPDATE item_name = item_name;
