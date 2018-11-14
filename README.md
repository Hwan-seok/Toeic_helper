# Toeic_helper
토풀남 (토익 대신 풀어주는 남자) 프로젝트 KHUVELOPER

* 강환석 Server
* 김원규 Pytorch Machine Learning
* 배상현 Android

# Android
# Main Server(Node.js)
functions

* problem solving : server RE requests to micro server(Django) -> Receive Answer(Django Sended)
                  -> send Answer to client
                  
* Daily Toeic : Serves problem from DataBase(dataset) to client when they want to solve one

* MY Page : Show problems what user asked and give a chance to re-answer it

* authenticate : passport.js 

                -Local Strategy
                
                -KaKao Strategy
                
                -MYSQL Session Store
                
* DataBase : MYSQL

    Tables

        -auth_local
      
        -auth_kakao
      
        -sessions
      
        -dataset_user
      
        -dataset_init
      
# Micro Server(Django) 
  functions
  
  * imports Machine Learning Model
  
  * send answer to Main server when they ask Toeic problem
  
  * derives answer from Machine Learning Model

# Pytorch Machine Learning

# Model 설명
input: 토익 PART5 문제의 빈칸에 보기를 넣은 문장 4개

embed_dim: 32

hidden_dim: 128

used model: LSTM

dropout_p: 0.5


# Model train
총 2개의 데이터셋 으로 진행하였습니다. => 'Sentiment analysis Dataset.csv', '5000+2000.csv'

한 개는 모델 성능을 측정하기 위한 많이 쓰는 데이터 중 하나인 감정 분석 데이터. (train: 90000, test: 10000)

다른 한 개는 저희가 직접 구한 토익 문제 데이터셋 입니다. (train: 31943, test: 9601)

<감정 분석 데이터>

EPOCH = 30

batch_size = 64

<토익 데이터>

EPOCH = 30

batch_size = 64

# Model test

<감정 분석 데이터>

EPOCH = 30

batch_size = 64

최고 정확도: 76.45 % in EPOCH 19

<토익 데이터>

EPOCH = 30

batch_size = 64

최고 정확도: 82.26 % in EPOCH 10

