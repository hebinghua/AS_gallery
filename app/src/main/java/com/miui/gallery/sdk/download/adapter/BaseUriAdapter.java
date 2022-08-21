package com.miui.gallery.sdk.download.adapter;

import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.util.SafeDBUtil;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class BaseUriAdapter implements IUriAdapter {
    public static final UriMatcher sUriMatcher;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "gallery_cloud/#", 0);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "share_image/#", 2);
    }

    @Override // com.miui.gallery.sdk.download.adapter.IUriAdapter
    public DBImage getDBItemForUri(Uri uri) {
        final Uri translate2ImageUri = translate2ImageUri(uri);
        if (translate2ImageUri != null) {
            return (DBImage) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), translate2ImageUri, new String[]{Marker.ANY_MARKER}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<DBImage>() { // from class: com.miui.gallery.sdk.download.adapter.BaseUriAdapter.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public DBImage mo1808handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToNext()) {
                        return null;
                    }
                    return CloudUtils.createDBImageByUri(BaseUriAdapter.this.getBaseUri(translate2ImageUri), cursor);
                }
            });
        }
        return null;
    }

    public final Uri translate2ImageUri(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match == 0 || match == 2) {
            return uri;
        }
        return null;
    }

    public final Uri getBaseUri(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match != 0) {
            if (match == 2) {
                return GalleryCloudUtils.SHARE_IMAGE_URI;
            }
            return null;
        }
        return GalleryCloudUtils.CLOUD_URI;
    }
}
