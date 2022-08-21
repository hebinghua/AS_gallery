package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class DataLoadStrategy extends BaseStrategy {
    @SerializedName("camera_preview_image_count")
    private int mCameraPreviewCount;

    public static DataLoadStrategy createDefault() {
        DataLoadStrategy dataLoadStrategy = new DataLoadStrategy();
        dataLoadStrategy.mCameraPreviewCount = 1000;
        return dataLoadStrategy;
    }

    public int getCameraPreviewCount() {
        return this.mCameraPreviewCount;
    }
}
