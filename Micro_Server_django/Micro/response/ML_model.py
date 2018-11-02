# coding: utf-8

# In[ ]:


import csv
import numpy as np
from torch.autograd import Variable
from torch.utils.data import Dataset, DataLoader
import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.nn.utils.rnn import pack_padded_sequence, pad_packed_sequence
import nltk

nltk.download('punkt')
import time
import pandas as pd
import pickle


# In[ ]:


# make torch
def make_variables(sentences, label, vocabulary):
    final_sentences = []

    # tokenizing
    for index, sentence in enumerate(sentences):
        final_sentences.append(nltk.word_tokenize(sentence))

    # indexing
    for index, sentence in enumerate(final_sentences):
        final_sentences[index] = [vocabulary[word] for word in sentence]

    # 각자의 seq_length 구하기 (미니배치별로 진행)
    seq_lengths = []
    for sentence in final_sentences:
        seq_lengths.append(len(sentence))
    seq_lengths = torch.LongTensor(seq_lengths)

    #     print("패딩전")
    #     print(final_sentences[:5])
    #     print(seq_lengths[:5])
    #     print(label[:5])
    return padding_tensor_sorting(final_sentences, seq_lengths, label)


# In[ ]:


def padding_tensor_sorting(sentences, seq_lengths, label):
    seq_tensor = torch.zeros((len(sentences), seq_lengths.max())).long()
    print('seq_tensor(max):', seq_lengths.max())
    for idx, (seq, seq_len) in enumerate(zip(sentences, seq_lengths)):
        seq_tensor[idx, :seq_len] = torch.LongTensor(seq)

    if len(seq_tensor) != 1:
        seq_lengths, perm_idx = seq_lengths.sort(0, descending=True)
        seq_tensor = seq_tensor[perm_idx]

    target = torch.tensor(label, dtype=torch.long)

    if len(seq_tensor) != 1 and len(target) != 0:
        target = target[perm_idx]

    #     print("패딩후")
    #     print(seq_tensor[:5])
    #     print(seq_lengths[:5])
    #     print(target[:5])
    return create_variable(seq_tensor), create_variable(seq_lengths), create_variable(target)


# In[ ]:


def create_variable(tensor):
    # tensor를 gpu 이용 가능한지
    if torch.cuda.is_available():
        return Variable(tensor.cuda())
    else:
        return Variable(tensor)


# In[ ]:


def pre_process_test(sentence, a1, a2, a3, a4):
    raw_data = []
    sentences = []

    temp = []
    temp.append(sentence)
    temp.append(a1)
    temp.append(a2)
    temp.append(a3)
    temp.append(a4)
    raw_data.append(temp)

    print("총 문제 개수: ", len(raw_data))
    print("총 문장 개수: ", len(raw_data) * 4)
    for row_index, row in enumerate(raw_data):
        row_sentence = []
        row_label = []
        hype1 = 0
        hype2 = 0
        for item_index, item in enumerate(row):
            # .뒤에 나오는 것 다 없애기 .이 여러 개 있을지 모르니 마지막 .을 이용하기. 두 문장인 경우 .과 ?과 !이 존재
            # 특수문자 앞에 공백으로 하기
            # 공백 없애기
            # 공백 두개, 세개 -> 한개로 바꾸기
            # _____,----- 연달아 있을시 index 찾아 양쪽 공백 만들기
            # 소문자로 바꾸기

            if item_index == 0:
                #
                index_list = []
                index_of_dot = item.rfind('.')
                index_of_question = item.rfind('?')
                index_of_surprise = item.rfind('!')
                index_list.append(index_of_dot)
                index_list.append(index_of_question)
                index_list.append(index_of_surprise)
                standard = max(index_list) + 1
                if standard > 0:
                    item = item[:standard]

                    #
                item = item.strip()
                #
                item = item.replace("  ", " ")
                item = item.replace("  ", " ")

                #
                index_of_hype1 = item.find('__')
                index_of_hype2 = item.find('--')
                index_of_hype3 = item.rfind('__')
                index_of_hype4 = item.rfind('--')
                if index_of_hype1 > index_of_hype2:
                    if index_of_hype3 > index_of_hype4:
                        hype1 = index_of_hype1
                        hype2 = index_of_hype3 + 1
                elif index_of_hype2 > index_of_hype1:
                    if index_of_hype4 > index_of_hype3:
                        hype1 = index_of_hype2
                        hype2 = index_of_hype4 + 1

                # ----- => hype1 = 0 , hype2 = 4
                if hype1 == 0:
                    if (hype2 + 1) < len(item):
                        if item[hype2 + 1] != ' ':
                            item = item[:hype2 + 1] + ' ' + item[hype2 + 1:]
                else:
                    if item[hype1 - 1] != ' ':
                        item = item[:hype1] + ' ' + item[hype1:]
                        hype1 = hype1 + 1
                        hype2 = hype2 + 1
                    if (hype2 + 1) < len(item):
                        if item[hype2 + 1] != ' ':
                            item = item[:hype2 + 1] + ' ' + item[hype2 + 1:]
                #
                item = item.lower()
                # 문장이 아닐 때
            else:
                item = item.strip()
            raw_data[row_index][item_index] = item
        # sentence 4개 만들기
        row[0] = row[0].replace(row[0][hype1:hype2 + 1], "")
        for i in range(1, 5):
            sentence = row[0][:hype1] + row[i] + row[0][hype1:]
            row_sentence.append(sentence)

        sentences.append(row_sentence)

    sentences = np.array(sentences)
    sentences = sentences.flatten()

    return sentences


