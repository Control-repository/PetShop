const jwt = require("jsonwebtoken");
const user = require("../models/user.models");
const token = require("../models/token.reset.models");
const { query } = require("../database/connect.db");

const crypto = require("crypto");

//create token
const generateToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, { expiresIn: "1d" });
};

//thêm mới một user
const registerUser = (req, res) => {
  const { username, password, fullname, email, phone } = req.body;

  if (!username || !password) {
    return res.status(400).json({ message: "Missing required fields" });
  }

  user.getByUsername(username, (err, results) => {
    if (err) {
      return res.status(500).json({ message: "Error checking username" });
    }

    if (results.length > 0) {
      return res.status(409).json({ message: "username already exists" });
    }

    user.getByEmail(email, (err, result) => {
      if (err) {
        return res.status(500).json({ message: "Error register user" });
      }
      if (result.length !== 0) {
        return res.status(409).json({ message: "Email already exists" });
      }

      const token = generateToken(username);
      // Send HTTP-only cookie
      res.cookie("token", token, {
        path: "/",
        httpOnly: true,
        expires: new Date(Date.now() + 1000 * 86400), // 1 day
        sameSite: "none",
        // secure: true,
      });

      user.insert(req.body, (err, result) => {
        if (err) {
          return res.status(500).json({ message: "Failed register user" });
        }
        res.status(200).json({
          message: "User inserted successfully",
        });
      });
    });
  });
};

//login
const loginUser = (username, password) => {
  let questionQuery = `SELECT * FROM users WHERE username = ? AND password= ?`;

  const values = [username, password];

  return new Promise((resolve, reject) => {
    query(questionQuery, values, (err, result) => {
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

const logout = (req, res) => {
  // Send HTTP-only cookie
  res.cookie("token", "", {
    path: "/",
    httpOnly: true,
    expires: new Date(0),
    sameSite: "none",
    // secure: true,
  });
  return res.status(200).json({ message: "Successfully Logged Out!" });
};

//update User
const updateUser = (req, res) => {
  const { fullname, email, phone } = req.body;

  if (!fullname || !email || !phone) {
    return res.status(400).json({ message: "Missing required fields" });
  }
  user.update(req.body, (err, result) => {
    if (err) {
      return res
        .status(500)
        .json({ message: "Error to change user information!" });
    }

    return res.status(200).json({ message: "User change successfully!" });
  });
};
//forgot password
const forgotPassword = async (req, res) => {
  const { email } = req.body;

  user.getByEmail(email, (err, result) => {
    if (result.length === 0) {
      return res.status(500).json({ message: "Not found email" });
    }
    token.getToken(result[0].username, (err, result) => {
      token.delete(result[0].username);
    });

    // Create Reste Token
    let resetToken =
      crypto.randomBytes(32).toString("hex") + result[0].username;
    console.log(resetToken);

    // Hash token before saving to DB
    const hashedToken = crypto
      .createHash("sha256")
      .update(resetToken)
      .digest("hex");

    // Save Token to DB
    token.insert(result[0].username, hashedToken, (err, result) => {
      if (err) {
        return res.status(500).json({ message: "Error to create Token" });
      }

      const subject = "Password Reset Request";
      const send_to = user.email;
      const sent_from = process.env.EMAIL_USER;

      sendEmail(subject, message, send_to, sent_from);
      res.status(200).json({ success: true, message: "Reset Email Sent" });
    });
  });
};

// Reset Password
const resetPassword = (req, res) => {
  const { password } = req.body;
  const { resetToken } = req.params;

  // Hash token, then compare to Token in DB
  const hashedToken = crypto
    .createHash("sha256")
    .update(resetToken)
    .digest("hex");

  // fIND tOKEN in DB
  token.getToken(hashedToken, Date.now(), (err, result) => {
    if (err) {
      return res.status(500).json({ message: `Error find token` });
    }
    if (result.length === 0) {
      return res.status(404).json({ message: "Invalid or Expired Token" });
    }
    user.updatePassword(result[0].user_username, password, (err, result) => {
      if (err) {
        return res.status(400).json({ message: "Error to updatePassword" });
      }
      return res.status(200).json({
        message: "Password Reset Successful, Please Login",
      });
    });
  });
};

module.exports = {
  registerUser,
  loginUser,
  resetPassword,
  generateToken,
};
