const { query } = require("../database/connect.db.js");

const Product = (product) => {
  this.id = product.id;
  this.name = product.name;
  this.category = product.category;
  this.price = product.id;
  this.quantity = product.quantity;
  this.description = product.description;
  this.imageURL = product.imageURL;
  this.user_username = product.user_username;
};
// get all product
Product.getAll = (user, search, callback) => {
  let questionQuery = `SELECT * from products WHERE user_username = "${user}"`;
  if (search) {
    questionQuery += `AND name LIKE "%${search}%"`;
  }
  query(questionQuery, [], callback);
};

//get by id
Product.getById = (id, callback) => {
  let questionQuery = "SELECT * FROM products WHERE id =?";
  query(questionQuery, [id], callback);
};

//insert product
Product.insert = (data, callback) => {
  let questionQuery =
    "INSERT INTO products(name,category,price,quantity,description,imageURL,user_username) VALUES(?,?,?,?,?,?,?)";
  const values = [
    data.name,
    data.category,
    parseFloat(data.price),
    parseInt(data.quantity),
    data.description,
    data.imageURL,
    data.user_username,
  ];
  query(questionQuery, values, callback);
};

//update product
Product.update = (data, callback) => {
  let questionQuery =
    "UPDATE products SET name =?,category=?,price=?,quantity=?,description=?,imageURL=?,user_username=? WHERE id=?";
  query(
    questionQuery,
    [
      data.name,
      data.category,
      data.price,
      data.quantity,
      data.description,
      data.imageURL,
      data.user_username,
      data.id,
    ],
    callback
  );
};

//remove by id
Product.remove = (user_username, id, callback) => {
  let questionQuery = "DELETE FROM products WHERE user_username = ? AND id =?";
  query(questionQuery, [user_username, id], callback);
};

//remove all
Product.removeAll = (username, callback) => {
  let questionQuery = "DELETE FROM products WHERE user_username =?";
  query(questionQuery, [username], callback);
};
module.exports = Product;