# In[2]:


class myModel(nn.Module):
    def __init__(self, n_layers, hidden_dim, n_vocab, embed_dim, n_classes, bidirectional=True, dropout_p=0.2):
        super(myModel, self).__init__()

        self.n_layers = n_layers
        self.hidden_dim = hidden_dim
        self.n_vocab = n_vocab
        self.embed_dim = embed_dim
        self.n_classes = n_classes
        self.n_directions = int(bidirectional) + 1

        self.embed = nn.Embedding(n_vocab, embed_dim)
        self.dropout = nn.Dropout(dropout_p)
        #         self.lstm = nn.LSTM(self.embed_dim, self.hidden_dim,
        #                             num_layers=self.n_layers,
        #                             dropout=dropout_p,
        #                             batch_first=True)
        self.lstm = nn.GRU(self.embed_dim, self.hidden_dim,
                           self.n_layers,
                           # dropout=dropout_p,
                           batch_first=True)
        #         self.relu = nn.ReLU()
        self.out = nn.Linear(self.hidden_dim, self.n_classes)

    #         self.fc1 = nn.Linear(self.hidden_dim, 50)
    #         self.fc2 = nn.Linear(50, self.n_classes)
    def forward(self, x, seq_lengths):
        x = x.t()
        sen_len = x.size(0)
        batch_size = x.size(1)
        print(self.n_layers, self.hidden_dim, self.n_vocab, self.embed_dim, self.n_classes, self.n_directions)

        embedded = self.embed(x)

        #         print(embedded)
        lstm_input = pack_padded_sequence(embedded, seq_lengths.data.cpu().numpy())
        self.hidden = self._init_hidden(batch_size)
        self.lstm.flatten_parameters()

        lstm_out, self.hidden = self.lstm(lstm_input)
        lstm_out, lengths = pad_packed_sequence(lstm_out)

        #         h_t = self.dropout(self.hidden[-1])
        #         logit = self.out(h_t[-1])
        logit = self.out(self.hidden[-1])
        #         print(logit)
        return logit

    def _init_hidden(self, batch_size):
        hidden = torch.zeros((self.n_layers, self.n_directions,
                              batch_size, self.hidden_dim))
        return create_variable(hidden)


# In[ ]:


def one_problem_test(sentence, a1, a2, a3, a4):
    sentences = pre_process_test(sentence, a1, a2, a3, a4)

    # 단어집 불러오기
    file = open("vocabulary", "rb")
    vocabulary = pickle.load(file)
    file.close()

    vocab = set()
    temp = nltk.word_tokenize(sentence)
    vocab.update(temp)

    length = len(vocabulary)

    for i, word in enumerate(vocab):
        vocabulary[word] = length + i
    vocabsize_t = len(vocabulary)
    print(vocabsize_t)

    # 모델 부르기
    test_model = torch.load('saved_gru')
    test_model.n_vocab = vocabsize_t
    test_model.embed = nn.Embedding(valid_model.n_vocab, valid_model.embed_dim)

    input, seq_lengths, target = make_variables(sentences, [], vocabulary)

    output = test_model(input, seq_lengths)
    pred = output.data.max(1, keepdim=True)[1]
    print("softmax 직후...", output)
    print("1개만 고른후...", pred)

    one_indice = []
    for i, pred_item in enumerate(pred):
        if pred_item[0] == 1:
            one_indice.append(i)
    if len(one_indice) == 1:
        return one_indice[0]
    else:
        result_list = []
        for index in one_indice:
            result_list.append(pred[index][0])
        return result_list.index(max(result_list))
