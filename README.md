# Android MVVM + Django rest API practice

Minimalistic app + server for rest api, android MVVM and sync practice.


### How to run

Connect android device and PC to same wifi. Find out your PC's IPv4 address. In ubuntu it can be viewed in Network -> see details of the wifi connection. My laptop's IPv4 address is 192.168.0.103, so from terminal in server root directory (where manage.py is located), first to create a new superuser, `python3 manage.py createsuperuser` (a superuser named shafin with password shafin already exists), then run `python3 manage.py runserver 192.168.0.103:8000`. Then to create a new user, go to 192.168.0.103:8000/accounts/signup/ or to view or change everything as an admin, go to 192.168.0.103:8000/admin/. View all available server urls in urls.py.

Change API_BASE = "192.168.0.103:8000/api/" in android's LoginActivity.java. Now you should be able to access server from app.



### UUID

It's not feasible to use normal incrementing primary key in a distributed system. If same user creates different data from different devices, they may have same primary key in local database, which will cause in collision while syncing.


### Biggest mistakes

- Using auto incrementing ID as primary key instead of UUID.
- Forgetting to add django app in settings.py after startapp.
- Forgetting to handle null pointer exception in android many times.



### Future guides for starting django rest framework projects

```bash
pip3 install pipenv
pipenv shell
pipenv install django djangorestframework django-cors-headers
django-admin startproject backend
cd backend
python3 manage.py startapp api && echo "urlpatterns = []" > api/urls.py
```

- add `path('api/', include('api.urls')),` in backend/urls.py

- vscode select python enterpreter: select the pipenv environment.

- in backend/settings.py,
    - make DATABASES = {}
    - add to INSTALLED_APPS:
        ```
        'rest_framework',
        'corsheaders',
        'backend',
        'api.apps.ApiConfig',
        ```
    - prepend `'corsheaders.middleware.CorsMiddleware',` to MIDDLEWARE
    - append `CORS_ALLOWED_ORIGINS = ['http://localhost']`

- add the following in api/admin.py.
    ```
    from django.apps import apps
    from django.contrib.admin.sites import AlreadyRegistered

    app_models = apps.get_app_config('api').get_models()
    for model in app_models:
        try:
            admin.site.register(model)
        except AlreadyRegistered:
            pass
    ```

- `python3 backend/manage.py migrate`

- `python3 backend/manage.py createsuperuser`

- `python3 backend/manage.py runserver`

- create new models in api/models.py. Any change in models have to be followed by:
`python3 backend/manage.py makemigrations && python3 backend/manage.py migrate`

- django auto provides User model. We can add attributes to this model by adding models.py in backend & [following this](https://docs.djangoproject.com/en/4.0/topics/auth/customizing/#using-a-custom-user-model-when-starting-a-project). use User in other models by `owner = models.ForeignKey(django.contrib.auth.get_user_model(), models.CASCADE)`. 

