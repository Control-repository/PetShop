const product = require("../models/product.model");
const cloudinary = require("cloudinary").v2;

//cấu hình cloudinary
cloudinary.config({
  cloud_name: "dg1nlrihe",
  api_key: "618714617838561",
  api_secret: process.env.API_SECRET_CLOUDINARY,
});

//get All products
const getAllProducts = async (req, res) => {
  let search = req.query.search;
  try {
    const results = await product.getAll(search);
    if (!results) {
      return res.status(400).json({ message: "Failed to get product" });
    }
    return res.status(200).json({ products: results });
  } catch (error) {
    console.log("PRODUCT ERROR: ", error);
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

//insert product
const insertProduct = async (req, res) => {
  const values = {
    ...req.body,
  };
  //kiểm tra tải ảnh lên server
  try {
    if (req.file) {
      // Gửi ảnh lên Cloudinary
      const cloudinary_image = await cloudinary.uploader.upload(req.file.path, {
        folder: "PorterAnime",
      });
      values.imageURL = cloudinary_image.secure_url;
      const result = await product.insert(values);

      if (!result) {
        //khi có lỗi xảy ra sẽ xóa bỏ hình ảnh trong cloudinary
        await cloudinary.uploader.destroy(cloudinary_image.public_id);
        console.log("Lỗi ");
        return res.status(500).json({ message: "Internal Server Error" });
      }
      return res.status(200).json({ message: "Product inserted successfully" });
    } else {
      //Trường hợp không có image
      const result = await product.insert(values);

      if (!result) {
        console.log("Lỗi");
        return res.status(500).json({ message: "Internal Server Error" });
      }
      return res.status(200).json({ message: "Product inserted successfully" });
    }
  } catch (error) {
    //khi có lỗi xảy ra sẽ xóa bỏ hình ảnh trong cloudinary
    console.error("Lỗi khi tải lên ảnh lên Cloudinary:", error);
  }
};

//update information product
const updateProduct = async (req, res) => {
  const { name, category, price, quantity, description, imageURL } = req.body;
  const id = req.params.id;

  const values = { id, name, category, price, quantity, description, imageURL };
  try {
    //Khi lựa chọn ảnh trong máy
    if (req.file) {
      // Gửi ảnh lên Cloudinary
      const cloudinary_image = await cloudinary.uploader.upload(req.file.path, {
        folder: "PorterAnime",
      });
      values.imageURL = cloudinary_image.secure_url;
      const result = await product.update(values);
      if (!result) {
        //khi có lỗi xảy ra sẽ xóa bỏ hình ảnh trong cloudinary
        await cloudinary.uploader.destroy(cloudinary_image.public_id);
        console.log("Lỗi ");
        return res.status(500).json({ message: "Failed to updated product" });
      }
      return res.status(200).json({ message: "Product updated successfully" });
    } else {
      //Khi không có ảnh
      const result = await product.update(values);
      if (!result) {
        console.log("lỗi");
        return res.status(500).json({ message: "Internal Server Error" });
      }
      return res.status(200).json({ message: "Product updated successfully" });
    }
  } catch (error) {
    console.log("ERROR UPDATE PRODUCT", error);
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

//get one product
const getProduct = async (req, res) => {
  const id = req.params.id;
  try {
    const result = product.getById(id);
    if (!result) {
      res.status(404).json({ message: "Product not found" });
    }
    return res.json({ product: result });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
};
// insert databse example
const insertProducts = async (req, res) => {
  try {
    const list = await product.insertExample();
    return res.json({ message: "insert complete!", products: list });
  } catch (error) {
    console.log(error);
    return res.json({ message: "Something wrong" });
  }
};
//delete item by id
const deleteProduct = async (req, res) => {
  const { id } = req.params;
  try {
    await product.remove(id);
    return res.status(200).json({ message: "Delete successfully!" });
  } catch (error) {
    console.log("DELETE ERROR", error);
    return res.status(500).json({ message: "Internal Server Error" });
  }
};
//delete all product
const deleteAllProduct = async (req, res) => {
  try {
    await product.removeAll();
    return res
      .status(200)
      .json({ message: "All products deleted successfully" });
  } catch (error) {
    return res.status(500).json({ message: "Internal Server Error" });
  }
};

module.exports = {
  getAllProducts,
  insertProduct,
  insertProducts,
  updateProduct,
  getProduct,
  deleteAllProduct,
  deleteProduct,
};
