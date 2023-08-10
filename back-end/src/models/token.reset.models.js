const { query } = require("../database/connect.db");

const Token = {};

//get Token
Token.getToken = async (email) => {
  let questionQuery = `SELECT * from reset_password_token WHERE user_email = ?`;

  return await query(questionQuery, [email]);
};
//get Token
Token.getToken = async (token, expiresAt) => {
  let questionQuery = `SELECT * from reset_password_token WHERE token = ? AND expiresAt >=?`;
  console.log("Reset ", expiresAt);
  return await query(questionQuery, [token, expiresAt]);
};

//insert
Token.insert = async (email, token) => {
  const create_at = new Date();
  const expiresAt = new Date(create_at.getTime() + 30 * (60 * 1000));
  console.log("Insert ", expiresAt);
  let questionQuery = `INSERT INTO reset_password_token(user_email,token,create_at,expiresAt) VALUES(?,?,?,?)`;
  return await query(questionQuery, [email, token, create_at, expiresAt]);
};

//delete token
Token.delete = async (email) => {
  const questionQuery = `DELETE FROM reset_password_token WHERE user_email = ?`;
  return await query(questionQuery, [email]);
};

module.exports = Token;
