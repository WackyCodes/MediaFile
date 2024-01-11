package com.wackycodes.mediafile.listener;

public interface OnCameraPermissionListener extends PermissionRequestListener {
    int PERMISSION_CODE_CAMERA = 3;
    boolean isCameraPermissionGranted();

}
