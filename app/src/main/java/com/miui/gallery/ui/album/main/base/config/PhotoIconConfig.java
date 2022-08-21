package com.miui.gallery.ui.album.main.base.config;

/* loaded from: classes2.dex */
public class PhotoIconConfig {
    public BaseIconStyle mPhotoIconConfig;

    public static PhotoIconConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final PhotoIconConfig INSTANCE = new PhotoIconConfig();
    }

    public PhotoIconConfig() {
    }

    public static BaseIconStyle getPhotoIconConfig() {
        return getInstance().photoIconConfig();
    }

    public final BaseIconStyle photoIconConfig() {
        if (this.mPhotoIconConfig == null) {
            this.mPhotoIconConfig = new BaseIconStyle();
        }
        return this.mPhotoIconConfig;
    }
}
