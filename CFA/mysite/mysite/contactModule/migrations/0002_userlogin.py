# Generated by Django 3.0.3 on 2021-03-13 08:19

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('contactModule', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='UserLogin',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('userID', models.CharField(max_length=100)),
                ('passID', models.CharField(max_length=100)),
            ],
        ),
    ]
