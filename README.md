# Toeic_helper
토풀남 (토익 대신 풀어주는 남자) 프로젝트 KHUVELOPER

#used

##Android
##Node_JS
##Django 
##Pytorch Machine Learning

강환석 김원규 배상현

# Model 설명
input: 토익 PART5 문제의 빈칸에 보기를 넣은 문장 4개

embed_dim: 32

hidden_dim: 128

used model: LSTM

dropout_p: 0.5


# Model train
총 2개의 데이터셋 으로 진행하였습니다. => 'Sentiment analysis Dataset.csv', '5000+2000.csv'

한 개는 모델 성능을 측정하기 위한 많이 쓰는 데이터 중 하나인 감정 분석 데이터.

다른 한 개는 저희가 직접 구한 토익 문제 데이터셋 입니다.

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

<토익 데이터>

EPOCH = 30

batch_size = 64
