package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import com.baidu.mapapi.map.BaiduMap;

/* loaded from: classes.dex */
class h implements com.baidu.platform.comapi.map.c {
    public final /* synthetic */ BaiduMap a;

    public h(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.platform.comapi.map.c
    public void a(Bitmap bitmap) {
        BaiduMap.SnapshotReadyCallback snapshotReadyCallback;
        snapshotReadyCallback = this.a.C;
        snapshotReadyCallback.onSnapshotReady(bitmap);
    }
}
