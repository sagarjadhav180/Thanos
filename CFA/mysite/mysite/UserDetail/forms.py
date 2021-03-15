from django import forms 
  
DEMO_CHOICES =( 
    ("1", "Naveen"), 
    ("2", "Pranav"), 
    ("3", "Isha"), 
    ("4", "Saloni"), 
) 
class userform(forms.Form): 
    componentfield = forms.MultipleChoiceField(choices = DEMO_CHOICES) 