package com.baidu.platform.comapi.map;

import com.baidu.platform.comjni.map.basemap.AppBaseMap;

/* loaded from: classes.dex */
class v implements Runnable {
    public final /* synthetic */ MapSurfaceView a;

    public v(MapSurfaceView mapSurfaceView) {
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
        z = this.a.n;
        baseMap.ShowStreetRoadMap(z);
    }
}
