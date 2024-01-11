package com.wackycodes.mediafile.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wackycodes.mediafile.listener.OnPermissionListener;

public class ActivityPermission extends AppCompatActivity implements OnPermissionListener {

    @Override
    public void onPermissionGranted(int permissionCode, int accessCode) {
        // TODO : Override this method into child class to check the permission.
    }

    @Override
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
           return true;
        }else return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean isLocationPermissionGranted() {
        try {
            return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestForPermission(int permissionCode) {
        switch (permissionCode) {
            case PERMISSION_CODE_STORAGE:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_CODE_STORAGE);
                break;
            case PERMISSION_CODE_LOCATION:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_CODE_LOCATION
                );
                break;
            case PERMISSION_CODE_CAMERA:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CODE_CAMERA
                );
                break;
            default:
                break;
        }
    }

    @Override
    public void requestForcePermission(int permissionCode) {
        String permission = "";
        switch (permissionCode) {
            case PERMISSION_CODE_STORAGE:
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                break;
            case PERMISSION_CODE_LOCATION:
                permission = Manifest.permission.ACCESS_COARSE_LOCATION;
                break;
            case PERMISSION_CODE_CAMERA:
                permission = Manifest.permission.CAMERA;
                break;
            default:
                permission = null;
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permission != null) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                requestForPermission(permissionCode);
            } else {
                // We passed true.. since there is no matched!
                showDebugLog("Permission Passed! Code = " + permissionCode);
//                onPermissionGranted( permissionCode, true );
                requestPermissionSetting();
            }
        } else {
            // We passed true.. since there is no matched!
            showDebugLog("Permission Passed! App is <= M :: Permission Code = " + permissionCode);
            onPermissionGranted(permissionCode, ACCESS_NOT_SURE);
        }
    }

    private void requestPermissionSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        this.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int accessCode;
        if ((grantResults.length >= 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                ||
                (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        ) {
            accessCode = ACCESS_GRANTED;
        } else {
            accessCode = ACCESS_DENIED;
        }
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE:
            case PERMISSION_CODE_LOCATION:
            case PERMISSION_CODE_CAMERA:
                onPermissionGranted(requestCode, accessCode);
                break;
            default:
//                int permissionCode = requestCode == REQUEST_PERMISSION_STORAGE ? PERMISSION_CODE_STORAGE :
//                        (requestCode == REQUEST_PERMISSION_LOCATION ? PERMISSION_CODE_LOCATION : -1);
//            showToast( getString( R.string.permission_denied ));
                onPermissionGranted(-1, ACCESS_NOT_SURE);
                break;
        }
    }

    // Show Or Hide Keyboard...
    private void showHideKeyBord(View view, boolean isHide) {
        if (isHide) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //Hide..!
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            //Show..!
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void showDebugLog(@Nullable String message) {
        Log.d("ActivityPermission", " Message : " + message);
    }


}
