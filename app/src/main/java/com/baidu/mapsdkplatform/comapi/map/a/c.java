package com.baidu.mapsdkplatform.comapi.map.a;

import android.os.Looper;
import android.os.Message;
import com.baidu.mapapi.map.track.TraceAnimationListener;
import com.baidu.mapapi.map.track.TraceOptions;
import com.baidu.mapapi.map.track.TraceOverlay;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapSurfaceView;
import com.baidu.platform.comapi.map.MapTextureView;
import com.baidu.platform.comapi.map.af;
import com.baidu.platform.comapi.map.ao;
import com.baidu.platform.comapi.map.aq;
import com.baidu.platform.comapi.util.h;
import com.baidu.platform.comapi.util.i;
import com.baidu.platform.comjni.engine.MessageProxy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class c {
    private com.baidu.mapsdkplatform.comapi.map.a.a a;
    private com.baidu.mapsdkplatform.comapi.map.c b;
    private int c;
    private TraceAnimationListener d;
    private b f;
    private MapSurfaceView g;
    private MapTextureView h;
    private a e = new a();
    private volatile boolean i = false;

    /* loaded from: classes.dex */
    public class a extends h {
        public a() {
            super(Looper.getMainLooper());
        }

        @Override // com.baidu.platform.comapi.util.h
        public void a(Message message) {
            int i = message.what;
            if (i != 65302) {
                if (i != 65303 || c.this.d == null) {
                    return;
                }
                c.this.d.onTraceUpdatePosition(CoordUtil.mc2ll(new GeoPoint(message.arg2 / 100.0f, message.arg1 / 100.0f)));
                return;
            }
            int i2 = message.arg1;
            if (i2 > 0 && i2 <= 1000 && c.this.d != null) {
                c.this.d.onTraceAnimationUpdate(message.arg1 / 10);
            }
            if (message.arg2 != 1 || c.this.d == null) {
                return;
            }
            c.this.d.onTraceAnimationFinish();
        }
    }

    public c(MapSurfaceView mapSurfaceView) {
        this.c = 1;
        if (mapSurfaceView == null) {
            return;
        }
        this.a = new com.baidu.mapsdkplatform.comapi.map.a.a();
        this.g = mapSurfaceView;
        this.b = mapSurfaceView.getBaseMap();
        mapSurfaceView.addOverlay(this.a);
        this.a.SetOverlayShow(true);
        this.c = 1;
    }

    public c(MapTextureView mapTextureView) {
        this.c = 1;
        if (mapTextureView == null) {
            return;
        }
        this.a = new com.baidu.mapsdkplatform.comapi.map.a.a();
        this.h = mapTextureView;
        this.b = mapTextureView.getBaseMap();
        mapTextureView.addOverlay(this.a);
        this.a.SetOverlayShow(true);
        this.c = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(TraceOverlay traceOverlay) {
        if (traceOverlay != null && this.a != null) {
            c();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(TraceOverlay traceOverlay) {
        com.baidu.mapsdkplatform.comapi.map.a.a aVar;
        if (traceOverlay == null || (aVar = this.a) == null) {
            return;
        }
        aVar.clear();
        i.b().execute(new f(this, traceOverlay));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(TraceOverlay traceOverlay) {
        if (this.a == null || traceOverlay == null) {
            return;
        }
        this.a.a(traceOverlay.isAnimate(), traceOverlay.getAnimationTime(), traceOverlay.getAnimationDuration(), traceOverlay.getAnimationType());
        af afVar = new af(new ao().a(-15794282).b(14));
        afVar.a(d(traceOverlay));
        afVar.a(new aq().d(1032).a(traceOverlay.getColor()).b(traceOverlay.getWidth()));
        afVar.c = traceOverlay.isTrackMove();
        this.a.a(afVar);
    }

    private List<GeoPoint> d(TraceOverlay traceOverlay) {
        if (traceOverlay == null || traceOverlay.getPoints() == null) {
            return null;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        ArrayList arrayList = new ArrayList();
        for (LatLng latLng : traceOverlay.getPoints()) {
            arrayList.add(CoordUtil.ll2mc(latLng));
            builder.include(latLng);
        }
        return arrayList;
    }

    public TraceOverlay a(TraceOptions traceOptions) {
        if (traceOptions == null) {
            return null;
        }
        TraceOverlay overlay = traceOptions.getOverlay();
        overlay.mListener = this.f;
        i.b().execute(new e(this, overlay));
        return overlay;
    }

    public void a() {
        this.f = new d(this);
        MessageProxy.registerMessageHandler(UIMsg.MsgDefine.V_WM_TRACK_MOVE_PROGRESS, this.e);
        MessageProxy.registerMessageHandler(UIMsg.MsgDefine.V_WM_TRACK_MOVE_POSITION, this.e);
    }

    public void a(TraceAnimationListener traceAnimationListener) {
        this.d = traceAnimationListener;
    }

    public void b() {
        com.baidu.mapsdkplatform.comapi.map.a.a aVar = this.a;
        if (aVar == null) {
            return;
        }
        aVar.clear();
        this.a.a();
    }

    public void c() {
        MapTextureView mapTextureView;
        MapSurfaceView mapSurfaceView;
        MessageProxy.unRegisterMessageHandler(UIMsg.MsgDefine.V_WM_TRACK_MOVE_PROGRESS, this.e);
        MessageProxy.unRegisterMessageHandler(UIMsg.MsgDefine.V_WM_TRACK_MOVE_POSITION, this.e);
        int i = this.c;
        if (i == 1 && (mapSurfaceView = this.g) != null) {
            mapSurfaceView.removeOverlay(this.a);
        } else if (i == 2 && (mapTextureView = this.h) != null) {
            mapTextureView.removeOverlay(this.a);
        }
        if (this.d != null) {
            this.d = null;
        }
        this.i = true;
    }

    public boolean d() {
        return this.i;
    }
}
