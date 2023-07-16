const jwt = require("jsonwebtoken");
const User = require("../models/user.models");

//protect auth
const protect = async (req, res, next) => {
  try {
    const token = req.cookies.token;
    if (!token) {
      res.status(401).json({ message: "Not authorized, please login" });
    }
    // Verify Token
    const verified = jwt.verify(token, process.env.JWT_SECRET);

    const user = await User.getByUsername(verified.id);
    if (!user) {
      return res.status(401).json({ message: "User not found" });
    }
    req.user = user[0];
    next();
  } catch (error) {
    console.log("PROTECT ERROR:", error);
    return res.status(500).json({ message: "Interval Server error" });
  }
};

module.exports = protect;
