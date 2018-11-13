const sha = require('sha256');
const db = require('./db.js');
var session = require('express-session');

var MySQLStore = require('express-mysql-session')(session);
module.exports = function (app) {

  const options = {
    host: 'localhost',
    port: 3306,
    user: 'root',
    password: '880918',
    database: 'toeic_solver'
  };
  var sessionStore = new MySQLStore(options);
  app.use(session({
    key: 'ffasoid24',
    secret: 'asdfawefas02',
    store: sessionStore,
    resave: false,
    saveUninitialized: true
  }));
  const passport = require('passport'),
    LocalStrategy = require('passport-local').Strategy
  
  app.use(passport.initialize());
  app.use(passport.session());

  passport.serializeUser(function (user, done) {
    done(null, user.id);
  });
  passport.deserializeUser(function (id, done) { //done에서 2번째 인자로 request.user라는 인자 전달해줌 
        db.query('SELECT * FROM auth_local WHERE id = ?', [id], function (err, user) {
       if (user[0] === undefined) {
        db.query('SELECT * FROM auth_kakao WHERE id = ?', [id], function (err, user) {
          return done(err, user[0]) //카카오 가져오기
        })
       }else{
       return done(err, user[0]); //로컬 가져오기
       }
     });
  });

  passport.use(new LocalStrategy({
      usernameField: 'id', //폼 데이터 형식 바꿔줌
      //passwordField: 'passwd'
      session: true, //세션 저장 유무
      passReqToCallback: false
    },
    function (id, password, done) {
      console.log('Nonexistent id.');
      db.query('SELECT * FROM auth_local WHERE id = ?', [id], function (err, user) {

        if (user[0] === undefined) {
          console.log('Nonexistent id.');
          return done(null, false, {
            message: 'Nonexistent id.'
          });
        }
        if (user[0].password !== sha(password + user[0].salt)) {
          console.log('Incorrect password.');
          return done(null, false, {
            message: 'Incorrect password.'
          });
        }
        console.log('success');
        return done(null, user[0]);
      })
    }));
  return passport;
}