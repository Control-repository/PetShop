const authMiddleware = (req, res, next) => {
  if (!req.user) {
    return res.status(401).send("Unauthorized");
  }

  next();
};

module.exports = authMiddleware;
