const myConnection = require("../database/connect.db");
const product = require("../models/product.model");

//get All products
const getAllProducts = (req, res) => {
  let username = req.user.username;
  let search = req.query.search;

  product.getAll(username, search, (err, results) => {
    if (err) {
      return res.status(500).json({ message: "Failed to get product" });
    }
    return res.json({ products: results });
  });
};

//insert product
const insertProduct = (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;
  const { username } = req.user;

  const values = {
    ...req.body,
    user_username: username,
  };

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ message: "Missing required fields" });
    return;
  }

  product.insert(values, (err, result) => {
    if (err) {
      console.log(err);
      return res.status(500).json({ message: "Failed to insert product" });
    }
    res.status(200).json({ message: "Product inserted successfully" });
  });
};

//update information product
const updateProduct = (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;
  const { id } = req.params.id;

  const values = { id, ...req.body, user_username: req.user.username };

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ message: "Missing required fields" });
    return;
  }

  product.update(values, (err, result) => {
    if (err) {
      console.log("Failed to insert product", err);
      return res.status(500).json({ message: "Failed to insert product" });
    }
    res.status(200).json({ message: "Product inserted successfully" });
  });
};

//get one product
const getProduct = (req, res) => {
  const id = req.params.id;
  product.getById(id, (err, results) => {
    if (err) {
      console.error("Error getting product:", err);
      res.status(500).json({ message: "Error getting product" });
      return;
    }

    if (!results) {
      res.status(404).json({ message: "Product not found" });
      return;
    }

    return res.json(results);
  });
};
// insert databse example
const insertProducts = (req, res) => {
  myConnection.insertProducts(req, res);
};
//delete item by id
const deleteProduct = (req, res) => {
  const { id } = req.params;
  const { username } = req.user;
  product.remove(username, id, (err, result) => {
    if (err) {
      return res.status(500).json({ message: "Error to delete Product" });
    }

    return res.status(200).json({ message: "Delete successfully!" });
  });
};
//delete all product
const deleteAllProduct = (req, res) => {
  const { username } = req.user;
  product.removeAll(username, (err, result) => {
    if (err) {
      console.log("DELTE ALL", err);
      return res.status(500).json({ message: "Failed to delete Products" });
    }

    return res
      .status(200)
      .json({ message: "All products deleted successfully" });
  });
};

module.exports = {
  getAllProducts,
  insertProduct,
  insertProducts,
  updateProduct,
  getProduct,
  deleteAllProduct,
  deleteProduct,
};
