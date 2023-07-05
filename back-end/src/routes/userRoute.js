const route = require("express")();
const userController = require("../controllers/userController.js");

//kiểm tra tồn tại của user
route.post("/signin", async (req, res) => {
  const { username, password } = req.body;

  try {
    const isVaild = await userController.checkUser(username, password);

    if (isVaild) {
      res.status(200).json({ message: "Login complete!" });
    } else {
      res.status(401).json({ message: "Username or password is incorrect" });
    }
  } catch (error) {
    res.status(500).json({ message: "Error server" });
  }
});

//thêm mới user
route.post("/insert", (req, res) => {
  userController.insertUser(req, res);
});

module.exports = route;
