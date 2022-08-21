package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class d implements Runnable {
    public final /* synthetic */ Bundle a;
    public final /* synthetic */ NABaseMap b;

    public d(NABaseMap nABaseMap, Bundle bundle) {
        this.b = nABaseMap;
        this.a = bundle;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean b;
        ReadWriteLock readWriteLock;
        ReadWriteLock readWriteLock2;
        ReadWriteLock readWriteLock3;
        long j;
        b = this.b.b();
        if (!b) {
            return;
        }
        boolean z = false;
        try {
            readWriteLock3 = this.b.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                NABaseMap nABaseMap = this.b;
                j = nABaseMap.b;
                nABaseMap.nativeAddOneOverlayItem(j, this.a);
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
