package com.miui.gallery.sdk.download.adapter;

import android.net.Uri;
import com.miui.gallery.data.DBImage;

/* loaded from: classes2.dex */
public interface IUriAdapter {
    public static final IUriAdapter DEFAULT = new BaseUriAdapter();

    DBImage getDBItemForUri(Uri uri);
}
