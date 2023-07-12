const route = require("express")();

const productController = require("../controllers/productController");
const protect = require("../middleware/authMiddleware");

route.get("/all", protect, productController.getAllProducts);
route.post("/insert", protect, productController.insertProduct);
route.post("/insert/data", protect, productController.insertProducts);
route.get("/get/:id", protect, productController.getProduct);
route.put("/update/:id", protect, productController.updateProduct);
route.delete("/delete/all", protect, productController.deleteAllProduct);
route.delete("/delete/:id", protect, productController.deleteProduct);
module.exports = route;
