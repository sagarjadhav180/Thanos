from django.shortcuts import render, HttpResponse, redirect
from django.contrib import messages
from userdetail.models import logindata, saveuserdata
from git import Repo
from subprocess import call


# Create your views here.

def gitSave(request):
    try:
        '''repo = Repo('https://github.com/sagarjadhav180/Thanos.git')
        repo.git.add(update=True)
        repo.index.commit("New CFA User Details Saved in Postgresql")
        origin = repo.remote('origin')
        origin.push()
        origin.push()'''

        call('git config credential.helper store', shell = True)
        call('git add .', shell = True)
        call('git commit --allow-empty -m "Trigger Build"', shell = True)
        # call('git pull', shell = True) -- use this command in case more than one person are comitting 
        call('git push origin master', shell = True)
        
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
        components = request.POST.getlist('component')
        saveuserdetails1 = saveuserdata(groups=groups, campaign=campaign, t_number=t_number, calls=calls, Tags=Tags, Webhooks=Webhooks, p_number=p_number, r_number=r_number, stage=stage, components=components);
        saveuserdetails1.save();
        gitSave(request);
    return render(request, "index.html");

def login(request):
    if request.method == "POST":
        userID = request.POST.get('userID')
        passID = request.POST.get('passID')
        userlogin = logindata(userID=userID, passID=passID) 
        userlogin.save()
        if True:
            messages.success(request, 'Profile details updated.')
            return redirect('/home')
        else:
            messages.error(request, 'Document deleted.')
            return render(request, "login.html");
    return render(request, "login.html");



   