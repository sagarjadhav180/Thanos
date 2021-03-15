from django.db import models

# Create your models here.

class LoginData(models.Model):
    tableName = 'logindata'
    userID = models.CharField(max_length=100)
    passID = models.CharField(max_length=100)
    def __str__(self):
        return self.tableName  

class SaveUserData(models.Model):
    tableName = 'saveuserdata'
    groups = models.PositiveIntegerField()
    campaign = models.PositiveIntegerField()
    t_number = models.PositiveIntegerField()
    calls = models.PositiveIntegerField()
    Tags = models.PositiveIntegerField()
    Webhooks = models.PositiveIntegerField()
    p_number = models.PositiveIntegerField()
    r_number = models.PositiveIntegerField()
    stage = models.PositiveIntegerField()
    components = models.CharField(max_length=100)
    def __str__(self):
        return self.tableName           
