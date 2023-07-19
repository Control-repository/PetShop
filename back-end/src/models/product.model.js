const { query } = require("../database/connect.db");
const products = require("../database/database.example");

const Product = {};

// get all product
Product.getAll = async (search) => {
  let questionQuery = `SELECT * FROM products `;

  if (search) {
    questionQuery += ` WHERE name LIKE "%${search}%"`;
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
    "INSERT INTO products(name, category, price, quantity, description, imageURL) VALUES (?, ?, ?, ?, ?, ?)";
  const values = [
    data.name,
    data.category,
    parseFloat(data.price),
    parseInt(data.quantity),
    data.description,
    data.imageURL,
  ];
  return await query(questionQuery, values);
};

//update product
Product.update = async (data) => {
  let questionQuery =
    "UPDATE products SET name = ?, category = ?, price = ?, quantity = ?, description = ?, imageURL = ? WHERE id = ?";
  const values = [
    data.name,
    data.category,
    data.price,
    data.quantity,
    data.description,
    data.imageURL,
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
Product.removeAll = async () => {
  let questionQuery = "DELETE FROM products";
  return await query(questionQuery, []);
};

//Insert database example
Product.insertExample = async () => {
  try {
    const list = [];
    for (const product of products) {
      const sql =
        "INSERT INTO products (name, category, price, quantity, description, imageURL) VALUES (?, ?, ?, ?, ?, ?)";
      const values = [
        product.name,
        product.category,
        product.price,
        product.quantity,
        product.description,
        product.imageURL,
      ];
      const result = await query(sql, values);
      list.push(result);
    }
    return list;
  } catch (error) {
    throw error;
  }
};
module.exports = Product;
