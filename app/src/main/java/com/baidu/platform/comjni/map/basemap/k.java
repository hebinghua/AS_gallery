package com.baidu.platform.comjni.map.basemap;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class k implements Runnable {
    public final /* synthetic */ long a;
    public final /* synthetic */ NABaseMap b;

    public k(NABaseMap nABaseMap, long j) {
        this.b = nABaseMap;
        this.a = j;
    }

    @Override // java.lang.Runnable
    public void run() {
        ReadWriteLock readWriteLock;
        ReadWriteLock readWriteLock2;
        ReadWriteLock readWriteLock3;
        Set set;
        long j;
        boolean z = false;
        try {
            readWriteLock3 = this.b.d;
            z = readWriteLock3.writeLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                set = this.b.e;
                set.add(Long.valueOf(this.a));
                NABaseMap nABaseMap = this.b;
                j = nABaseMap.b;
                nABaseMap.nativeRemoveLayer(j, this.a);
            }
            if (!z) {
                return;
            }
        } catch (Exception unused) {
            if (!z) {
                return;
            }
        } catch (Throwable th) {
            if (z) {
                readWriteLock = this.b.d;
                readWriteLock.writeLock().unlock();
            }
            throw th;
        }
        readWriteLock2 = this.b.d;
        readWriteLock2.writeLock().unlock();
    }
}
