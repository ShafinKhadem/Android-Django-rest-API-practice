from django.urls import path, include
from . import views


app_name = 'heart'

urlpatterns = [
    path('', views.HealthEntryListView.as_view(), name='index'),
    path('detail/<uuid:pk>', views.HealthEntryDetailView.as_view(), name='detail'),
    path('accounts/', include('django.contrib.auth.urls')),
    path('accounts/profile/', views.UserHealthListView.as_view(), name='user_index'),
    path('accounts/signup/', views.signup, name='signup'),
    path('create/', views.UserHealthCreateView.as_view(), name='create'),
    path('update/<uuid:pk>', views.UserHealthUpdateView.as_view(), name='update'),
    path('delete/<uuid:pk>', views.UserHealthDeleteView.as_view(), name='delete'),
]
