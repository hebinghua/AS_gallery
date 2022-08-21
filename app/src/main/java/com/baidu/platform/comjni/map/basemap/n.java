package com.baidu.platform.comjni.map.basemap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class n implements Runnable {
    public final /* synthetic */ long a;
    public final /* synthetic */ NABaseMap b;

    public n(NABaseMap nABaseMap, long j) {
        this.b = nABaseMap;
        this.a = j;
    }

    @Override // java.lang.Runnable
    public void run() {
        ReadWriteLock readWriteLock;
        ReadWriteLock readWriteLock2;
        ReadWriteLock readWriteLock3;
        boolean a;
        long j;
        boolean z = false;
        try {
            readWriteLock3 = this.b.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                a = this.b.a(this.a);
                if (!a) {
                    NABaseMap nABaseMap = this.b;
                    j = nABaseMap.b;
                    nABaseMap.nativeClearLayer(j, this.a);
                }
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
                readWriteLock.readLock().unlock();
            }
            throw th;
        }
        readWriteLock2 = this.b.d;
        readWriteLock2.readLock().unlock();
    }
}
