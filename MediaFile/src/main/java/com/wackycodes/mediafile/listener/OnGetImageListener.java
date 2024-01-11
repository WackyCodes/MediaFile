package com.wackycodes.mediafile.listener;

import androidx.annotation.Nullable;

import com.wackycodes.mediafile.model.ModelImage;

public interface OnGetImageListener {
    void onGetImage(@Nullable ModelImage modelImage, @Nullable String message);

}
