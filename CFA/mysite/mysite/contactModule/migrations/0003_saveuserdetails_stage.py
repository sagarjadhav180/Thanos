# Generated by Django 3.0.3 on 2021-03-13 08:44

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('contactModule', '0002_userlogin'),
    ]

    operations = [
        migrations.AddField(
            model_name='saveuserdetails',
            name='stage',
            field=models.PositiveIntegerField(default=1),
            preserve_default=False,
        ),
    ]