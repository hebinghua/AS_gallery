package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class p implements Runnable {
    public final /* synthetic */ Bundle a;
    public final /* synthetic */ boolean b;
    public final /* synthetic */ NABaseMap c;

    public p(NABaseMap nABaseMap, Bundle bundle, boolean z) {
        this.c = nABaseMap;
        this.a = bundle;
        this.b = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        ReadWriteLock readWriteLock;
        ReadWriteLock readWriteLock2;
        ReadWriteLock readWriteLock3;
        long j;
        boolean a;
        ReadWriteLock readWriteLock4;
        boolean z = false;
        try {
            readWriteLock3 = this.c.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                Bundle bundle = this.a;
                if (bundle != null) {
                    a = this.c.a(bundle.getLong("itemaddr", 0L));
                    if (a) {
                        if (!z) {
                            return;
                        }
                        readWriteLock4 = this.c.d;
                        readWriteLock4.readLock().unlock();
                        return;
                    }
                }
                NABaseMap nABaseMap = this.c;
                j = nABaseMap.b;
                nABaseMap.nativeAddItemData(j, this.a, this.b);
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
