package com.wackycodes.mediafile.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ModelVideo implements Parcelable {

    public ModelVideo() {
    }

    public ModelVideo(Uri videoUri) {
        this.videoUri = videoUri;
    }

    private Uri videoUri;
    private String base64Encoded;
    private String videoFullPath;
    private String videoName;
    private int duration;
    private int videoSize;

    protected ModelVideo(Parcel in) {
        videoUri = in.readParcelable(Uri.class.getClassLoader());
        base64Encoded = in.readString();
        videoFullPath = in.readString();
        videoName = in.readString();
        duration = in.readInt();
        videoSize = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(videoUri, flags);
        dest.writeString(base64Encoded);
        dest.writeString(videoFullPath);
        dest.writeString(videoName);
        dest.writeInt(duration);
        dest.writeInt(videoSize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelVideo> CREATOR = new Creator<ModelVideo>() {
        @Override
        public ModelVideo createFromParcel(Parcel in) {
            return new ModelVideo(in);
        }

        @Override
        public ModelVideo[] newArray(int size) {
            return new ModelVideo[size];
        }
    };

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public String getBase64Encoded() {
        return base64Encoded;
    }

    public void setBase64Encoded(String base64Encoded) {
        this.base64Encoded = base64Encoded;
    }

    public String getVideoFullPath() {
        return videoFullPath;
    }

    public void setVideoFullPath(String videoFullPath) {
        this.videoFullPath = videoFullPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }
}
