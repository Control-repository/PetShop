const route = require("express")();

const product = require("../controllers/productController");

const protect = require("../middleware/authMiddleware");

route.get("/all",protect, product.getAllProducts);
route.post("/insert", protect, product.insertProduct);
route.post("/insert/data", protect, product.insertProducts);
route.get("/get/:id", protect, product.getProduct);
route.put("/update/:id", protect, product.updateProduct);
route.delete("/delete/all", protect, product.deleteAllProduct);
route.delete("/delete/:id", protect, product.deleteProduct);


module.exports = route;
