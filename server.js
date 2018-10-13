const express = require('express');
const app = express();
var bodyParser = require('body-parser');
const helmet = require('helmet');
//const cookie = require('cookie-parser');
const db = require('./db.js');
//const axios = require('axios');
//var promise = require('es6-promise').Promise;
const requesting = require('request');
const auth = require('./auth.js')(app); //  authentication routing
app.use('/auth', auth);

app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json())
app.use(helmet());


app.get('/', function (request, response) {
    response.send(`
    <a href="/auth/login">login</a>
    <a href="/auth/register">register</a>
    <a href="/auth/logout">logout</a>
    <a href="/auth/kakao">
    <img src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png"/>
    </a>
    ${request.user && 1}
    `);
});
app.get('/auth/login', function (request, response) {
    response.send(`
    <form action = "/auth/login" method="post">
    <input type ="text" name="id" placeholder = "id">
    <input type ="text" name="password" placeholder = "password">
    <input type ="submit">
    </form>
    `);
});
app.get('/auth/register', function (request, response) {
    response.send(`
    <form action = "/auth/register" method="post">
    <input type ="text" name="id" placeholder = "id">
    <input type ="text" name="password" placeholder = "password">
    <input type ="text" name="email" placeholder = "email@example.com">
    <input type ="submit">
    </form>
    `);
});
app.post('/problem', function (request, response) {

    const post = request.body;
    console.log(request.body);

    const question = post.question;
    let option = new Array(4);
    let i;
    for (i = 0; i < 4; i++) {
        option[i] = post.option[i];
    }
    //   mysql에 client 부터 받은 데이터 삽입
    //  파이썬에게 데이터 request하고 callback 함수 실행
    requesting.post({
        url: 'http://1.201.138.251:80/testing',
        body: {
            question: question,
            option: [option[0], option[1], option[2], option[3]]
        },
        json:true
    },
        function (err, machine_res, machine_body) {
            if (err) throw err;
            if (machine_res) { // 파이썬의 대답이 있을경우 client에게 그 답을 보내줌

                db.query(`INSERT INTO dataset (question,option_1,option_2,option_3,option_4,answer) VALUES (?,?,?,?,?,?)`, [question, option[0], option[1], option[2], option[3],machine_body.answer], function (err, result) {
                    if (err) throw err;
                    console.log("dataset successfully inserted into DB")
                });
                return response.send( { answer: machine_body.answer } );
            }
        });
});
app.get('/problem/daily', function (request, response) {
    db.query(`SELECT * FROM dataset order by rand() limit 1`, function (err, problem) {
        return response.json({
            question: problem[0].question,
            option: [problem[0].option_1,
                problem[0].option_2,
                problem[0].option_3,
                problem[0].option_4
            ],
            answer: problem[0].answer
        });
    });
})
app.post('/testing',function(request,response){
    response.json({
        answer:`this is the machine's answer`
    })
})
app.listen(80);
