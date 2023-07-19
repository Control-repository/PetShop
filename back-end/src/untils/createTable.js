const productTable = `CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    description TEXT,
    imageURL TEXT
  )`;
const userTable = `
  CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    fullname VARCHAR(255),
    phone VARCHAR(10),
    role INTEGER NOT NULL
  )
`;
const reset_pass_table = `
CREATE TABLE IF NOT EXISTS reset_password_token(
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_username VARCHAR(50) NOT NULL,
  token VARCHAR(255) NOT NULL,
  create_at DATE DEFAULT CURRENT_TIMESTAMP,
  expiresAt DATE DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_username) REFERENCES users(username) 
);
`;

module.exports = {
  productTable,
  userTable,
  reset_pass_table,
};
