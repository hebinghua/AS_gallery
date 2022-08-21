package com.miui.gallery.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.loader.BaseLoader;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class ContentProxySource extends PhotoLoaderSource {
    @Override // com.miui.gallery.model.PhotoLoaderSource
    public boolean match(Uri uri, Bundle bundle) {
        return uri != null && MiStat.Param.CONTENT.equals(uri.getScheme());
    }

    @Override // com.miui.gallery.model.PhotoLoaderSource
    public BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle) {
        return new ContentProxyLoader(context, uri, bundle);
    }
}
