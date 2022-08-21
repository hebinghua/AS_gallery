package com.miui.gallery.assistant.manager;

import com.miui.gallery.util.ReusedBitmapCache;

/* loaded from: classes.dex */
public class ImageFeatureReusedBitCache extends ReusedBitmapCache {
    @Override // com.miui.gallery.util.ReusedBitmapCache
    public int getMaxCount() {
        return 3;
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean needMutable() {
        return false;
    }

    @Override // com.miui.gallery.util.ReusedBitmapCache
    public boolean needRecycle() {
        return false;
    }
}
