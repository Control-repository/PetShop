const myConnection = require("../database/connect.db");
const dotenv = require("dotenv");
dotenv.config();

//kiểm tra tồn tại của user
const checkAuth = (username, password) => {
  let query = `SELECT * FROM users WHERE username = ? AND password= ?`;

  const values = [username, password];

  return new Promise((resolve, reject) => {
    myConnection.query(query, values, (err, result) => {
      if (err) {
        reject(err);
        return;
      }

      if (result.length > 0) {
        console.log(result);
        resolve(result[0]);
      } else {
        resolve(null);
      }
    });
  });
};

//thêm mới một user
const insertAuth = (req, res) => {
  const { username, password, fullname, email, phone } = req.body;

  if (!username || !password) {
    res.status(400).send("Missing required fields");
    return;
  }

  let query = `INSERT INTO users(username,password,email,fullname,phone)
    VALUE(?,?,?,?,?)`;
  const values = [username, password, email, fullname, phone];

  myConnection.query(query, values, (err, result) => {
    if (err) {
      console.log("Failed to insert user", err);
      return res.status(500).send("Failed to insert prodcut");
    }
    res.status(200).json({
      message: "User inserted successfully",
    });
  });
};

//reset password
const resetPassword = (req, res) => {
  const { token, newPassword } = req.body;

  //Check
  if (!token || !newPassword) {
    res.status(401).send("Token or new Password is missing");
    return;
  }
  if (token !== process.env.RESET_PASSWORD_TOKEN) {
    res.status(401).send("Invalid token");
    return;
  }

  //Check token
  const selectQuery = `SELECT * from reset_password_token WHERE token = ?`;
  myConnection.query(selectQuery, [token], (err, results) => {
    if (err) {
      console.error("Error retrieving reset token:", err);
      res.status(500).send("Error retrieving reset token");
      return;
    }

    //If true
    if (results.length > 0) {
      res.status(404).send("Invalid reset token");
      return;
    }

    const resetToken = results[0];

    const updateQuery = `UPDATE users SET password=? WHERE username =?`;
    myConnection.query(
      updateQuery,
      [newPassword, resetToken.use_username],
      (err, result) => {
        if (err) {
          res.status(500).send("Error resetting password");
          return;
        }

        const query = `DELETE FROM reset_password_token WHERE token =?`;
        myConnection.query(query, [token], (err, result) => {
          if (err) {
            console.error("Error deleting reset token:", err);
            res.status(500).send("Error deleting reset token");
            return;
          }

          res.json({ message: "Password reset successfully" });
        });
      }
    );
  });
};

module.exports = { checkAuth, insertAuth, resetPassword };
