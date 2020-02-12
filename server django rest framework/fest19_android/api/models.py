from django.db import models
from django.contrib.auth import get_user_model
from django.urls import reverse
import uuid

# Create your models here.


class HealthEntry(models.Model):
    # even if following line is not given, an auto incrementing id is generated
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    owner = models.ForeignKey(get_user_model(), on_delete=models.CASCADE)    # also see models.ManyToManyField
    recordTime = models.DateTimeField()
    distanceTravelled = models.PositiveIntegerField()
    leapCount = models.PositiveIntegerField()
    heartRate = models.PositiveSmallIntegerField()

    def __str__(self):
        return '<HealthEntry:owner = '+str(self.owner)+', distanceTravelled = '+str(self.distanceTravelled)+', leapCount = '+str(self.leapCount)+', heartRate = '+str(self.heartRate)+', recordTime = '+str(self.recordTime)+'>'

    def get_absolute_url(self):     # this is called to redirect from CreateView, UpdateView, DeleteView
        return reverse('heart:detail', kwargs={'pk': self.pk})
