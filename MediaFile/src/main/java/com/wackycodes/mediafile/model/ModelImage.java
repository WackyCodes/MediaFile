package com.wackycodes.mediafile.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ModelImage implements Parcelable {


    public ModelImage() {
    }

    public ModelImage(String imageFullPath, boolean isInternetImage) {
        this.imageFullPath = imageFullPath;
        this.isInternetImage = isInternetImage;
    }
    public ModelImage(String imageFullPath, String altText) {
        this.imageFullPath = imageFullPath;
        this.altText = altText;
    }

    public ModelImage(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public ModelImage(Uri imageUri, String base64Encoded) {
        this.imageUri = imageUri;
        this.base64Encoded = base64Encoded;
    }

    public ModelImage(Uri imageUri, String base64Encoded, String imageFullPath) {
        this.imageUri = imageUri;
        this.base64Encoded = base64Encoded;
        this.imageFullPath = imageFullPath;
    }

    private boolean isInternetImage = false;
    private Uri imageUri;
    private String base64Encoded;
    private String imageFullPath;

    private Bitmap bitmap;
    private String altText;

    protected ModelImage(Parcel in) {
        isInternetImage = in.readByte() != 0;
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        base64Encoded = in.readString();
        imageFullPath = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        altText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isInternetImage ? 1 : 0));
        dest.writeParcelable(imageUri, flags);
        dest.writeString(base64Encoded);
        dest.writeString(imageFullPath);
        dest.writeParcelable(bitmap, flags);
        dest.writeString(altText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelImage> CREATOR = new Creator<ModelImage>() {
        @Override
        public ModelImage createFromParcel(Parcel in) {
            return new ModelImage(in);
        }

        @Override
        public ModelImage[] newArray(int size) {
            return new ModelImage[size];
        }
    };

    public boolean isInternetImage() {
        return isInternetImage;
    }

    public void setInternetImage(boolean internetImage) {
        isInternetImage = internetImage;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getBase64Encoded() {
        return base64Encoded;
    }

    public void setBase64Encoded(String base64Encoded) {
        this.base64Encoded = base64Encoded;
    }

    public String getImageFullPath() {
        return imageFullPath;
    }

    public void setImageFullPath(String imageFullPath) {
        this.imageFullPath = imageFullPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
