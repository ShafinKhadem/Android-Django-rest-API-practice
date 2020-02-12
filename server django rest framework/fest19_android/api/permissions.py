from rest_framework import permissions


# object level permission

class IsOwnerOrAdminOrReadOnly(permissions.BasePermission):
    """
    Object-level permission to only allow owners of an object to edit it.
    Assumes the model instance has an `username_id` attribute, which denotes owner's id.
    """

    def has_object_permission(self, request, view, obj):
        # Read permissions are allowed to any request,
        # so we'll always allow GET, HEAD or OPTIONS requests.
        if request.method in permissions.SAFE_METHODS:
            return True

        # Instance must have an attribute named `username_id`.
        return request.user.is_staff or request.user.pk == obj.username_id


# class level permission example:

# class BlacklistPermission(permissions.BasePermission):
#     """
#     Global permission check for blacklisted IPs.
#     """
#
#     def has_permission(self, request, view):
#         ip_addr = request.META['REMOTE_ADDR']
#         blacklisted = Blacklist.objects.filter(ip_addr=ip_addr).exists()
#         return not blacklisted
