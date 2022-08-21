package com.baidu.platform.comapi.map;

import com.baidu.platform.comjni.map.basemap.AppBaseMap;

/* loaded from: classes.dex */
class w implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ MapSurfaceView b;

    public w(MapSurfaceView mapSurfaceView, boolean z) {
        this.b = mapSurfaceView;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        AppBaseMap baseMap;
        MapController mapController = this.b.a;
        if (mapController == null || (baseMap = mapController.getBaseMap()) == null) {
            return;
        }
        baseMap.ShowBaseIndoorMap(this.a);
    }
}
