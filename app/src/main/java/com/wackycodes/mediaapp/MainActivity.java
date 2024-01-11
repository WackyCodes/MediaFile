package com.wackycodes.mediaapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.wackycodes.mediaapp.databinding.ActivityMainBinding;
import com.wackycodes.mediafile.ActivityMyFile;
import com.wackycodes.mediafile.listener.OnGetImageListener;
import com.wackycodes.mediafile.model.ModelImage;

public class MainActivity extends ActivityMyFile {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        onUIAction();
    }

    private void setImage( ModelImage modelImage, String message){
        if (modelImage != null){
            try {
                if (modelImage.getImageUri() != null){
                    mainBinding.imageView.setImageURI( modelImage.getImageUri() );
                }else if (modelImage.getBitmap() != null){
                    mainBinding.imageView.setImageBitmap( modelImage.getBitmap() );
                }
            } catch (Exception e) {
                Log.e("ImageLoad", "Failed : " + e.getMessage());
            }
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void onUIAction( ){
        mainBinding.buttonGetImage.setOnClickListener(view -> {
            if (isStoragePermissionGranted()){
                queryToSelectImage(this::setImage);
            }else{
                requestForPermission(PERMISSION_CODE_STORAGE);
            }
        });
        mainBinding.buttonCaptureImage.setOnClickListener( view -> {
            if (isCameraPermissionGranted()){
                queryToCaptureImage(this::setImage);
            }else{
                requestForPermission(PERMISSION_CODE_CAMERA);
            }
        });

        mainBinding.buttonOpenFragment.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(mainBinding.frameLayout.getId(), new FragmentImageSelection());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        mainBinding.buttonOpenBottomSheet.setOnClickListener(view -> {

        });
    }


}