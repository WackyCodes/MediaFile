package com.wackycodes.mediafile.listener;

public interface OnStoragePermissionListener extends PermissionRequestListener {
    int PERMISSION_CODE_STORAGE = 1;
    boolean isStoragePermissionGranted();
}
