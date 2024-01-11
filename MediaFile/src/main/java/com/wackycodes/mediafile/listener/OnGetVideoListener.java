package com.wackycodes.mediafile.listener;

import androidx.annotation.Nullable;

import com.wackycodes.mediafile.model.ModelVideo;

public interface OnGetVideoListener {
    void onGetVideo(@Nullable ModelVideo modelVideo, @Nullable String message);
}
