const jwt = require("jsonwebtoken");
const userModel = require("../models/user.models");
const Product = require("../models/product.model");
const token = require("../models/token.reset.models");
const { query } = require("../database/connect.db");
const sendEmail = require("../untils/sendEmail");
const crypto = require("crypto");

//create token
const generateToken = (id) => {
  return jwt.sign({ id }, process.env.JWT_SECRET, { expiresIn: "1d" });
};

//thêm mới một user
const registerUser = async (req, res) => {
  const { password, fullname, email, phone, role } = req.body;

  if (!password || !email) {
    return res.status(400).json({ message: "Missing required fields" });
  }

  try {
    const isCheck = await userModel.getByEmail(email);
    if (isCheck.length > 0) {
      return res.status(400).json({ message: "Email already exists" });
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

    await userModel.insert(req.body);
    res.status(200).json({
      message: "User inserted successfully",
    });
  } catch (error) {
    console.log("REGISTER USER FAULT", error);
    if (error.code === "ER_DUP_ENTRY") {
      return res.status(409).json({ message: "Username already exits" });
    } else {
      return res.status(500).json({ message: "Failed register user" });
    }
  }
};

const loginUser = async (email, password) => {
  const questionQuery = "SELECT * FROM users WHERE email = ? AND password = ?";
  const values = [email, password];

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
//update all information for user
const updateAll = async (req, res) => {
  const { email } = req.params;
  try {
    const values = { ...req.body, email };
    await userModel.updateAll(values);
    return res.status(200).json({ message: "User change successfully!" });
  } catch (error) {
    console.log("ERROR UPDATE ALL USER: ", error);
    return res
      .status(500)
      .json({ message: "Error to change user information!" });
  }
};
//update User
const updateInfor = async (req, res) => {
  const { email } = req.user;

  const values = { ...req.body, email };
  try {
    const result = await userModel.getByEmail(email);
    if (result.length !== 0) {
      const user = await userModel.update(values);
      return res
        .status(200)
        .json({ message: "User change successfully!", user: user[0] });
    } else {
      return res.status(400).json({ message: "Not to found User!" });
    }
  } catch (error) {
    console.log("ERROR UPDATE USER: ", error);
    return res
      .status(500)
      .json({ message: "Error to change user information!" });
  }
};

//update password
const updatePassword = async (req, res) => {
  const { password } = req.body;
  const { email } = req.user;
  try {
    await userModel.updatePassword(email, password);
    return res.status(200).json({ message: "Change password successfully!" });
  } catch (error) {
    return res.status(500).json({ message: "Error to change Password " });
  }
};
//delete user
const deleteUser = async (req, res) => {
  const { email } = req.params;
  try {
    const result = userModel.getByEmail(email);
    if (!result) {
      return res.status(400).json({ message: "Not found username" });
    }
    await userModel.remove(email);
    return res.status(200).json({ message: "Remove succesfully!" });
  } catch (error) {
    return res.status(500).json({ message: "Error to delete user" });
  }
};

//forgot password
const forgotPassword = async (req, res) => {
  const { email } = req.body;
  try {
    const result = await userModel.getByEmail(email);
    if (result.length === 0) {
      return res.status(500).json({ message: "Not found email" });
    }
    await token.delete(email);

    // Create Reste Token
    let resetToken = crypto.randomBytes(3).toString("hex");
    console.log(resetToken);

    // Hash token before saving to DB
    const hashedToken = crypto
      .createHash("sha256")
      .update(resetToken)
      .digest("hex");

    // Save Token to DB
    await token.insert(email, hashedToken);

    const subject = "Password Reset Request";
    const send_to = email;
    const sent_from = process.env.EMAIL_USERNAME;

    const message = `<p>Hello ${email},</p>
  <p>Your token to reset password: ${resetToken}.</p>
  `;
    // sendEmail(subject, message, send_to, sent_from,send_to);
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
    const date = new Date();
    // Find token in DB
    console.log(date);
    const tokens = await token.getToken(hashedToken, date);
    if (!tokens) {
      return res.status(404).json({ message: "Invalid or Expired Token" });
    }
    // Update password
    const isUpdate = await userModel.updatePassword(
      tokens[0].user_email,
      password
    );
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

//get all user without user current
const getAllCurrent = async (req, res) => {
  try {
    const user = req.user;
    const results = await userModel.getAllCurrent(user.email);
    if (results.length === 0) {
      return res.status(409).json({ message: "No user without current user" });
    }
    return res.status(200).json({ users: results });
  } catch (error) {
    console.log("ERROR GET ALL", error);
    return res.status(500).json({ message: "Error when get all user" });
  }
};
//get all user
const getAll = async (req, res) => {
  try {
    const results = await userModel.getAll();

    return res.status(200).json({ users: results });
  } catch (error) {
    return res.status(500).json({ message: "Error when get all user" });
  }
};
module.exports = {
  registerUser,
  loginUser,
  resetPassword,
  generateToken,
  forgotPassword,
  updateInfor,
  updatePassword,
  updateAll,
  deleteUser,
  getAllCurrent,
  getAll,
  logout,
};
