package com.miui.gallery.util;

import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class CounterUtil {
    public final String mTag;
    public long mTs;

    public CounterUtil(String str) {
        this.mTag = str;
        reset();
    }

    public void reset() {
        this.mTs = System.currentTimeMillis();
    }

    public void tick(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        DefaultLogger.d(this.mTag, "%s cost time:%d", str, Long.valueOf(currentTimeMillis - this.mTs));
        this.mTs = currentTimeMillis;
    }
}
