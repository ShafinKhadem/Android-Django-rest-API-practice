# Android + rest API practice

Minimalistic app + server for rest api and sync practice.

### UUID

It's not feasible to use normal incrementing primary key in a distributed system. If same user creates different data from different devices, they may have same primary key in local database, which will cause in collision while syncing.


### How to run

Connect android device and PC to same wifi. Find out your PC's IPv4 address. In ubuntu it can be viewed in Network -> see details of the wifi connection. My laptop's IPv4 address is 192.168.0.103, so from terminal in server root directory (where manage.py is located), first to create a new superuser, `python3 manage.py createsuperuser` (a superuser named shafin with password shafin already exists), then run `python3 manage.py runserver 192.168.0.103:8000`. Then to create a new user, go to 192.168.0.103:8000/accounts/signup/ or to view or change everything as an admin, go to 192.168.0.103:8000/admin/. View all available server urls in urls.py.

Change API_BASE = "192.168.0.103:8000/api/" in android's LoginActivity.java. Now you should be able to access server from app.