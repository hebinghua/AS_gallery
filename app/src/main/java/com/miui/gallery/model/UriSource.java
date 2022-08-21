package com.miui.gallery.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.UriSetLoader;

/* loaded from: classes2.dex */
public class UriSource extends PhotoLoaderSource {
    @Override // com.miui.gallery.model.PhotoLoaderSource
    public boolean match(Uri uri, Bundle bundle) {
        String string = bundle != null ? bundle.getString("mime_type") : null;
        if (string == null) {
            string = GalleryApp.sGetAndroidContext().getContentResolver().getType(uri);
        }
        return string == null || string.startsWith("image/");
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle) {
        return new UriSetLoader(context, uri, bundle);
    }
}
