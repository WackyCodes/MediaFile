package com.wackycodes.mediafile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.wackycodes.mediafile.listener.OnGetImageListener;
import com.wackycodes.mediafile.listener.OnGetVideoListener;
import com.wackycodes.mediafile.model.ModelImage;
import com.wackycodes.mediafile.model.ModelVideo;
import com.wackycodes.mediafile.permission.FragmentBottomSheetPermission;
import com.wackycodes.mediafile.utils.MyFileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FragmentBottomSheetMyFile extends FragmentBottomSheetPermission {

    public FragmentBottomSheetMyFile() {
    }

    public FragmentBottomSheetMyFile(Context context) {
        super(context);
    }

    private OnGetImageListener onGetImageListener;
    private OnGetVideoListener onGetVideoListener;

    public void queryToSelectImage(OnGetImageListener imageListener) {
        this.onGetImageListener = imageListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Launch the photo picker and let the user choose only images.
            pickMediaImage.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            // Ask Permission !!
            if (!isStoragePermissionGranted()) {
                imageListener.onGetImage( null, "Storage Permission Required!");
                showDebugLog("storage permission Required!!");
                return;
            }
            // Call The launcher to select Image
            getImageLauncher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"));
        }
    }
    public void queryToCaptureImage(OnGetImageListener imageListener) {
        this.onGetImageListener = imageListener;
        if (!isCameraPermissionGranted()){
            imageListener.onGetImage( null, "Camera Permission Required!");
            showDebugLog("Camera permission Required!!");
            return;
        }
        try {
            // on below line opening an intent to capture a video.
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImageLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showDebugLog("Exception : " + e.getMessage());
            imageListener.onGetImage(null, "Something went wrong!");
        }
    }
    private void sendImageResponse(ModelImage modelImage, String message){
        if (onGetImageListener != null){
            onGetImageListener.onGetImage( modelImage, message );
        }
    }


    //region>> Image Selection and captures --------------------------------------------------------------
    private final ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
                    // Received Image Data in the form of Intent
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        // Now You Can Use Uri According to You!!
                        sendImageResponse( getImageFile(uri, null), "Success" );
                    }
                } catch (Exception e) {
                    sendImageResponse( null, "Something went wrong!" );
                }
            });

    private final ActivityResultLauncher<Intent> captureImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
                    // Received Image Data in the form of Intent
                    if (result.getData() != null) {
                        Intent intent = result.getData();
                        // Now You Can Use Uri According to You!!
                        Bundle extras = intent.getExtras();
                        ModelImage modelImage = null;
                        if (extras != null){
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            modelImage = new ModelImage();
                            modelImage.setBitmap(imageBitmap);
                        }
                        sendImageResponse( getImageFile(null, modelImage), "Success" );
                    }
                } catch (Exception e) {
                    sendImageResponse( null, "Something went wrong!" );
                }
            });

    // Registers a photo picker activity launcher in single-select mode.
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    sendImageResponse( getImageFile(uri, null), "Success");
                }else{
                    sendImageResponse( null, "Something went wrong!" );
                }
            });

    // This method is used to set Image..!!
    private ModelImage getImageFile(Uri uri, ModelImage modelImage) {
        if (uri == null && modelImage == null) {
            return null;
        }
        if (modelImage == null) {
            modelImage = new ModelImage(uri);
        }

        String imageEncoded = "";
        Bitmap bitmap = null;
        try {
            if (uri == null && modelImage.getBitmap() != null) {
                uri = MyFileUtil.getImageUriFromBitmap(requireContext(), modelImage.getBitmap());
                bitmap = modelImage.getBitmap();
            }else if (uri != null){
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            }else{
                throw new IOException("Image bitmap not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (modelImage.getBitmap() != null)
                bitmap = modelImage.getBitmap();
        }
        if (bitmap != null) {
            imageEncoded = MyFileUtil.getBase64FromBitmap(bitmap);
            modelImage.setBase64Encoded(imageEncoded);
            if (modelImage.getBitmap() == null) {
                modelImage.setBitmap(bitmap);
            }
//            updateProfileBinding.imageView.setImageBitmap( bitmap );
        } else {
//            File file = new File( Objects.requireNonNull( MyFileUtil.getPathFromUri(getContext(), uri) ) );
//            final Uri imageUri = data.getData();
            try {
                InputStream imageStream = requireContext().getContentResolver().openInputStream(uri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageEncoded = MyFileUtil.getBase64FromBitmap(selectedImage);
                modelImage.setBase64Encoded(imageEncoded);
                modelImage.setBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return modelImage;
    }

    //endregion>> Image Selection and captures --------------------------------------------------------------

    public void queryToSelectVideo(OnGetVideoListener videoListener) {
        this.onGetVideoListener = videoListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Launch the photo picker and let the user choose only images.
            pickMediaVideo.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                    .build());
        } else {
            // Ask Permission !!
            if (!isStoragePermissionGranted()) {
                videoListener.onGetVideo(null, "Storage Permission Required!");
                return;
            }
            // Call The launcher to select Image
            getVideoLauncher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("video/*"));
        }
    }

    public void queryToRecordVideo(OnGetVideoListener videoListener) {
        this.onGetVideoListener = videoListener;
        try {
            if (!isCameraPermissionGranted()){
                videoListener.onGetVideo(null, "Camera permission required!");
                return;
            }
            // on below line opening an intent to capture a video.
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            getVideoLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showDebugLog("queryToRecordVideo : " + e.getMessage());
            sendVideoResponse(null, "Something went wrong!");
        }
    }
    private void sendVideoResponse(ModelVideo modelVideo, String message){
        if (onGetVideoListener != null){
            onGetVideoListener.onGetVideo( modelVideo, message );
        }
    }

    //region>> Video Selection and captures --------------------------------------------------------------

    private final ActivityResultLauncher<Intent> getVideoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                try {
                    // Received Image Data in the form of Intent
                    if (result.getData() != null) {
                        Uri uri = result.getData().getData();
                        sendVideoResponse(getVideoFile(uri), "Success");
                    } else {
                        showDebugLog("getVideoLauncher : No Data found!");
                        sendVideoResponse(null, "No Data found!");
                    }
                } catch (Exception e) {
                    showDebugLog("getVideoLauncher : Error " + e.getMessage());
                    sendVideoResponse(null, "Something went wrong!");
                }
            });

    // Registers a photo picker activity launcher in single-select mode.
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaVideo =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    sendVideoResponse(null, "No Data found!");
                } else {
                    sendVideoResponse(null, "no video found!");
                }
            });


    private ModelVideo getVideoFile(Uri uri) {
        if (uri == null) {
            showDebugLog("getVideoFile : Uri is null!");
            return null;
        }
        // Now You Can Use Uri According to You!!
        ModelVideo modelVideo = new ModelVideo(uri);
        // OI FILE Manager
        modelVideo.setVideoFullPath(uri.getPath());
        // MEDIA GALLERY
        String filePath = getPath(uri);
        if (filePath != null) {
            modelVideo.setVideoFullPath(filePath);
        }
        return modelVideo;
    }

    //endregion>> Video Selection and captures --------------------------------------------------------------

    public String getPath(Uri uri) {
        String filePath = null;
        try {
            if (uri == null) {
                return null;
            }
            String[] projection = {MediaStore.Video.Media.DATA};
            @SuppressLint("Recycle")
            Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                filePath = cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

}
