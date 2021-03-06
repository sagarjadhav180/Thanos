# Generated by Django 3.0.3 on 2021-03-15 18:37

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('userdetail', '0002_remove_savedata_idflag'),
    ]

    operations = [
        migrations.CreateModel(
            name='saveuserdata',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('groups', models.PositiveIntegerField()),
                ('campaign', models.PositiveIntegerField()),
                ('t_number', models.PositiveIntegerField()),
                ('calls', models.PositiveIntegerField()),
                ('Tags', models.PositiveIntegerField()),
                ('Webhooks', models.PositiveIntegerField()),
                ('p_number', models.PositiveIntegerField()),
                ('r_number', models.PositiveIntegerField()),
                ('stage', models.PositiveIntegerField()),
                ('components', models.CharField(max_length=100)),
            ],
        ),
        migrations.RenameModel(
            old_name='SaveData',
            new_name='LoginData',
        ),
    ]
