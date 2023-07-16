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

const loginUser = async (username, password) => {
  const questionQuery =
    "SELECT * FROM users WHERE username = ? AND password = ?";
  const values = [username, password];

  try {
    const result = await query(questionQuery, values);
    if (result.length > 0) {
      return result[0];
    } else {
      return null;
    }
  } catch (err) {
    throw err;
  }
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
  try {
    const result = await user.getByEmail(email);
    if (result.length === 0) {
      return res.status(500).json({ message: "Not found email" });
    }
    const username = result[0].username;
    await token.delete(username);

    // Create Reste Token
    let resetToken = crypto.randomBytes(3).toString("hex");
    console.log(resetToken);

    // Hash token before saving to DB
    const hashedToken = crypto
      .createHash("sha256")
      .update(resetToken)
      .digest("hex");

    // Save Token to DB
    await token.insert(result[0].username, hashedToken);

    const subject = "Password Reset Request";
    const send_to = user.email;
    const sent_from = process.env.EMAIL_USER;

    const message = `
  Your token to reset password: ${resetToken}
  `;
    // sendEmail(subject, message, send_to, sent_from);
    res.status(200).json({
      success: true,
      message: "Reset Email Sent",
      token: resetToken,
    });
  } catch (error) {
    console.log("error", error);
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

// Reset Password
const resetPassword = async (req, res) => {
  const { password } = req.body;
  const { resetToken } = req.params;

  try {
    // Hash token, then compare to Token in DB
    const hashedToken = crypto
      .createHash("sha256")
      .update(resetToken)
      .digest("hex");

    // Find token in DB
    const tokens = await token.getToken(hashedToken, new Date());
    if (!tokens) {
      return res.status(404).json({ message: "Invalid or Expired Token" });
    }
    const { user_username } = tokens[0];
    // Update password
    const isUpdate = await user.updatePassword(user_username, password);
    if (isUpdate.length === 0) {
      return res.status(400).json({ message: "Wrong to update password" });
    }
    return res
      .status(200)
      .json({ message: "Password Reset Successfully, Please Login" });
  } catch (error) {
    console.log("TOKEN ERROR", error);
    return res.status(500).json({ message: "Error resetting password" });
  }
};

module.exports = {
  registerUser,
  loginUser,
  resetPassword,
  generateToken,
  forgotPassword,
};
