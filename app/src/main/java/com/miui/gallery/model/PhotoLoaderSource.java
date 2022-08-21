package com.miui.gallery.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.miui.gallery.loader.BaseLoader;

/* loaded from: classes2.dex */
public abstract class PhotoLoaderSource {
    public abstract BaseLoader createDataLoader(Context context, Uri uri, Bundle bundle);

    public abstract boolean match(Uri uri, Bundle bundle);
}
