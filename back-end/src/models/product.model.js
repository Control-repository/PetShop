const { query } = require("../database/connect.db");

const Product = {};

// get all product
Product.getAll = async (user, search) => {
  let questionQuery = `SELECT * FROM products WHERE user_username = '${user}'`;

  if (search) {
    questionQuery += ` AND name LIKE "%${search}%"`;
  }

  return await query(questionQuery, []);
};

//get by id
Product.getById = async (id) => {
  let questionQuery = "SELECT * FROM products WHERE id = ?";
  return await query(questionQuery, [id]);
};

//insert product
Product.insert = async (data) => {
  let questionQuery =
    "INSERT INTO products(name, category, price, quantity, description, imageURL, user_username) VALUES (?, ?, ?, ?, ?, ?, ?)";
  const values = [
    data.name,
    data.category,
    parseFloat(data.price),
    parseInt(data.quantity),
    data.description,
    data.imageURL,
    data.user_username,
  ];
  return await query(questionQuery, values);
};

//update product
Product.update = async (data) => {
  let questionQuery =
    "UPDATE products SET name = ?, category = ?, price = ?, quantity = ?, description = ?, imageURL = ?, user_username = ? WHERE id = ?";
  const values = [
    data.name,
    data.category,
    data.price,
    data.quantity,
    data.description,
    data.imageURL,
    data.user_username,
    data.id,
  ];
  return await query(questionQuery, values);
};

//remove by id
Product.remove = async (id) => {
  let questionQuery = "DELETE FROM products WHERE id = ?";
  return await query(questionQuery, [id]);
};

//remove all
Product.removeAll = async (username) => {
  let questionQuery = "DELETE FROM products WHERE user_username = ?";
  return await query(questionQuery, [username]);
};

module.exports = Product;
