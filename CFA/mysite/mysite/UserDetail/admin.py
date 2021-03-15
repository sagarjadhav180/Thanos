from django.contrib import admin

from userdetail.models import logindata, saveuserdata


# Register your models here.

admin.site.register(logindata)
admin.site.register(saveuserdata)

