const route = require("express")();

const productController = require("../controllers/productController");

route.get("/all", (req, res) => {
  productController.getAllProducts(req, res);
});
route.post("/insert", (req, res) => {
  productController.insertProduct(req, res);
});
route.post("/insert/data", (req, res) => {
  productController.insertProducts(req, res);
});
route.get("/get/:id", (req, res) => {
  productController.getProduct(req, res);
});
module.exports = route;
