package com.miui.gallery.model;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.FaceCloudSetLoader;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class FaceSource extends PhotoLoaderSource {
    public static UriMatcher sUriMatcher;

    public FaceSource() {
        if (sUriMatcher == null) {
            UriMatcher uriMatcher = new UriMatcher(-1);
            sUriMatcher = uriMatcher;
            uriMatcher.addURI("com.miui.gallery.cloud.provider", "person", 0);
            sUriMatcher.addURI("com.miui.gallery.cloud.provider", "person_recommend", 1);
        }
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public boolean match(Uri uri, Bundle bundle) {
        return uri != null && MiStat.Param.CONTENT.equals(uri.getScheme()) && "com.miui.gallery.cloud.provider".equals(uri.getAuthority()) && sUriMatcher.match(uri) != -1;
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle) {
        int match = sUriMatcher.match(uri);
        if (match != 0) {
            if (match == 1) {
                return new FaceCloudSetLoader(context, uri, bundle);
            }
            return null;
        }
        return new FaceCloudSetLoader(context, uri, bundle);
    }
}
