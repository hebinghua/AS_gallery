package com.miui.gallery.model.datalayer.utils;

import android.database.ContentObserver;
import android.net.Uri;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.ui.album.NoRepeatContentObserver;

/* loaded from: classes2.dex */
public class ContentResolverUtils {
    public static void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
        if (uri == null) {
            return;
        }
        GalleryApp.sGetAndroidContext().getContentResolver().registerContentObserver(uri, z, contentObserver);
    }

    public static void unRegisterContentObserver(ContentObserver contentObserver) {
        if (contentObserver == null) {
            return;
        }
        GalleryApp.sGetAndroidContext().getContentResolver().unregisterContentObserver(contentObserver);
        if (!(contentObserver instanceof NoRepeatContentObserver)) {
            return;
        }
        ((NoRepeatContentObserver) contentObserver).dispose();
    }
}
