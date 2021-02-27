from django.urls import path
from . import views

urlpatterns = [
    path('git/login/', views.loginPage, name='login'),
    path('home/', views.homePage, name='homePage'),
    path('home/saveDetails', views.homePage, name=''),
]