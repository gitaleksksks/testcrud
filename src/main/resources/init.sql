CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    birth_date DATE,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS user_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    additional_info TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    data LONGBLOB NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (id, last_name, first_name, middle_name, birth_date, email, phone)
VALUES
    (1, 'Ivanov', 'Ivan', 'Ivanovich', '1970-01-15', 'ivan.ivanov@example.com', '+1234567890'),
    (2, 'Petrov', 'Petr', 'Petrovich', '1980-01-05', 'petr.petrov@example.com', '+9876543210')
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_details (id, user_id, additional_info)
VALUES
    (1, 1, 'Additional information for Ivan Ivanov'),
    (2, 2, 'Additional information for Petr Petrov')
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO roles (id, name)
VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 2)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
