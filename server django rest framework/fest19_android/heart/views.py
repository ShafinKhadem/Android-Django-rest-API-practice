from django.contrib.auth import authenticate, login
from django.db.models.functions import Coalesce
from django.shortcuts import render, redirect
from django.urls import reverse_lazy
from django.utils import timezone
from django.views.generic import ListView, DetailView, CreateView, UpdateView, DeleteView
from django.contrib.auth.mixins import LoginRequiredMixin, UserPassesTestMixin

from .forms import SignUpForm
from api.models import HealthEntry


# Create your views here.

class HealthEntryListView(ListView):
    model = HealthEntry
    template_name = 'heart/index.html'
    paginate_by = 50


class HealthEntryDetailView(DetailView):
    model = HealthEntry
    template_name = 'heart/detail.html'


class UserHealthListView(LoginRequiredMixin, ListView):
    template_name = 'heart/index.html'
    paginate_by = 10

    def get_queryset(self):
        return HealthEntry.objects.filter(owner_id=self.request.user.id).order_by(Coalesce('recordTime', 'id').desc())


class UserHealthCreateView(LoginRequiredMixin, CreateView):
    model = HealthEntry
    template_name = 'heart/create.html'
    fields = ['distanceTravelled', 'leapCount', 'heartRate']

    def form_valid(self, form):
        form.instance.owner = self.request.user
        form.instance.recordTime = timezone.now()
        return super().form_valid(form)


class UserHealthUpdateView(LoginRequiredMixin, UserPassesTestMixin, UpdateView):
    model = HealthEntry
    template_name = 'heart/update.html'
    fields = ['distanceTravelled', 'leapCount', 'heartRate']

    def form_valid(self, form):
        form.instance.owner = self.request.user
        form.instance.recordTime = timezone.now()
        return super().form_valid(form)

    def test_func(self):
        return self.request.user.id == self.get_object().owner_id


class UserHealthDeleteView(LoginRequiredMixin, UserPassesTestMixin, DeleteView):
    model = HealthEntry
    template_name = 'heart/delete.html'
    success_url = reverse_lazy('heart:index')

    def test_func(self):
        return self.request.user.id == self.get_object().owner_id


def signup(request):
    if request.method == 'POST':
        form = SignUpForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=raw_password)
            login(request, user)
            return redirect('heart:user_index')
    else:
        form = SignUpForm()
    return render(request, 'registration/signup.html', {'form': form})
