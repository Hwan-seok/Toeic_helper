const sha = require('sha256');
module.exports = function (app) {
    const express = require('express');
    var bodyParser = require('body-parser');
    app.use(bodyParser.urlencoded({
        extended: false
    }));
    app.use(bodyParser.json());

    const db = require('./db.js');
    var router = express.Router();
    const passport = require('./passport.js')(app); //passport 사용

    router.post('/login', passport.authenticate('local'), function (request, response) {
        if (!request.user) {
            return response.status(400).json({
                SERVER_RESPONSE: 0,
                SERVER_MESSAGE: "Wrong Credentials"
            });
        } else request.logIn(request.user, function (err) {
            console.log("logged in!");
            return response.json({
                SERVER_RESPONSE: 1,
                SERVER_MESSAGE: "Logged in!"
            });
        });
    });
    router.post('/register', function (request, response) { //name= {id , password} 으로 받음 
        const post = request.body;
	console.log(post);
        console.log(post.id);
	console.log(post.password);
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*()";
        var string_length = 15;
        var salt = '';
        db.query('SELECT id FROM auth_local WHERE id=?', post.id, function (err, result) {
            if (result[0]) {
                return response.status(400).json({
                    SERVER_RESPONSE: 0,
                    SERVER_MESSAGE: "Existed ID"
                }) // 이미 존재하는 아이디이면 다시 팅김
            } else {
                for (var i = 0; i < string_length; i++) {
                    var rnum = Math.floor(Math.random() * chars.length);
                    salt += chars.substring(rnum, rnum + 1);
                }
                db.query("INSERT INTO auth_local values(?,?,?,?)", [post.id, sha(post.password + salt), post.email, salt], function (err) {
                    request.login(post, function (err) {
                        request.session.save(function () {
                            return response.json({
                                SERVER_RESPONSE: 1,
                                SERVER_MESSAGE: "Register SUCESS"
                            });
                        });
                    });
                });
            }
        });
    });
    router.get('/logout', function (request, response) {
        request.logout();
        response.redirect(`/`);
    });
    router.get('/kakao', passport.authenticate('kakao'));
    router.get('/kakao/callback', passport.authenticate('kakao'), function (request, response) {
        console.log(request);
        if (!request.user) {
            console.log("kakao_Wrong credentials");
            return response.status(400).json({
                SERVER_RESPONSE: 0,
                SERVER_MESSAGE: "kakao_Wrong Credentials"
            });
        } else {
            console.log("kakao_logged in!");
            return response.json({
                SERVER_RESPONSE: 1,
                SERVER_MESSAGE: "kakao_Logged in!"
            });
        }
    });
    return router;
};
