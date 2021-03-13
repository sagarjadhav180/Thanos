from django.contrib import admin
from contactModule.models import SaveUserDetails, UserLogin

# Register your models here.
admin.site.register(SaveUserDetails)
admin.site.register(UserLogin)
