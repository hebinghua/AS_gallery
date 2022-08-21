package com.miui.gallery.sdk.uploadstatus;

import android.net.Uri;
import com.miui.gallery.cloud.GalleryCloudUtils;

/* loaded from: classes2.dex */
public abstract class UriAdapter {
    public abstract String getLocalId(Uri uri);

    public abstract Uri getUserUri(ItemType itemType, String str);

    public ItemType getItemTypeBySdkBaseUri(Uri uri) {
        if (GalleryCloudUtils.CLOUD_URI.equals(uri)) {
            return ItemType.OWNER;
        }
        if (GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI.equals(uri)) {
            return ItemType.OWNER_SUB_UBI;
        }
        if (GalleryCloudUtils.SHARE_IMAGE_URI.equals(uri)) {
            return ItemType.SHARER;
        }
        if (GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI.equals(uri)) {
            return ItemType.SHARER_SUB_UBI;
        }
        throw new IllegalArgumentException("not supported uri: " + uri);
    }
}
