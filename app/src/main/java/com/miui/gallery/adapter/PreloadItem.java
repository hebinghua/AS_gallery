package com.miui.gallery.adapter;

import android.graphics.RectF;

/* loaded from: classes.dex */
public class PreloadItem {
    public final long fileLength;
    public final String path;
    public final RectF region;
    public final byte[] secretKey;

    public PreloadItem(String str, long j, RectF rectF, byte[] bArr) {
        this.path = str;
        this.fileLength = j;
        this.region = rectF;
        this.secretKey = bArr;
    }
}
