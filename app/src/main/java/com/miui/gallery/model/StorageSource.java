package com.miui.gallery.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.loader.BaseLoader;
import com.miui.gallery.loader.StorageSetLoader;

/* loaded from: classes2.dex */
public class StorageSource extends PhotoLoaderSource {
    @Override // com.miui.gallery.model.PhotoLoaderSource
    public boolean match(Uri uri, Bundle bundle) {
        return isFileScheme(uri);
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle) {
        return new StorageSetLoader(context, uri, bundle);
    }

    public static boolean isFileScheme(Uri uri) {
        return uri != null && Action.FILE_ATTRIBUTE.equals(uri.getScheme());
    }
}
