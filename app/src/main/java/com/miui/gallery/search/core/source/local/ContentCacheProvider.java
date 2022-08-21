package com.miui.gallery.search.core.source.local;

import android.database.ContentObserver;
import android.net.Uri;

/* loaded from: classes2.dex */
public interface ContentCacheProvider<T> {
    Uri getContentUri();

    /* renamed from: loadContent */
    T mo1330loadContent();

    void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver);

    void unregisterContentObserver(ContentObserver contentObserver);
}
