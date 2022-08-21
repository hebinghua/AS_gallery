package com.baidu.platform.comapi.map;

/* loaded from: classes.dex */
class p implements Runnable {
    public final /* synthetic */ MapSurfaceView a;
    public final /* synthetic */ o b;

    public p(o oVar, MapSurfaceView mapSurfaceView) {
        this.b = oVar;
        this.a = mapSurfaceView;
    }

    @Override // java.lang.Runnable
    public void run() {
        MapSurfaceView mapSurfaceView = this.a;
        if (mapSurfaceView != null) {
            mapSurfaceView.setBackgroundResource(0);
        }
    }
}
