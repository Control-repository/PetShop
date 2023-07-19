const cloudinary = require("cloudinary").v2;
const multer = require("multer");

const key = API_SECRET_CLOUDINARY;

cloudinary.config({
  cloud_name: "dg1nlrihe",
  api_key: "618714617838561",
  api_secret: key,
});

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null,"uploads");
  },
  filename: (req,file,cb)=>{
    cb(ull,new Date().toISOString().replace(/:/g,"-") + "-" + file.originalname)
  }
});
