const express = require('express');
const app = express();
var bodyParser = require('body-parser');
const helmet = require('helmet');
//const cookie = require('cookie-parser');
const db = require('./db.js');
//const axios = require('axios');
//const promise = require('es6-promise').Promise;
const requesting = require('request');
const auth = require('./auth.js')(app); //  authentication routing
app.use('/auth', auth);

app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json())
app.use(helmet());


app.post('/problem', function (request, response) {

    const post = request.body;
    const question = post.question;
    const user_id = post.user_id;
    let option = new Array(4);
    let i;
    for (i = 0; i < 4; i++) {
        option[i] = post.option[i];
    }
    //   mysql에 client 부터 받은 데이터 삽입
    //  파이썬에게 데이터 request하고 callback 함수 실행
    requesting.post({
        url: 'http://52.79.236.65:8000/response/problem_solving',
        body: {
            question: question,
            option: [option[0], option[1], option[2], option[3]]
        },
        json:true
    },
        function (err, machine_res, machine_body) {
            if (err) throw err;
            if (machine_res) { // 파이썬의 대답이 있을경우 client에게 그 답을 보내줌
                const sql = `INSERT INTO dataset (question,option_1,option_2,option_3,option_4,answer,user_id) VALUES (?,?,?,?,?,?,?)`;
                db.query(sql, [question, option[0], option[1], option[2], option[3],machine_body.answer,user_id], function (err, result) {
                    if (err) throw err;
                    console.log("dataset successfully inserted into DB")
                });
                return response.send( { answer: machine_body.answer } );
            }
        });
});
app.get('/problem/daily', function (request, response) {
    const sql = `SELECT * FROM dataset order by rand() limit 1`;
    db.query(sql, function (err, problem) {
        return response.json({
            question: problem[0].question,
            option: [
                problem[0].option_1,
                problem[0].option_2,
                problem[0].option_3,
                problem[0].option_4
            ],
            answer: problem[0].answer
        });
    });
})
app.get('/problem/mine/:user_id',function(request,response){
    const sql=`SELECT question,option_1,option_2,option_3,option_4,answer FROM dataset where user_id="test1" GROUP BY question`;
    db.query(sql,[request.params.user_id],function(err,results){
        if(err)throw err;
        return response.json(results);  // user_id 와 일치하는 문제 선택해 전송
    });
})
app.listen(80);
