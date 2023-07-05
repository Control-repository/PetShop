const myConnection = require("../database/connect.db");

//get All products
const getAllProducts = (req, res) => {
  const query = "SELECT * from products";

  myConnection.query(query, (err, results) => {
    if (err) {
      console.log("Error getting products: ", err);
      return res.status(500).json({ error: "Failed to get product" });
    }

    return res.json(results);
  });
};

//insert product
const insertProduct = (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ error: "Missing required fields" });
    return;
  }

  const query = `INSERT INTO products(name, category, price, quantity, description, imageURL) VALUE 
  ('${name}','${category}',${price},${quantity},'${description}','${imageURL}')`;

  myConnection.query(query, (err, result) => {
    if (err) {
      console.log("Failed to insert prodcut", err);
      return res.status(500).json({ error: "Failed to insert prodcut" });
    }
    res
      .status(200)
      .json({ message: "Product inserted successfully", product: result });
    console.log(result);
  });
};

//update information product
const updateProduct = (req, res) => {
  const { id, name, category, price, quantity, description, imageURL } =
    req.body;

  if (!name || !category || !price || !quantity) {
    res.status(400).json({ error: "Missing required fields" });
    return;
  }

  const query = `UPDATE products SET name='${name}', category='${category}', price=${price}, quantity=${quantity}, description='${description}', imageURL='${imageURL}' WHERE id=${id}`;

  myConnection.query(query, (err, result) => {
    if (err) {
      console.log("Failed to insert prodcut", err);
      return res.status(500).json({ error: "Failed to insert prodcut" });
    }
    res
      .status(200)
      .json({ message: "Product inserted successfully", product: result });
    console.log(result);
  });
};

// insert databse example
// const insertProducts = (req, res) => {
//   myConnection.insertProducts((err, results) => {
//     if (err) {
//       console.log("Error insert products: ", err);
//       return res.status(500).json({ err: "Not insert products" });
//     }

//     return res.json(results);
//   });
// };
module.exports = {
  getAllProducts,
  insertProduct,
};
