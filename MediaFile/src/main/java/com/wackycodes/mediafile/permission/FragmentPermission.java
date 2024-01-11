package com.wackycodes.mediafile.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.wackycodes.mediafile.listener.OnPermissionListener;

public class FragmentPermission extends Fragment implements OnPermissionListener {
    private Context context;

    public FragmentPermission() {
    }

    public FragmentPermission(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void showDebugLog(String logMsg) {
        Log.d("LOG", " " + logMsg);
    }

    private Context getCurrentContext() {
        return (context != null ? context : requireContext());
    }

    //----------------------------------------------------------------------------------------------

    private int permissionCode;
    @Override
    public void onPermissionGranted(int permissionCode, int accessCode) {

    }

    public boolean isLocationPermissionGranted() {
        try {
            return ContextCompat.checkSelfPermission( getCurrentContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission( getCurrentContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isCameraPermissionGranted() {
        try {
            return ContextCompat.checkSelfPermission(getCurrentContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStoragePermissionGranted() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return Environment.isExternalStorageManager();
//                return ContextCompat.checkSelfPermission(getCurrentContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            } else
                return ContextCompat.checkSelfPermission(getCurrentContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getCurrentContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void requestForPermission(int permissionCode) {
        this.permissionCode = permissionCode;
        switch (permissionCode) {
            case PERMISSION_CODE_STORAGE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //request for the permission
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    getRequestStoragePermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                }
                break;
            case PERMISSION_CODE_LOCATION:
                getRequestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                break;
            case PERMISSION_CODE_CAMERA:
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                break;
            default:
                break;
        }
    }
    @Override
    public void requestForcePermission(int permissionCode) {
        String permission = "";
        this.permissionCode = permissionCode;
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
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
    public void requestPermissionSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getCurrentContext().getPackageName(), null);
        intent.setData(uri);
        this.startActivity(intent);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    onPermissionGranted(permissionCode, ACCESS_GRANTED);
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    onPermissionGranted(permissionCode, ACCESS_DENIED);
                }
            });

    private final ActivityResultLauncher<String[]> getRequestStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                // Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                if (
                        (result.containsKey(Manifest.permission.WRITE_EXTERNAL_STORAGE) && Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)))
                                &&
                                (result.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE) && Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)))
                ) {
                    // permissionGranted!
                    onPermissionGranted(PERMISSION_CODE_STORAGE, ACCESS_GRANTED);
                } else {
                    onPermissionGranted(PERMISSION_CODE_STORAGE, ACCESS_DENIED);
                }
            });

    private final ActivityResultLauncher<String[]> getRequestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (
                        (result.containsKey(Manifest.permission.ACCESS_FINE_LOCATION) && Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION)))
                                &&
                                (result.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION) && Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION)))
                ) {
                    // permissionGranted!
                    onPermissionGranted(PERMISSION_CODE_LOCATION, ACCESS_GRANTED);
                } else {
                    onPermissionGranted(PERMISSION_CODE_LOCATION, ACCESS_DENIED);
                }
            });



}
