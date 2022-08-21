package com.miui.gallery.util.cloudimageloader;

import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.sdk.uploadstatus.ItemType;
import com.miui.gallery.sdk.uploadstatus.UriAdapter;

/* loaded from: classes2.dex */
public class CloudUriAdapter extends UriAdapter {
    public static final UriMatcher sUriMatcher;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "gallery_cloud/#", 0);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_owner_sububi/#", 1);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image/#", 2);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image_sububi/#", 3);
    }

    /* renamed from: com.miui.gallery.util.cloudimageloader.CloudUriAdapter$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$sdk$uploadstatus$ItemType;

        static {
            int[] iArr = new int[ItemType.values().length];
            $SwitchMap$com$miui$gallery$sdk$uploadstatus$ItemType = iArr;
            try {
                iArr[ItemType.OWNER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$uploadstatus$ItemType[ItemType.OWNER_SUB_UBI.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$uploadstatus$ItemType[ItemType.SHARER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$uploadstatus$ItemType[ItemType.SHARER_SUB_UBI.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    @Override // com.miui.gallery.sdk.uploadstatus.UriAdapter
    public Uri getUserUri(ItemType itemType, String str) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$sdk$uploadstatus$ItemType[itemType.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_SUBUBI, Long.valueOf(str).longValue());
            }
            if (i == 3) {
                return ContentUris.withAppendedId(GalleryContract.ShareImage.SHARE_URI, Long.valueOf(str).longValue());
            }
            if (i == 4) {
                return ContentUris.withAppendedId(GalleryContract.ShareImage.SHARE_URI_SUBUBI, Long.valueOf(str).longValue());
            }
            return null;
        }
        return ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, Long.valueOf(str).longValue());
    }

    @Override // com.miui.gallery.sdk.uploadstatus.UriAdapter
    public String getLocalId(Uri uri) {
        return uri.getLastPathSegment();
    }

    public static Uri getDownloadUri(long j) {
        if (ShareMediaManager.isOtherShareMediaId(j)) {
            return ContentUris.withAppendedId(GalleryContract.ShareImage.SHARE_URI, ShareMediaManager.getOriginalMediaId(j));
        }
        return ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, j);
    }
}
