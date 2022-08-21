package com.baidu.mapapi.map;

import android.os.Bundle;
import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f implements com.baidu.mapsdkplatform.comapi.map.l {
    public final /* synthetic */ BaiduMap a;

    public f(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.mapsdkplatform.comapi.map.l
    public Bundle a(int i, int i2, int i3) {
        Lock lock;
        Lock lock2;
        HeatMap heatMap;
        Lock lock3;
        HeatMap heatMap2;
        lock = this.a.J;
        lock.lock();
        try {
            heatMap = this.a.I;
            if (heatMap != null) {
                heatMap2 = this.a.I;
                Tile a = heatMap2.a(i, i2, i3);
                if (a != null) {
                    return a.toBundle();
                }
            }
            lock3 = this.a.J;
            lock3.unlock();
            return null;
        } finally {
            lock2 = this.a.J;
            lock2.unlock();
        }
    }
}
