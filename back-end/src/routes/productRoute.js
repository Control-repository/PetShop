const route = require("express")();

const productController = require("../controllers/productController");
const authMiddleware = require("../middleware/authMiddleware");

route.get("/all",authMiddleware, (req, res) => {
  productController.getAllProducts(req, res);
});
route.post("/insert", authMiddleware, (req, res) => {
  productController.insertProduct(req, res);
});
route.post("/insert/data", (req, res) => {
  productController.insertProducts();
});
route.get("/get/:id", (req, res) => {
  productController.getProduct(req, res);
});
route.delete("/delete/all", (req, res) => {
  productController.deleteAllProduct(req, res);
});
module.exports = route;
