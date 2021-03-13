from django.db import models

# Create your models here.

class SaveUserDetails(models.Model):
    groups = models.PositiveIntegerField()
    campaign = models.PositiveIntegerField()
    t_number = models.PositiveIntegerField()
    calls = models.PositiveIntegerField()
    Tags = models.PositiveIntegerField()
    Webhooks = models.PositiveIntegerField()
    p_number = models.PositiveIntegerField()
    r_number = models.PositiveIntegerField()
    stage = models.PositiveIntegerField()
    component = models.CharField(max_length=100)

class UserLogin(models.Model):
    userID = models.CharField(max_length=100)
    passID = models.CharField(max_length=100)
    def __str__(self):
        return UserLogin  

