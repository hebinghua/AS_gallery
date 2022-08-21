package com.miui.gallery.model.dto.utils;

import android.content.ContentValues;
import android.net.Uri;

/* loaded from: classes2.dex */
public interface Insertable {
    long insert(Uri uri, String str, String str2, ContentValues contentValues);
}
