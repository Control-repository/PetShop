const myConnection = require("../database/connect.db");

//kiểm tra tồn tại của user
const checkUser = (username, password) => {
  let query = `SELECT * FROM users WHERE username = ? AND password= ?`;

  const values = [username, password];

  return new Promise((resolve, reject) => {
    myConnection.query(query, values, (err, result) => {
      if (err) {
        reject(err);
        return;
      }

      if (result.length > 0) {
        resolve(true);
      } else {
        resolve(false);
      }
    });
  });
};

//thêm mới một user
const insertUser = (req, res) => {
  const { username, password, fullname, email, phone } = req.body;

  if (!username || !password) {
    res.status(400).json({ error: "Missing required fields" });
    return;
  }

  let query = `INSERT INTO users(username,password,email,fullname,phone)
    VALUE(?,?,?,?,?)`;
  const values = [username, password, email, fullname, phone];

  myConnection.query(query, values, (err, result) => {
    if (err) {
      console.log("Failed to insert user", err);
      return res.status(500).json({ message: "Failed to insert prodcut" });
    }
    res.status(200).json({
      message: "User inserted successfully",
      user: result,
    });
  });
};

module.exports = { checkUser, insertUser };
