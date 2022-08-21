package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class o implements Runnable {
    public final /* synthetic */ long a;
    public final /* synthetic */ long b;
    public final /* synthetic */ boolean c;
    public final /* synthetic */ Bundle d;
    public final /* synthetic */ NABaseMap e;

    public o(NABaseMap nABaseMap, long j, long j2, boolean z, Bundle bundle) {
        this.e = nABaseMap;
        this.a = j;
        this.b = j2;
        this.c = z;
        this.d = bundle;
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
            readWriteLock3 = this.e.d;
            z = readWriteLock3.readLock().tryLock(2000L, TimeUnit.MILLISECONDS);
            if (z) {
                a = this.e.a(this.a);
                if (!a) {
                    NABaseMap nABaseMap = this.e;
                    j = nABaseMap.b;
                    nABaseMap.nativeSetFocus(j, this.a, this.b, this.c, this.d);
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
                readWriteLock = this.e.d;
                readWriteLock.readLock().unlock();
            }
            throw th;
        }
        readWriteLock2 = this.e.d;
        readWriteLock2.readLock().unlock();
    }
}
