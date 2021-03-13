from django.shortcuts import render, HttpResponse, redirect
from django.contrib import messages
from contactModule.models import SaveUserDetails, UserLogin
from git import Repo


# Create your views here.

def gitSave(request):
    try:
        repo = Repo(r'https://github.com/sagarjadhav180/Thanos/.git')
        repo.git.add(update=True)
        repo.index.commit("New CFA User Details Saved in Postgresql")
        origin = repo.remote('origin')
        origin.push()
        origin.push()
        
    except Exception as e:
        print(e, type(e))

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
        stage = request.POST.get('stageENV')
        component = request.POST.get('component')
        saveuserdetails = SaveUserDetails(groups=groups, campaign=campaign, t_number=t_number, calls=calls, Tags=Tags, Webhooks=Webhooks, p_number=p_number, r_number=r_number, stage=stage, component=component);
        saveuserdetails.save();
        gitSave(request);
    return render(request, "index.html");

def login(request):
    if request.method == "POST":
        userID = request.POST.get('userID')
        passID = request.POST.get('passID')
        #userlogin = UserLogin(userID=userID, passID=passID) 
        #loginFlag = userlogin.all();
        # print(loginFlag)
        if True:
            messages.success(request, 'Profile details updated.')
            return redirect('/')
        else:
            messages.error(request, 'Document deleted.')
            return render(request, "login.html");
    return render(request, "login.html");



   