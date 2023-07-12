const jwt = require("jsonwebtoken");
const asyncHandler = require("express-async-handler");
const User = require("../models/user.models");

//protect auth

const protect = asyncHandler(async (req, res, next) => {
  try {
    const token = req.cookies.token;
    if (!token) {
      res.status(401).json({ message: "Not authorized, please login" });
    }
    // Verify Token
    const verified = jwt.verify(token, process.env.JWT_SECRET);

    User.getByUsername(verified.id, (err, result) => {
      if (err) {
        return res.status(400).json({ message: "Error found username" });
      }
      if (result.length === 0) {
        return res.status(401).json({ message: "User not found" });
      }
      req.user = result[0];
      next();
    });
  } catch (error) {
    console.log("PROTECT ERROR:", error);
  }
});

module.exports = protect;
