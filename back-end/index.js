const express = require("express");
const bodyParser = require("body-parser");
const dotenv = require("dotenv").config();
const cookieParser = require("cookie-parser");


const product = require("./src/routes/productRoute");
const customer = require("./src/routes/customerRoute");
const auth = require("./src/routes/authRoute");

app = express();

app.use(express.json());
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use("/products", product);
app.use("/customers", customer);
app.use("/auth", auth);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Connecting to PORT ${PORT}`);
});
