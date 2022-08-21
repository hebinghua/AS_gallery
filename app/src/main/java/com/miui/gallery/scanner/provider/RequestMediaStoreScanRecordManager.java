package com.miui.gallery.scanner.provider;

import android.os.Build;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class RequestMediaStoreScanRecordManager {
    public static final long DEFAULT_EFFECTIVE_DURATION;
    public Map<String, EffectiveTime> mRecordMap;

    static {
        DEFAULT_EFFECTIVE_DURATION = Build.VERSION.SDK_INT <= 29 ? 3000L : AbstractComponentTracker.LINGERING_TIMEOUT;
    }

    /* loaded from: classes2.dex */
    public static final class Singleton {
        public static RequestMediaStoreScanRecordManager sInstance = new RequestMediaStoreScanRecordManager();
    }

    public static RequestMediaStoreScanRecordManager getInstance() {
        return Singleton.sInstance;
    }

    public RequestMediaStoreScanRecordManager() {
        this.mRecordMap = new ConcurrentHashMap();
    }

    public void record(String str) {
        record(str, DEFAULT_EFFECTIVE_DURATION);
    }

    public void record(String str, long j) {
        clean();
        this.mRecordMap.put(str, new EffectiveTime(System.currentTimeMillis(), j));
    }

    public boolean exists(String str) {
        clean();
        return this.mRecordMap.get(str) != null;
    }

    public final void clean() {
        Iterator<Map.Entry<String, EffectiveTime>> it = this.mRecordMap.entrySet().iterator();
        while (it.hasNext()) {
            if (!it.next().getValue().isValid()) {
                it.remove();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class EffectiveTime {
        public long createTime;
        public long duration;

        public EffectiveTime(long j, long j2) {
            this.createTime = j;
            this.duration = j2;
        }

        public boolean isValid() {
            return (System.currentTimeMillis() - this.createTime) - this.duration < 0;
        }
    }
}
