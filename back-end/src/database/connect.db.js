const mysql = require("mysql");
const createTable = require("../untils/createTable.js");

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
      //create new table users
      connection.query(createTable.userTable, (err) => {
        if (err) {
          console.log("Error creating table ", err);
          return;
        } else {
          console.log("Table created Users");
          //Create new table products
          connection.query(createTable.productTable, (err) => {
            if (err) {
              console.log("Error creating table ", err);
              return;
            } else {
              console.log("Table created Products");
            }
          });

          connection.query(createTable.reset_pass_table, (err) => {
            if (err) {
              console.log("Error creating table ", err);
              return;
            } else {
              console.log("Table created Token");
            }
          });
        }
      });
    });
  });
});

const query = (sql, value) => {
  return new Promise((resolve, reject) => {
    connection.query(sql, value, (err, results) => {
      if (err) {
        reject(err);
        return;
      }
      resolve(results);
    });
  });
};

process.on("exit", () => {
  connection.end();
});

module.exports = { query };
