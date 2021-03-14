from django.contrib import admin
from django.urls import path
from contactmodule import views

urlpatterns = [
    path('', views.index, name='index'),
    path('/gitSave', views.gitSave, name='gitSave'),
    path('login', views.login, name='login'),
]