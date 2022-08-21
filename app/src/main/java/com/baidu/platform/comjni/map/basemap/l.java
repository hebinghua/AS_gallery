package com.baidu.platform.comjni.map.basemap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class l implements Runnable {
    public final /* synthetic */ long a;
    public final /* synthetic */ long b;
    public final /* synthetic */ NABaseMap c;

    public l(NABaseMap nABaseMap, long j, long j2) {
        this.c = nABaseMap;
        this.a = j;
        this.b = j2;
    }

    @Override // java.lang.Runnable
    public void run() {
        ReadWriteLock readWriteLock;
        ReadWriteLock readWriteLock2;
        ReadWriteLock readWriteLock3;
        boolean a;
        boolean a2;
        long j;
        boolean z = false;
        try {
            readWriteLock3 = this.c.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                a = this.c.a(this.a);
                if (!a) {
                    a2 = this.c.a(this.b);
                    if (!a2) {
                        NABaseMap nABaseMap = this.c;
                        j = nABaseMap.b;
                        nABaseMap.nativeSwitchLayer(j, this.a, this.b);
                    }
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
