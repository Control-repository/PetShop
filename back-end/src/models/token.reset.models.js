const { query } = require("../database/connect.db");

const Token = {};

//get Token
Token.getToken = async (username) => {
  let questionQuery = `SELECT * from reset_password_token WHERE user_username = ?`;

  return await query(questionQuery, [username]);
};
//get Token
Token.getToken = async (token, expiresAt) => {
  let questionQuery = `SELECT * from reset_password_token WHERE token = ? AND expiresAt >=?`;

  return await query(questionQuery, [token, expiresAt]);
};

//insert
Token.insert = async (username, token) => {
  const create_at = new Date();
  const expiresAt = new Date(create_at.getTime() + 30 * (60 * 1000));
  let questionQuery = `INSERT INTO reset_password_token(user_username,token,create_at,expiresAt) VALUES(?,?,?,?)`;
  return await query(questionQuery, [username, token, create_at, expiresAt]);
};

//delete token
Token.delete = async (username) => {
  const questionQuery = `DELETE FROM reset_password_token WHERE user_username = ?`;
  return await query(questionQuery, [username]);
};

module.exports = Token;
