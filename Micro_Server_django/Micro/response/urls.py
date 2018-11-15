from django.urls import path
from django.conf.urls import url
from . import views

urlpatterns = [
    path('problem_solving', views.problem_solving, name='problem_solving')
]