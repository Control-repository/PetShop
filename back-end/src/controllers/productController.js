const myConnection = require("../database/connect.db");

//get All products
const getAllProducts = (req, res) => {
  let query = `SELECT * from products`;
  let search = req.query.search;

  if (search) {
    query += ` WHERE name LIKE '%${search}%'`;
  }

  myConnection.query(query, [], (err, results) => {
    if (err) {
      console.log("Error getting products: ", err);
      return res.status(500).send("Failed to get product");
    }
    return res.json(results);
  });
};

//insert product
const insertProduct = (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;

  if (!name || !category || !price || !quantity) {
    res.status(400).send("Missing required fields");
    return;
  }

  const query = `INSERT INTO products(name, category, price, quantity, description, imageURL) VALUE 
  (?,?,?,?,?,?)`;

  const values = [name, category, price, quantity, description, imageURL];

  myConnection.query(query, values, (err, result) => {
    if (err) {
      console.log("Failed to insert prodcut", err);
      return res.status(500).send("Failed to insert prodcut");
    }
    res.status(200).json({ message: "Product inserted successfully" });
  });
};

//update information product
const updateProduct = (req, res) => {
  const { id, name, category, price, quantity, description, imageURL } =
    req.body;

  if (!name || !category || !price || !quantity) {
    res.status(400).send("Missing required fields");
    return;
  }

  const query = `UPDATE products SET name='${name}', category='${category}', price=${price}, quantity=${quantity}, description='${description}', imageURL='${imageURL}' WHERE id=${id}`;

  myConnection.query(query, (err, result) => {
    if (err) {
      console.log("Failed to insert prodcut", err);
      return res.status(500).send("Failed to insert prodcut");
    }
    res.status(200).json({ message: "Product inserted successfully" });
    console.log(result);
  });
};

//get one product
const getProduct = (req, res) => {
  const id = req.params.id;
  const searchQuery = `SELECT * from products WHERE id =?`;

  myConnection.query(searchQuery, [id], (err, results) => {
    if (err) {
      console.error("Error getting product:", err);
      res.status(500).send("Error getting product");
      return;
    }

    if (!results) {
      res.status(404).send("Product not found");
      return;
    }

    return res.json(results);
  });
};
// insert databse example
const insertProducts = (req, res) => {
  myConnection.insertProducts((err, results) => {
    if (err) {
      console.log("Error insert products: ", err);
      return res.status(500).json({ message: "Not insert products" });
    }

    return res.json(results);
  });
};
module.exports = {
  getAllProducts,
  insertProduct,
  insertProducts,
  getProduct,
};
