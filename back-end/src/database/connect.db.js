const mysql = require("mysql");
const products = require("./database.example.js");
const createTable = require("./createTable.js");
const dotenv = require("dotenv");

dotenv.config();

const database_name = process.env.DB_NAME;

const connection = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: null,
});

connection.connect((err) => {
  if (err) {
    console.log("Error connecting to MySQL: ", err);
    return;
  }
  console.log("Connect to Database MySQL");

  //Create new database
  const createDatabaseQuery = `CREATE DATABASE IF NOT EXISTS ${database_name}`;

  connection.query(createDatabaseQuery, (err) => {
    if (err) {
      console.log("Error creating database");
      connection.end();
      return;
    }
    console.log(`Created a new database named "${database_name}"`);

    // Switch to the created database
    connection.changeUser({ database: database_name }, (err) => {
      if (err) {
        console.error("Error switching to the database:", err);

        connection.end();
        return;
      }
      console.log("Switched to the database");

      //Create new table products
      connection.query(createTable.productTable, (err) => {
        if (err) {
          console.log("Error creating table ", err);
          return;
        } else {
          console.log("Table created ");
        }
      });
    });
  });
});

const query = (sql, callback) => {
  connection.query(sql, (err, result) => {
    if (err) {
      callback(err, null);
      return;
    }
    callback(null, result);
  });
};

process.on("exit", () => {
  connection.end();
});

module.exports = { connection, query };

// Insert the products into the MySQL database
// for (const product of products) {
//   const sql =
//     "INSERT INTO products (name, category, price, quantity, description, imageURL) VALUES (?, ?, ?, ?, ?, ?)";
//   const values = [
//     product.name,
//     product.category,
//     product.price,
//     product.quantity,
//     product.description,
//     product.imageURL,
//   ];

//   connection.query(sql, values, (err, result) => {
//     if (err) {
//       console.log("not insert");
//       return;
//     }
//     console.log("insert complete");
//   });
// }
