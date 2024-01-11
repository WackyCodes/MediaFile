package com.wackycodes.mediafile.listener;

public interface OnLocationPermissionListener extends PermissionRequestListener{

    int PERMISSION_CODE_LOCATION = 2;

    boolean isLocationPermissionGranted();
}
