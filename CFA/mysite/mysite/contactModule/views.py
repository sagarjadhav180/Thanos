from django.shortcuts import render, HttpResponse
from contactModule.models import SaveUserDetails

# Create your views here.


def index(request):
    if request.method == "POST":
        groups = request.POST.get('group')
        campaign = request.POST.get('campaign')
        t_number = request.POST.get('track')
        calls = request.POST.get('call')
        Tags = request.POST.get('tag')
        Webhooks = request.POST.get('webhook')
        p_number = request.POST.get('premium')
        r_number = request.POST.get('reserve')
        component = request.POST.get('component')
        saveuserdetails = SaveUserDetails(groups=groups, campaign=campaign, t_number=t_number, calls=calls, Tags=Tags, Webhooks=Webhooks, p_number=p_number, r_number=r_number, component=component);
        saveuserdetails.save();
    return render(request, "index.html");

def about(request):
    return HttpResponse("Hi, This is about page");

def login(request):
    return render(request, "login.html");

def service(request):
    return HttpResponse("Hi, This is service page"); 

def contact(request):
    return HttpResponse("Hi, This is contact page");        