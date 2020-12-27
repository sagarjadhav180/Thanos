from django.http import HttpResponse
from django.shortcuts import render
from django.template import loader

def index(request):
    return HttpResponse("<h1>Hello, world. You're at the polls index.</h1>")
    
def loginPage(TemplateView):
    template = loader.get_template('login.html')
    return HttpResponse(template.render())

def homePage(TemplateView):
    template = loader.get_template('home.html')
    return HttpResponse(template.render())

# Create your views here.
