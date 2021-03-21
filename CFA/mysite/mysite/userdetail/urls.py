from django.contrib import admin
from django.urls import path
from userdetail import views

urlpatterns = [
    # path('home', views.index, name='index'),
    # path('', views.login, name='login'),

    path('home', views.index, name='index'),
    path('login', views.login, name='login'),
    path('', views.login, name='login'),
]