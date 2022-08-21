package com.miui.gallery.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FilterInputStream;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class RecyclableBufferedInputStream extends com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream implements InputStreamWrapper {
    public RecyclableBufferedInputStream(InputStream inputStream, ArrayPool arrayPool) {
        super(inputStream, arrayPool);
    }

    @Override // com.miui.gallery.glide.load.resource.bitmap.InputStreamWrapper
    public InputStream getWrapped() {
        return ((FilterInputStream) this).in;
    }
}
