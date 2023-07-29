const route = require("express")();

const product = require("../controllers/productController");

const protect = require("../middleware/authMiddleware");

const multer = require('multer');
const upload = multer({ dest: 'uploads/' });

route.get("/all",protect, product.getAllProducts);
route.post("/insert", protect,upload.single('image'), product.insertProduct);
route.post("/insert/data", protect, product.insertProducts);
route.get("/get/:id", protect, product.getProduct);
route.put("/update/:id", protect,upload.single('image'), product.updateProduct);
route.delete("/delete/all", protect, product.deleteAllProduct);
route.delete("/delete/:id", protect, product.deleteProduct);


module.exports = route;
