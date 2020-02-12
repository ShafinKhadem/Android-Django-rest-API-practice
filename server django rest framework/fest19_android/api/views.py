from django.db.models.functions import Coalesce
from djoser.views import TokenDestroyView
from rest_framework import viewsets, permissions, status
from rest_framework.response import Response

from .models import HealthEntry
from .permissions import IsOwnerOrAdminOrReadOnly
from .serializers import HealthDataSerializer, HealthUserSerializer


# Create your views here.

class HealthDataViewSet(viewsets.ModelViewSet):
    queryset = HealthEntry.objects.all()
    serializer_class = HealthDataSerializer
    permission_classes = [permissions.IsAuthenticatedOrReadOnly, IsOwnerOrAdminOrReadOnly, ]


class HealthUserViewSet(viewsets.ModelViewSet):
    queryset = HealthEntry.objects.all()
    serializer_class = HealthUserSerializer

    def get_queryset(self):
        return self.queryset.filter(owner_id=self.request.user.id).order_by('-recordTime')

    def perform_create(self, serializer):
        # By print(self.request.data, serializer.validated_data), you can check how serializer validation discards
        # extra or read only fields even if you give it.
        serializer.save(owner=self.request.user)    # in BaseSerializer.save(), this kwarg is added to validatd_data

    def perform_update(self, serializer):
        serializer.save(owner=self.request.user)


class LogoutView(TokenDestroyView):

    def post(self, request):
        super().post(request)
        return Response({'Logout_status': 'OK'}, status=status.HTTP_200_OK)
