package com.baidu.platform.comapi.map;

import com.baidu.platform.comjni.map.basemap.AppBaseMap;

/* loaded from: classes.dex */
class s implements Runnable {
    public final /* synthetic */ MapSurfaceView a;

    public s(MapSurfaceView mapSurfaceView) {
        this.a = mapSurfaceView;
    }

    @Override // java.lang.Runnable
    public void run() {
        AppBaseMap baseMap;
        boolean z;
        MapController mapController = this.a.a;
        if (mapController == null || (baseMap = mapController.getBaseMap()) == null) {
            return;
        }
        z = this.a.m;
        baseMap.ShowSatelliteMap(z);
    }
}
