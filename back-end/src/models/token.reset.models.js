const { query } = require("../database/connect.db");

const Token = (token) => {
  this.id = token.id;
  this.user_username = token.user_username;
  this.token = token.token;
  this.create_at = token.create_at;
};

//get Token
Token.getToken = (username, callback) => {
  let questionQuery = `SELECT * from reset_password_token WHERE user_username = ?`;

  query(questionQuery, [username], callback);
};
//get Token
Token.getToken = (token, expiresAt, callback) => {
  let questionQuery = `SELECT * from reset_password_token WHERE token = ? AND expriseAt =?`;

  query(questionQuery, [token, expiresAt], callback);
};

//insert
Token.insert = (username, token, callback) => {
  let questionQuery = `INSERT INTO reset_password_token(user_username,token,create_at,expiresAt) VALUES(?,?,?,?)`;
  query(
    questionQuery,
    [username, token, Date.now(), Date.now() + 30 * (60 * 1000)],
    callback
  );
};

//delete token
Token.delete = (username, callback) => {
  const questionQuery = `DELETE FROM reset_password_token WHERE user_username = ?`;
  query(questionQuery, [username], callback);
};

module.exports = Token;
