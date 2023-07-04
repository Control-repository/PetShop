const route = require("express")();
const bodyParser = require("body-parser");

route.use(bodyParser.urlencoded({ extended: true }));
route.use(bodyParser.json());

module.exports = route;
