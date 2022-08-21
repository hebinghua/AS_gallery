package com.baidu.platform.comapi.map;

import android.graphics.Bitmap;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;
import java.lang.ref.WeakReference;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class o implements ap {
    public static boolean d = false;
    public int a;
    public int b;
    private aj g;
    private WeakReference<MapSurfaceView> h;
    private WeakReference<h> i;
    private c j;
    private int l;
    private int m;
    private int n;
    private int o;
    private Bitmap.Config p;
    private e q;
    private boolean r;
    private int s;
    private int t;
    private int u;
    private AppBaseMap e = null;
    private boolean f = false;
    private volatile boolean k = false;
    private long v = 0;
    private boolean w = false;
    private volatile boolean x = false;
    public int c = 0;

    public o(h hVar, aj ajVar) {
        this.i = new WeakReference<>(hVar);
        this.g = ajVar;
    }

    public o(WeakReference<MapSurfaceView> weakReference, aj ajVar) {
        this.g = ajVar;
        this.h = weakReference;
    }

    private void b(Object obj) {
        h hVar;
        int i;
        int i2;
        MapSurfaceView mapSurfaceView;
        int i3;
        int i4;
        if (this.j == null) {
            return;
        }
        WeakReference<MapSurfaceView> weakReference = this.h;
        if (weakReference != null && (mapSurfaceView = weakReference.get()) != null && (i3 = this.l) > 0 && (i4 = this.m) > 0) {
            com.baidu.platform.comapi.util.i.a(new q(this, mapSurfaceView.captureImageFromSurface(this.n, this.o, i3, i4, obj, this.p)), 0L);
        }
        WeakReference<h> weakReference2 = this.i;
        if (weakReference2 == null || (hVar = weakReference2.get()) == null || (i = this.l) <= 0 || (i2 = this.m) <= 0) {
            return;
        }
        com.baidu.platform.comapi.util.i.a(new r(this, hVar.captureImageFromSurface(this.n, this.o, i, i2, obj, this.p)), 0L);
    }

    private boolean c() {
        return this.e != null && this.f;
    }

    public void a() {
        this.x = false;
    }

    @Override // com.baidu.platform.comapi.map.ap
    public void a(int i, int i2) {
        AppBaseMap appBaseMap = this.e;
        if (appBaseMap != null) {
            appBaseMap.renderResize(i, i2);
        }
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b a = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
            a.a("BasicMap onSurfaceChanged width = " + i + "; height = " + i2);
        }
    }

    @Override // com.baidu.platform.comapi.map.ap
    public void a(SurfaceHolder surfaceHolder) {
        Surface surface = surfaceHolder != null ? surfaceHolder.getSurface() : null;
        AppBaseMap appBaseMap = this.e;
        if (appBaseMap != null) {
            appBaseMap.surfaceDestroyed(surface);
        }
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap onSurfaceDestroyed");
        }
    }

    @Override // com.baidu.platform.comapi.map.ap
    public void a(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.r = false;
        this.s = 0;
        this.u = 0;
        this.t = 0;
        if (!c()) {
            return;
        }
        Surface surface = null;
        if (surfaceHolder != null) {
            surface = surfaceHolder.getSurface();
        }
        this.e.renderInit(i, i2, surface, i3);
        if (!OpenLogUtil.isMapLogEnable()) {
            return;
        }
        com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap onSurfaceCreated ok");
    }

    public void a(c cVar, int i, int i2) {
        this.k = true;
        this.j = cVar;
        this.l = i;
        this.m = i2;
        this.p = null;
    }

    public void a(c cVar, int i, int i2, int i3, int i4, Bitmap.Config config) {
        this.k = true;
        this.j = cVar;
        this.n = i;
        this.o = i2;
        this.l = i3;
        this.m = i4;
        this.p = config;
    }

    public void a(c cVar, int i, int i2, Bitmap.Config config) {
        this.k = true;
        this.j = cVar;
        this.l = i;
        this.m = i2;
        this.p = config;
    }

    public void a(e eVar) {
        this.q = eVar;
    }

    public void a(AppBaseMap appBaseMap) {
        this.e = appBaseMap;
    }

    @Override // com.baidu.platform.comapi.map.ap
    public void a(Object obj) {
        e eVar;
        h hVar;
        MapSurfaceView mapSurfaceView;
        MapSurfaceView mapSurfaceView2;
        if (!c()) {
            return;
        }
        boolean z = true;
        if (!this.w) {
            this.w = true;
            WeakReference<MapSurfaceView> weakReference = this.h;
            if (weakReference != null && (mapSurfaceView2 = weakReference.get()) != null) {
                mapSurfaceView2.post(new p(this, mapSurfaceView2));
            }
        }
        if (d) {
            d = false;
        } else if (!this.x) {
            int Draw = this.e.Draw();
            WeakReference<MapSurfaceView> weakReference2 = this.h;
            if (weakReference2 != null && (mapSurfaceView = weakReference2.get()) != null) {
                if (Draw == 1) {
                    mapSurfaceView.requestRender();
                } else if (mapSurfaceView.getRenderMode() != 0) {
                    mapSurfaceView.setRenderMode(0);
                }
            }
            WeakReference<h> weakReference3 = this.i;
            if (weakReference3 != null && (hVar = weakReference3.get()) != null) {
                if (Draw == 1) {
                    hVar.requestRender();
                } else if (hVar.getRenderMode() != 0) {
                    hVar.setRenderMode(0);
                }
            }
            if (this.k) {
                this.k = false;
                if (this.j != null) {
                    b(obj);
                }
            }
            if (!this.r) {
                int i = this.s + 1;
                this.s = i;
                if (i == 2 && (eVar = this.q) != null) {
                    eVar.a();
                    if (OpenLogUtil.isMapLogEnable()) {
                        com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap onDrawFirstFrame");
                    }
                }
                if (this.s != 2) {
                    z = false;
                }
                this.r = z;
            }
            WeakReference<MapSurfaceView> weakReference4 = this.h;
            if (weakReference4 == null || weakReference4.get().getBaseMap() == null || this.h.get().getBaseMap().h == null) {
                return;
            }
            for (ak akVar : this.h.get().getBaseMap().h) {
                if (this.h.get().getBaseMap() == null) {
                    return;
                }
                com.baidu.mapsdkplatform.comapi.map.w I = this.h.get().getBaseMap().I();
                if (akVar != null) {
                    akVar.a((GL10) null, I);
                }
            }
        }
    }

    public void a(boolean z) {
        this.f = z;
    }

    public void b() {
        this.x = true;
    }
}
