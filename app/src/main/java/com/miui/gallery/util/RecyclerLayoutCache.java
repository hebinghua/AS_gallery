package com.miui.gallery.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class RecyclerLayoutCache {
    public LinkedList<View> mCache = new LinkedList<>();
    public final int mCacheSize;

    public RecyclerLayoutCache(int i) {
        this.mCacheSize = i;
    }

    public View get() {
        return this.mCache.poll();
    }

    public void put(View view) {
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
        if (this.mCache.size() >= this.mCacheSize) {
            return;
        }
        this.mCache.push(view);
    }
}
