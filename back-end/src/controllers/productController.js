const product = require("../models/product.model");

//get All products
const getAllProducts = async (req, res) => {
  let { username } = req.user;
  let search = req.query.search;
  try {

    const results = await product.getAll(username, search);
    if (!results) {
      return res.status(400).json({ message: "Failed to get product" });
    }
    return res.status(200).json({ products: results });
  } catch (error) {
    console.log("PRODUCT ERROR: ", error);
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

//insert product
const insertProduct = async (req, res) => {
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

  try {
    const result = await product.insert(values);
    if (!result) {
      return res.status(500).json({ message: "Failed to insert product" });
    }

    return res.status(200).json({ message: "Product inserted successfully" });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

//update information product
const updateProduct = async (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;
  const { id } = req.params.id;

  const values = { id, ...req.body, user_username: req.user.username };

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ message: "Missing required fields" });
    return;
  }
  try {
    const result = await product.update(values);
    if (!result) {
      return res.status(500).json({ message: "Failed to update product" });
    }

    return res.status(200).json({ message: "Product inserted successfully" });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

//get one product
const getProduct = async (req, res) => {
  const id = req.params.id;
  try {
    const result = product.getById(id);
    if (!result) {
      res.status(404).json({ message: "Product not found" });
    }
    return res.json({ product: result });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
};
// insert databse example
const insertProducts = (req, res) => {
  myConnection.insertProducts(req, res);
};
//delete item by id
const deleteProduct = async (req, res) => {
  const { id } = req.params;
  try {
    await product.remove(id);
    return res.status(200).json({ message: "Delete successfully!" });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
};
//delete all product
const deleteAllProduct = async (req, res) => {
  const { username } = req.user;
  try {
    await product.removeAll(username);
    return res
      .status(200)
      .json({ message: "All products deleted successfully" });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
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
