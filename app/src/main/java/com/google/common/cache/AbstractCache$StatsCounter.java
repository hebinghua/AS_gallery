package com.google.common.cache;

/* loaded from: classes.dex */
public interface AbstractCache$StatsCounter {
    void recordEviction();

    void recordHits(int i);

    void recordLoadException(long j);

    void recordLoadSuccess(long j);

    void recordMisses(int i);
}
