package com.miui.gallery.model;

import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.CameraParallelProcessingSetLoader;
import com.miui.gallery.loader.LegacyMediaSetLoader;
import com.miui.gallery.loader.MediaStoreImageSetLoader;
import com.miui.gallery.loader.MediaStoreVideoSetLoader;
import com.miui.gallery.loader.MergedCursorSetLoaderWrapper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaSource extends PhotoLoaderSource {
    public static UriMatcher sUriMatcher;

    public MediaSource() {
        if (sUriMatcher == null) {
            UriMatcher uriMatcher = new UriMatcher(-1);
            sUriMatcher = uriMatcher;
            uriMatcher.addURI("media", "*/*/#", 1);
            sUriMatcher.addURI("media", "*/*/*/#", 1);
        }
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public boolean match(Uri uri, Bundle bundle) {
        return uri != null && MiStat.Param.CONTENT.equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle) {
        if (sUriMatcher.match(uri) == -1) {
            return null;
        }
        String checkArgumentVolumeName = StorageUtils.checkArgumentVolumeName(getVolumeNameFromUri(uri));
        if (TextUtils.isEmpty(checkArgumentVolumeName)) {
            return null;
        }
        boolean equals = "internal".equals(checkArgumentVolumeName);
        if (Build.VERSION.SDK_INT >= 29) {
            return new MergedCursorSetLoaderWrapper(context, new MediaStoreImageSetLoader(context, uri, bundle, equals), new CameraParallelProcessingSetLoader(context, uri, bundle, equals), new MediaStoreVideoSetLoader(context, uri, bundle, equals));
        }
        return new LegacyMediaSetLoader(context, uri, bundle, equals);
    }

    public final String getVolumeNameFromUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments != null && pathSegments.size() > 0) {
            return pathSegments.get(0);
        }
        DefaultLogger.e("MediaSource", "illegal uri : wrong path");
        return null;
    }
}
