const route = require("express")();
const auth = require("../controllers/authController.js");

//kiểm tra tồn tại của user
route.post("/signin", async (req, res) => {
  const { username, password } = req.body;
  console.log(username + " " + password);
  try {
    const user = await auth.checkAuth(username, password);

    if (user) {
      res.status(200).json({ message: "Login complete!", user: user });
    } else {
      res.status(401).send("Username or password is incorrect");
    }
  } catch (error) {
    res.status(500).send("Error server");
  }
});

//thêm mới user
route.post("/insert", (req, res) => {
  auth.insertAuth(req, res);
});
//reset password

route.post("reset-password", (req, res) => {
  auth.resetPassword(req, res);
});
module.exports = route;
