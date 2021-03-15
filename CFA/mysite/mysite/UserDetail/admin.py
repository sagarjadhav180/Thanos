from django.contrib import admin

from UserDetail.models import LoginData, SaveUserData


# Register your models here.

admin.site.register(LoginData)
admin.site.register(SaveUserData)

