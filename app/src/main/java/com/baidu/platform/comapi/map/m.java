package com.baidu.platform.comapi.map;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapController;

/* loaded from: classes.dex */
class m implements Runnable {
    public final /* synthetic */ MapController.b a;

    public m(MapController.b bVar) {
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        GeoPoint fromPixels = (MapController.this.getMapView() == null || MapController.this.getMapView().getProjection() == null) ? null : MapController.this.h.get().getProjection().fromPixels(MapController.this.getScreenWidth() / 2, MapController.this.getScreenHeight() / 2);
        if (fromPixels != null) {
            MapController.CleanAfterDBClick(MapController.this.s, (float) fromPixels.getLongitudeE6(), (float) fromPixels.getLatitudeE6());
        }
        MapController.this.N = false;
    }
}
