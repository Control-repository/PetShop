const route = require("express")();
const auth = require("../controllers/authController.js");

//kiểm tra tồn tại của user
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
      return res.status(200).json({ message: "Login complete!", user: user });
    } else {
      return res
        .status(401)
        .json({ message: "Username or password is incorrect" });
    }
  } catch (error) {
    res.status(500).json({ message: "Error server" });
  }
});

//thêm mới user
route.post("/register", auth.registerUser);
//reset password
route.put("reset-password", auth.resetPassword);
module.exports = route;
