from django.db import models
from django.db.models import CharField, Model 
from django.contrib.postgres.fields import ArrayField

# Create your models here.

# class SaveUserDetails(models.Model):
#     groups = models.PositiveIntegerField()
#     campaign = models.PositiveIntegerField()
#     t_number = models.PositiveIntegerField()
#     calls = models.PositiveIntegerField()
#     Tags = models.PositiveIntegerField()
#     Webhooks = models.PositiveIntegerField()
#     p_number = models.PositiveIntegerField()
#     r_number = models.PositiveIntegerField()
#     stage = models.PositiveIntegerField()
#     component = models.CharField(max_length=100)

# class UserLogin(models.Model):
#     tableName = 'userlogin'
#     userID = models.CharField(max_length=100)
#     passID = models.CharField(max_length=100)
#     def __str__(self):
#         return self.tableName  

class saveuserdata(models.Model):
    groups = models.PositiveIntegerField()
    campaign = models.PositiveIntegerField()
    t_number = models.PositiveIntegerField()
    calls = models.PositiveIntegerField()
    Tags = models.PositiveIntegerField()
    Webhooks = models.PositiveIntegerField()
    p_number = models.PositiveIntegerField()
    r_number = models.PositiveIntegerField()
    stage = models.PositiveIntegerField()
    component = ArrayField(models.CharField(max_length=100, blank=True),size=20)
    # component = models.CharField(max_length=100)

class userlogindata(models.Model):
    tableName = 'userlogindata'
    userID = models.CharField(max_length=100)
    passID = models.CharField(max_length=100)
    def __str__(self):
        return self.tableName    