package com.miui.gallery.glide.load.data;

/* loaded from: classes2.dex */
public class BoundCover {
    public final byte[] cover;

    public BoundCover(byte[] bArr) {
        this.cover = bArr;
    }

    public byte[] getData() {
        return this.cover;
    }

    public static BoundCover obtain(byte[] bArr) {
        return new BoundCover(bArr);
    }
}
