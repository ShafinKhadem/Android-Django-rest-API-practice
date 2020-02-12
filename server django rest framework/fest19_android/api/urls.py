from django.urls import path, include
from . import views
from rest_framework import routers


# don't give any app_name in api, otherwise there will be problem in HyperlinkedModelSerializer
from .views import LogoutView

router = routers.DefaultRouter()
router.register('health_entries', views.HealthDataViewSet)
router.register('health_entries_me', views.HealthUserViewSet, basename='healthuser')

urlpatterns = [
    path('', include(router.urls)),
    path('auth/', include('djoser.urls')),
    path('auth/', include('djoser.urls.authtoken')),
    path('auth/', include('djoser.urls.jwt')),
    path('logout/', LogoutView.as_view()),
    # path('api-auth/', include('rest_framework.urls')),    # for normal login-logout without token
]
