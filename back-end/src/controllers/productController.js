const myConnection = require("../database/connect.db");

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
const insertProduct = (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;
  console.log(req.body);
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
    return res.json(result);
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
