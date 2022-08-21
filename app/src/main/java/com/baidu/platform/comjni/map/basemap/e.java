package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class e implements Runnable {
    public final /* synthetic */ Bundle[] a;
    public final /* synthetic */ int b;
    public final /* synthetic */ NABaseMap c;

    public e(NABaseMap nABaseMap, Bundle[] bundleArr, int i) {
        this.c = nABaseMap;
        this.a = bundleArr;
        this.b = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean b;
        ReadWriteLock readWriteLock;
        ReadWriteLock readWriteLock2;
        ReadWriteLock readWriteLock3;
        long j;
        b = this.c.b();
        if (!b) {
            return;
        }
        boolean z = false;
        try {
            readWriteLock3 = this.c.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                NABaseMap nABaseMap = this.c;
                j = nABaseMap.b;
                nABaseMap.nativeAddOverlayItems(j, this.a, this.b);
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
