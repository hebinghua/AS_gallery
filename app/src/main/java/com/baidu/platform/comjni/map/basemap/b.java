package com.baidu.platform.comjni.map.basemap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class b implements Runnable {
    public final /* synthetic */ long a;
    public final /* synthetic */ boolean b;
    public final /* synthetic */ NABaseMap c;

    public b(NABaseMap nABaseMap, long j, boolean z) {
        this.c = nABaseMap;
        this.a = j;
        this.b = z;
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
            readWriteLock3 = this.c.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                a = this.c.a(this.a);
                if (!a) {
                    NABaseMap nABaseMap = this.c;
                    j = nABaseMap.b;
                    nABaseMap.nativeShowLayers(j, this.a, this.b);
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
                readWriteLock = this.c.d;
                readWriteLock.readLock().unlock();
            }
            throw th;
        }
        readWriteLock2 = this.c.d;
        readWriteLock2.readLock().unlock();
    }
}
