const app = require("express")();

const productRoute = require("./src/routes/productRoute");
const customerRoute = require("./src/routes/customerRoute");
const userRoute = require("./src/routes/userRoute");

app.use("/product", productRoute);
app.use("/customer", customerRoute);
app.use("/user", userRoute);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Connecting to PORT ${PORT}`);
});
