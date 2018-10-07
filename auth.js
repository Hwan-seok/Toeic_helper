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

    router.post('/login',
        passport.authenticate('local', {
            successRedirect: '/',
            failureRedirect: '/auth/login'
        })
    );
    router.post('/register', function (request, response) { //name= {id , password} 으로 받음 
        const post = request.body;
	console.log(post);
        console.log(post.id);
	console.log(post.password);
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz!@#$%^&*()";
        var string_length = 15;
        var salt = '';
        db.query('SELECT id FROM auth_local WHERE id=?', post.id, function (err, result) { 
            if (result[0]){
            console.log(result[0]);
                return response.redirect('/auth/register');// 이미 존재하는 아이디이면 다시 팅김
        }
            else {
                for (var i = 0; i < string_length; i++) {
                    var rnum = Math.floor(Math.random() * chars.length);
                    salt += chars.substring(rnum, rnum + 1);
                }
                db.query("INSERT INTO auth_local values(?,?,?,?)", [post.id, sha(post.password + salt),post.email,salt], function (err) {
                    request.login(post, function (err) {
                        request.session.save(function () {
                            return response.redirect(`/`);
                        })
                    })
                });
            }
        })

    });
    router.get('/logout', function (request, response) {
        request.logout();
        response.redirect(`/`);
    });
    router.get('/kakao', passport.authenticate('kakao'));
    router.get('/kakao/callback', passport.authenticate('kakao',{
        successRedirect : '/',
        failureRedirect : '/login'
    }));
    return router;
};
