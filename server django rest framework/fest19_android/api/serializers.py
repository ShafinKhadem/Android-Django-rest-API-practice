from django.utils import timezone
from rest_framework import serializers
from .models import HealthEntry


class HealthDataSerializer(serializers.HyperlinkedModelSerializer):

    class Meta:
        model = HealthEntry
        fields = '__all__'


class HealthUserSerializer(serializers.ModelSerializer):
    owner_username = serializers.ReadOnlyField(source='owner.username')  # in foreign key fields source=... specifies:
                                                                # which field from foreign model we want to show in api
    # ModelSerializer tries to deserialize all fields except ReadOnlyField or
    # fields of other type with read_only = true or default read_only = true, these are only to serialize.
    # Instead of mentioning read_only = true in every field, read_only_fields can be used in Meta
    # using python3 manage.py shell, you can create a HealthSerializer object and print(repr(object)) to see that
    # id would be read_only field if following line was absent.
    # default=None is necessary here, if no default then giving a value is must for deserialization, e.g. create.
    # using allow_null only works when serialize e.g. view.
    id = serializers.UUIDField(read_only=False, default=None)
    recordTime = serializers.DateTimeField(label='RecordTime', default=timezone.now())

    class Meta:
        model = HealthEntry
        fields = ('id', 'recordTime', 'distanceTravelled', 'leapCount', 'heartRate', 'owner_username')
