package com.baidu.mapapi.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class g implements com.baidu.mapsdkplatform.comapi.map.aa {
    public final /* synthetic */ BaiduMap a;

    public g(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.mapsdkplatform.comapi.map.aa
    public Bundle a(int i, int i2, int i3, Context context) {
        Lock lock;
        Lock lock2;
        TileOverlay tileOverlay;
        Lock lock3;
        TileOverlay tileOverlay2;
        lock = this.a.K;
        lock.lock();
        try {
            tileOverlay = this.a.H;
            if (tileOverlay != null) {
                tileOverlay2 = this.a.H;
                Tile a = tileOverlay2.a(i, i2, i3);
                StringBuilder sb = new StringBuilder();
                sb.append("mapLayerDataReq tile t == null = ");
                sb.append(a == null);
                Log.e("SDKTileLayer", sb.toString());
                if (a != null) {
                    return a.toBundle();
                }
            }
            lock3 = this.a.K;
            lock3.unlock();
            return null;
        } finally {
            lock2 = this.a.K;
            lock2.unlock();
        }
    }
}
