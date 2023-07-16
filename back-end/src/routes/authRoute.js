const route = require("express")();
const auth = require("../controllers/authController.js");

route.post("/signin", async (req, res) => {
  const { username, password } = req.body;
  try {
    const user = await auth.loginUser(username, password);

    if (user) {
      const token = auth.generateToken(username);

      res.cookie("token", token, {
        path: `/`,
        httpOnly: true,
        expires: new Date(Date.now() + 1000 * 86400), // 1 day
        sameSite: "none",
        // secure: true,
      });
      req.user = user;
      return res
        .status(200)
        .json({ message: "Login complete!", token: token, user: user });
    } else {
      return res
        .status(401)
        .json({ message: "Username or password is incorrect" });
    }
  } catch (error) {
    console.log("LOGIN ERROR", error);
    res.status(500).json({ message: "Error server" });
  }
});

//thêm mới user
route.post("/register", auth.registerUser);
//reset password
route.put("/reset-password/:resetToken", auth.resetPassword);
//forgot-password
route.post("/forgot-password", auth.forgotPassword);
module.exports = route;
