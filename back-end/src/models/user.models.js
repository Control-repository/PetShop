const { query } = require("../database/connect.db");

const User = {};

//get all user
User.getAll = async () => {
  let questionQuery = `SELECT * from users`;

  return await query(questionQuery, []);
};
//get all user but not get user cureent
User.getAllCurrent = async (username) => {
  let questionQuery = `SELECT * FROM users WHERE email <> ?`;

  return await query(questionQuery, [username]);
};

//get user by email
User.getByEmail = async (email) => {
  let questionQuery = "SELECT * FROM users WHERE email = ?";
  return await query(questionQuery, [email]);
};

User.insert = async (data) => {
  let questionQuery =
    "INSERT INTO users(email,password,fullname,phone,role) VALUES(?,?,?,?,?)";
  const values = [
    data.email,
    data.password,
    data.fullname,
    data.phone,
    data.role,
  ];
  return await query(questionQuery, values);
};

User.updatePassword = async (email, newPassword) => {
  let questionQuery = "UPDATE users SET password=? WHERE email=?";
  return await query(questionQuery, [newPassword, email]);
};

User.update = async (data) => {
  let questionQuery = "UPDATE users SET phone=?,fullname=? WHERE email=?";
  return await query(questionQuery, [data.phone, data.fullname, data.email]);
};
User.updateAll = async (data) => {
  let questionQuery =
    "UPDATE users SET phone=?,fullname=?,password=? WHERE email=?";
  return await query(questionQuery, [
    data.phone,
    data.fullname,
    data.password,
    data.email,
  ]);
};

User.remove = async (email) => {
  let questionQuery = "DELETE FROM users WHERE email =?";
  return await query(questionQuery, [email]);
};
module.exports = User;
