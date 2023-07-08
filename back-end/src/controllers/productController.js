const myConnection = require("../database/connect.db");

//get All products
const getAllProducts = (req, res) => {
  let query = `SELECT * from products WHERE user_username=?`;
  let search = req.query.search;

  const user = req.user;

  myConnection.query(query, [user.username], (err, results) => {
    if (err) {
      console.log("Error getting products: ", err);
      return res.status(500).json({ message: "Failed to get product" });
    }

    if (search) {
      query += ` AND name LIKE '%${search}%'`;
    }
    myConnection.query(query, [], (err, results) => {
      if (err) {
        console.log("Error getting products: ", err);
        return res.status(500).json({ message: "Failed to get product" });
      }
      return res.json({ products: results });
    });
  });
};

//insert product
const insertProduct = (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ message: "Missing required fields" });
    return;
  }

  const query = `INSERT INTO products(name, category, price, quantity, description, imageURL) VALUE 
  (?,?,?,?,?,?)`;

  const values = [name, category, price, quantity, description, imageURL];

  myConnection.query(query, values, (err, result) => {
    if (err) {
      console.log("Failed to insert prodcut", err);
      return res.status(500).json({ message: "Failed to insert prodcut" });
    }
    res.status(200).json({ message: "Product inserted successfully" });
  });
};

//update information product
const updateProduct = (req, res) => {
  const { id, name, category, price, quantity, description, imageURL } =
    req.body;

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ message: "Missing required fields" });
    return;
  }

  const query = `UPDATE products SET name='${name}', category='${category}', price=${price}, quantity=${quantity}, description='${description}', imageURL='${imageURL}' WHERE id=${id}`;

  myConnection.query(query, (err, result) => {
    if (err) {
      console.log("Failed to insert prodcut", err);
      return res.status(500).json({ message: "Failed to insert prodcut" });
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
//delete product
const deleteAllProduct = (req, res) => {
  const query = `DELETE FROM products`;
  myConnection.query(query, [], (err, result) => {
    if (err) {
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
  getProduct,
  deleteAllProduct,
};
