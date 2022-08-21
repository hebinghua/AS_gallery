package com.miui.gallery.ui.share;

import android.net.Uri;
import com.miui.gallery.ui.share.PrepareItem;

/* loaded from: classes2.dex */
public interface PrepareFunc<T extends PrepareItem> {
    Uri prepare(T t, PrepareProgressCallback<T> prepareProgressCallback);

    void release();
}
