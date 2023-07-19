const { query } = require("../database/connect.db");

const User = {};

//get all user
User.getAll = async () => {
  let questionQuery = `SELECT * from users`;

  return await query(questionQuery, []);
};
//get all user but not get user cureent
User.getAllCurrent = async (username) => {
  let questionQuery = `SELECT * FROM users WHERE username <> ?`;

  return await query(questionQuery, [username]);
};

//get user by username
User.getByUsername = async (username) => {
  let questionQuery = "SELECT * FROM users WHERE username =?";
  return await query(questionQuery, [username]);
};

//get user by email
User.getByEmail = async (email) => {
  let questionQuery = "SELECT * FROM users WHERE email = ?";
  return await query(questionQuery, [email]);
};

User.insert = async (data) => {
  let questionQuery =
    "INSERT INTO users(username,password,fullname,email,phone) VALUES(?,?,?,?,?)";
  const values = [
    data.username,
    data.password,
    data.fullname,
    data.email,
    data.phone,
  ];
  return await query(questionQuery, values);
};

User.updatePassword = async (username, newPassword) => {
  let questionQuery = "UPDATE users SET password=? WHERE username=?";
  return await query(questionQuery, [newPassword, username]);
};

User.update = async (data) => {
  let questionQuery =
    "UPDATE users SET email=?,phone=?,fullname=? WHERE username=?";
  return await query(questionQuery, [data.email, data.phone, data.fullname]);
};

User.remove = async (username) => {
  let questionQuery = "DELETE FROM users WHERE username =?";
  return await query(questionQuery, [username]);
};
module.exports = User;
