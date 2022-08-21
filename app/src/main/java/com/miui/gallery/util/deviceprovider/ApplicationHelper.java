package com.miui.gallery.util.deviceprovider;

/* loaded from: classes2.dex */
public abstract class ApplicationHelper {
    public static ApplicationHelper sApplicationHelper;

    public static boolean isAutoUploadFeatureOpen() {
        return true;
    }

    public static boolean isBabyAlbumFeatureOpen() {
        return true;
    }

    public static boolean isCloudTrashBinFeatureOpen() {
        return true;
    }

    public static boolean isFaceAlbumFeatureOpen() {
        return true;
    }

    public abstract BitmapProviderInterface getBitmapProviderInternal();

    public abstract IntentProviderInterface getIntentProviderInternal();

    public abstract MiCloudProviderInterface getMiCloudProviderInternal();

    public abstract boolean isSecretAlbumFeatureOpenInternal();

    public abstract boolean supportShareInternal();

    public abstract boolean supportStoryAlbumInternal();

    public static ApplicationHelper getInstance() {
        if (sApplicationHelper == null) {
            sApplicationHelper = newInstance();
        }
        return sApplicationHelper;
    }

    public static ApplicationHelper newInstance() {
        return new BigApplicationHelper();
    }

    public static boolean isSecretAlbumFeatureOpen() {
        return getInstance().isSecretAlbumFeatureOpenInternal();
    }

    public static boolean isStoryAlbumFeatureOpen() {
        return getInstance().supportStoryAlbumInternal();
    }

    public static IntentProviderInterface getIntentProvider() {
        return getInstance().getIntentProviderInternal();
    }

    public static MiCloudProviderInterface getMiCloudProvider() {
        return getInstance().getMiCloudProviderInternal();
    }

    public static BitmapProviderInterface getBitmapProvider() {
        return getInstance().getBitmapProviderInternal();
    }

    public static boolean supportShare() {
        return getInstance().supportShareInternal();
    }
}
