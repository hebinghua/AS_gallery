package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class c implements Runnable {
    public final /* synthetic */ Bundle a;
    public final /* synthetic */ NABaseMap b;

    public c(NABaseMap nABaseMap, Bundle bundle) {
        this.b = nABaseMap;
        this.a = bundle;
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
            readWriteLock3 = this.b.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                Bundle bundle = this.a;
                if (bundle != null) {
                    a = this.b.a(bundle.getLong("itemaddr", 0L));
                    if (a) {
                        if (!z) {
                            return;
                        }
                        readWriteLock4 = this.b.d;
                        readWriteLock4.readLock().unlock();
                        return;
                    }
                }
                NABaseMap nABaseMap = this.b;
                j = nABaseMap.b;
                nABaseMap.nativeRemoveItemData(j, this.a);
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
