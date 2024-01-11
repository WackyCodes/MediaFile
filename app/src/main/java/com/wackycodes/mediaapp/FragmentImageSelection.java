package com.wackycodes.mediaapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wackycodes.mediaapp.databinding.FragmentImageSelectionBinding;
import com.wackycodes.mediafile.FragmentMyFile;
import com.wackycodes.mediafile.model.ModelImage;

public class FragmentImageSelection extends FragmentMyFile {
    public FragmentImageSelection() {
        // Required empty public constructor
    }

    private FragmentImageSelectionBinding selectionBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        selectionBinding = FragmentImageSelectionBinding.inflate( inflater, container, false );

        onUIAction();

        return selectionBinding.getRoot();
    }

    private void setImage(ModelImage modelImage, String message){
        if (modelImage != null){
            try {
                if (modelImage.getImageUri() != null){
                    selectionBinding.imageView.setImageURI( modelImage.getImageUri() );
                }else if (modelImage.getBitmap() != null){
                    selectionBinding.imageView.setImageBitmap( modelImage.getBitmap() );
                }
            } catch (Exception e) {
                Log.e("ImageLoad", "Failed : " + e.getMessage());
            }
        }else{
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    private void onUIAction( ){
        selectionBinding.buttonGetImage.setOnClickListener(view -> {
            if (isStoragePermissionGranted()){
                queryToSelectImage(this::setImage);
            }else{
                requestForPermission(PERMISSION_CODE_STORAGE);
            }
        });
        selectionBinding.buttonCaptureImage.setOnClickListener( view -> {
            if (isCameraPermissionGranted()){
                queryToCaptureImage(this::setImage);
            }else{
                requestForPermission(PERMISSION_CODE_CAMERA);
            }
        });

        selectionBinding.backBtn.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

}