from django.shortcuts import render
import json
from django.http import JsonResponse
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def problem_solving(request):
     if request.method == 'POST':
        print("check")
        request_data = ((request.body).decode('utf-8'))
        request_data = json.loads(request_data)
        question = request_data['question']
        option_1 =request_data['option'][0]
        option_2 =request_data['option'][1]
        option_3 =request_data['option'][2]
        option_4 =request_data['option'][3]
        answer="ss"
        return JsonResponse({"answer":answer})