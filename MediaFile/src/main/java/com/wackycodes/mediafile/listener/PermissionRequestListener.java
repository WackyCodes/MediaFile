package com.wackycodes.mediafile.listener;

public interface PermissionRequestListener {
    int ACCESS_GRANTED = 1;
    int ACCESS_DENIED = 0;
    int ACCESS_NOT_SURE = -1;
    void requestForPermission( int permissionCode );
    void requestForcePermission( int permissionCode );
    void onPermissionGranted( int permissionCode, int accessCode );

}
