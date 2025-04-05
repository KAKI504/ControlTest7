CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     phone_number VARCHAR(50),
                                     enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS authorities (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           username VARCHAR(255) NOT NULL,
                                           authority VARCHAR(50) NOT NULL,
                                           CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username, authority);

CREATE TABLE IF NOT EXISTS accounts (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        user_id INT NOT NULL,
                                        currency VARCHAR(3) NOT NULL,
                                        balance DECIMAL(19, 2) NOT NULL DEFAULT 0,
                                        CONSTRAINT fk_accounts_users FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS transactions (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            from_account_id INT,
                                            to_account_id INT,
                                            amount DECIMAL(19, 2) NOT NULL,
                                            currency VARCHAR(3) NOT NULL,
                                            date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            status VARCHAR(20) NOT NULL,
                                            CONSTRAINT fk_transactions_from_account FOREIGN KEY(from_account_id) REFERENCES accounts(id),
                                            CONSTRAINT fk_transactions_to_account FOREIGN KEY(to_account_id) REFERENCES accounts(id)
);