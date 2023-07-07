const app = require("express")();
const bodyParser = require("body-parser");

const productRoute = require("./src/routes/productRoute");
const customerRoute = require("./src/routes/customerRoute");
const auth = require("./src/routes/authRoute");

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use("/products", productRoute);
app.use("/customers", customerRoute);
app.use("/auth", auth);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Connecting to PORT ${PORT}`);
});
