package com.miui.gallery.model;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.CloudSetLoader;
import com.miui.gallery.loader.StorageSetLoader;
import com.xiaomi.stat.MiStat;
import java.io.File;

/* loaded from: classes2.dex */
public class LocalSource extends PhotoLoaderSource {
    public static UriMatcher sUriMatcher;

    public LocalSource() {
        if (sUriMatcher == null) {
            UriMatcher uriMatcher = new UriMatcher(-1);
            sUriMatcher = uriMatcher;
            uriMatcher.addURI("com.miui.gallery.cloud.provider", "media", 0);
            sUriMatcher.addURI("com.miui.gallery.cloud.provider", "album_media", 0);
            sUriMatcher.addURI("com.miui.gallery.cloud.provider", "share_album_media/#", 0);
            sUriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud_and_share", 0);
            sUriMatcher.addURI("com.miui.gallery.open", "raw/*", 1);
            sUriMatcher.addURI("com.miui.gallery.cloud.provider", "recent_media", 0);
        }
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public boolean match(Uri uri, Bundle bundle) {
        if (uri == null || !MiStat.Param.CONTENT.equals(uri.getScheme())) {
            return false;
        }
        return ("com.miui.gallery.cloud.provider".equals(uri.getAuthority()) || "com.miui.gallery.open".equals(uri.getAuthority())) && sUriMatcher.match(uri) != -1;
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle) {
        int match = sUriMatcher.match(uri);
        if (match != 0) {
            if (match == 1) {
                return new StorageSetLoader(context, Uri.fromFile(new File(uri.getLastPathSegment())), bundle);
            }
            return null;
        }
        return new CloudSetLoader(context, uri, bundle);
    }
}
