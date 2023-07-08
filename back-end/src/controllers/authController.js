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
    return res.status(400).json({message:"Missing required fields"});
  }

  //Check user exists
  const checkQuery = `SELECT * from users WHERE username = ?`;
  myConnection.query(checkQuery, [username], (err, results) => {
    if (err) {
      return res.status(500).json({ message: "Error checking username" });
    }

    if (results.length > 0) {
      return res.status(409).json({ message: "username already exists" });
    }

    //insert user
    let query = `INSERT INTO users(username,password,email,fullname,phone)
    VALUE(?,?,?,?,?)`;
    const values = [username, password, email, fullname, phone];

    myConnection.query(query, values, (err, result) => {
      if (err) {
        return res.status(500).json({message:"Failed register user"});
      }
      res.status(200).json({
        message: "User inserted successfully",
      });
    });
  });
};

//reset password
const resetPassword = (req, res) => {
  const { token, newPassword } = req.body;

  //Check
  if (!token || !newPassword) {
    return res.status(401).json({message:"Token or new Password is missing"});
  }
  if (token !== process.env.RESET_PASSWORD_TOKEN) {
    return res.status(401).json({message:"Invalid token"});
  }

  //Check token
  const selectQuery = `SELECT * from reset_password_token WHERE token = ?`;
  myConnection.query(selectQuery, [token], (err, results) => {
    if (err) {
      console.error("Error retrieving reset token:", err);
      return res.status(500).json({message:"Error retrieving reset token"});
    }

    //If true
    if (results.length > 0) {
      return res.status(404).json({message:"Invalid reset token"});
    }

    const resetToken = results[0];

    const updateQuery = `UPDATE users SET password=? WHERE username =?`;
    myConnection.query(
      updateQuery,
      [newPassword, resetToken.use_username],
      (err, result) => {
        if (err) {
          return res.status(500).json({ message: "Error resetting password"});
        }

        const query = `DELETE FROM reset_password_token WHERE token =?`;
        myConnection.query(query, [token], (err, result) => {
          if (err) {
            console.error("Error deleting reset token:", err);
            return res.status(500).json({ message: "Error deleting reset token"});
          }

          return res.json({ message: "Password reset successfully" });
        });
      }
    );
  });
};

module.exports = { checkAuth, insertAuth, resetPassword };
