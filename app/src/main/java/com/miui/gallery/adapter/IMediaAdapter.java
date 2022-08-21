package com.miui.gallery.adapter;

import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.util.IncompatibleMediaType;

/* loaded from: classes.dex */
public interface IMediaAdapter {
    long getCreateTime(int i);

    default Uri getDownloadUri(int i) {
        return null;
    }

    default long getFileLength(int i) {
        return 0L;
    }

    default int getImageHeight(int i) {
        return 0;
    }

    default int getImageWidth(int i) {
        return 0;
    }

    int getItemCount();

    default RectF getItemDecodeRectF(int i) {
        return null;
    }

    long getItemKey(int i);

    default byte[] getItemSecretKey(int i) {
        return null;
    }

    String getLocation(int i);

    String getMicroThumbFilePath(int i);

    String getMimeType(int i);

    String getOptimalThumbFilePath(int i);

    default String getOriginFilePath(int i) {
        return null;
    }

    String getSha1(int i);

    default String getThumbFilePath(int i) {
        return null;
    }

    default boolean supportFoldBurstItems() {
        return false;
    }

    void updateGalleryCloudSyncableState();

    default String getBestQualityPath(int i) {
        String originFilePath = getOriginFilePath(i);
        if (!TextUtils.isEmpty(originFilePath)) {
            return originFilePath;
        }
        String thumbFilePath = getThumbFilePath(i);
        if (!TextUtils.isEmpty(thumbFilePath)) {
            return thumbFilePath;
        }
        String microThumbFilePath = getMicroThumbFilePath(i);
        if (TextUtils.isEmpty(microThumbFilePath)) {
            return null;
        }
        return microThumbFilePath;
    }

    default String getBindImagePath(int i) {
        if (IncompatibleMediaType.isUnsupportedMediaType(getMimeType(i)) && getDownloadUri(i) != null) {
            return getMicroThumbFilePath(i);
        }
        return getOptimalThumbFilePath(i);
    }
}
