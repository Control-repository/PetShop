const route = require("express")();
const auth = require("../controllers/authController.js");
const protect = require("../middleware/authMiddleware");
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

//get all user
route.get("/all/full", protect, auth.getAll);
//get all user without user current
route.get("/all/current", protect, auth.getAllCurrent);
//thêm mới user
route.post("/register", auth.registerUser);
//reset password
route.put("/reset-password/:resetToken", auth.resetPassword);
//forgot-password
route.post("/forgot-password", auth.forgotPassword);
//update information user
route.put("/update/infor", protect, auth.updateInfor);
//update password for current user
route.put("/update/password", protect, auth.updatePassword);
//update all innformation
route.put("/udpate/all/:username", protect, auth.updateAll);
//delete user
route.delete("/delete/user/:username", protect, auth.deleteUser);
module.exports = route;
