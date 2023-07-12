const { query } = require("../database/connect.db");

const User = (user) => {
  this.username = user.username;
  this.password = user.password;
  this.fullname = user.fullname;
  this.email = user.email;
  this.phone = user.phone;
};

User.getAll = (callback) => {
  let questionQuery = `SELECT * from users`;

  query(questionQuery, [], callback);
};
User.getByUsername = (username, callback) => {
  let questionQuery = "SELECT * FROM users WHERE username =?";
  query(questionQuery, [username], callback);
};

User.getByEmail = (email, callback) => {
  let questionQuery = "SELECT * FROM users WHERE email =?";
  query(questionQuery, [email], callback);
};

User.insert = (data, callback) => {
  let questionQuery =
    "INSERT INTO users(username,password,fullname,email,phone) VALUES(?,?,?,?,?)";
  const values = [
    data.username,
    data.password,
    data.fullname,
    data.email,
    data.phone,
  ];
  query(questionQuery, values, callback);
};

User.updatePassword = (username, newPassword, callback) => {
  let questionQuery = "UPDATE users SET password=? WHERE username=?";
  query(questionQuery, [data.password, data.username], callback);
};

User.update = (data, callback) => {
  let questionQuery =
    "UPDATE users SET email=?,phone=?,fullname=? WHERE username=?";
  query(questionQuery, [data.email, data.phone, data.fullname], callback);
};

User.remove = (id, callback) => {
  let questionQuery = "DELETE FROM users WHERE id =?";
  query(questionQuery, [id], callback);
};
module.exports = User;
