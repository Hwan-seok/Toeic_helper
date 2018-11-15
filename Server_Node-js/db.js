/*jshint esversion: 6 */
const mysql = require('mysql');
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '880918',
  database: 'toeic_solver'
});
module.exports = db;
