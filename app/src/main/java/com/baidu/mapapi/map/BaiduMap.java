package com.baidu.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.track.TraceAnimationListener;
import com.baidu.mapapi.map.track.TraceOptions;
import com.baidu.mapapi.map.track.TraceOverlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.map.MapSurfaceView;
import com.baidu.platform.comapi.map.MapTextureView;
import com.xiaomi.miai.api.StatusCode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class BaiduMap {
    public static final int MAP_TYPE_NONE = 3;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final float REAL_MAX_ZOOM_LEVEL = 21.0f;
    public static final float REAL_MIN_ZOOM_LEVEL = 4.0f;
    private static final String e = "BaiduMap";
    public static int mapStatusReason = 256;
    private OnMarkerDragListener A;
    private OnMyLocationClickListener B;
    private SnapshotReadyCallback C;
    private OnMapDrawFrameCallback D;
    private OnBaseIndoorMapListener E;
    private OnMapRenderValidDataListener F;
    private OnSynchronizationListener G;
    private TileOverlay H;
    private HeatMap I;
    private Map<String, InfoWindow> L;
    private Map<InfoWindow, Marker> M;
    private Marker N;
    private MyLocationData O;
    private MyLocationConfiguration P;
    private boolean Q;
    private boolean R;
    private boolean S;
    private boolean T;
    private Point U;
    private com.baidu.mapsdkplatform.comapi.map.a.c W;
    public MapView a;
    public TextureMapView b;
    public WearMapView c;
    public com.baidu.mapsdkplatform.comapi.map.v d;
    private Projection f;
    private UiSettings g;
    private MapSurfaceView h;
    private MapTextureView i;
    private com.baidu.mapsdkplatform.comapi.map.c j;
    private List<Overlay> k;
    private List<Marker> l;
    private List<Marker> m;
    private List<InfoWindow> n;
    private Overlay.a o;
    private InfoWindow.a p;
    private OnMapStatusChangeListener q;
    private OnMapTouchListener r;
    private OnMapClickListener s;
    private OnMapLoadedCallback t;
    private OnMapRenderCallback u;
    private OnMapDoubleClickListener v;
    private OnMapLongClickListener w;
    private CopyOnWriteArrayList<OnMarkerClickListener> x = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<OnPolylineClickListener> y = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<OnMultiPointClickListener> z = new CopyOnWriteArrayList<>();
    private Lock J = new ReentrantLock();
    private Lock K = new ReentrantLock();
    private volatile boolean V = false;

    /* loaded from: classes.dex */
    public interface OnBaseIndoorMapListener {
        void onBaseIndoorMapMode(boolean z, MapBaseIndoorMapInfo mapBaseIndoorMapInfo);
    }

    /* loaded from: classes.dex */
    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);

        void onMapPoiClick(MapPoi mapPoi);
    }

    /* loaded from: classes.dex */
    public interface OnMapDoubleClickListener {
        void onMapDoubleClick(LatLng latLng);
    }

    /* loaded from: classes.dex */
    public interface OnMapDrawFrameCallback {
        void onMapDrawFrame(MapStatus mapStatus);

        @Deprecated
        void onMapDrawFrame(GL10 gl10, MapStatus mapStatus);
    }

    /* loaded from: classes.dex */
    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    /* loaded from: classes.dex */
    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    /* loaded from: classes.dex */
    public interface OnMapRenderCallback {
        void onMapRenderFinished();
    }

    /* loaded from: classes.dex */
    public interface OnMapRenderValidDataListener {
        void onMapRenderValidData(boolean z, int i, String str);
    }

    /* loaded from: classes.dex */
    public interface OnMapStatusChangeListener {
        public static final int REASON_API_ANIMATION = 2;
        public static final int REASON_DEVELOPER_ANIMATION = 3;
        public static final int REASON_GESTURE = 1;

        void onMapStatusChange(MapStatus mapStatus);

        void onMapStatusChangeFinish(MapStatus mapStatus);

        void onMapStatusChangeStart(MapStatus mapStatus);

        void onMapStatusChangeStart(MapStatus mapStatus, int i);
    }

    /* loaded from: classes.dex */
    public interface OnMapTouchListener {
        void onTouch(MotionEvent motionEvent);
    }

    /* loaded from: classes.dex */
    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    /* loaded from: classes.dex */
    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    /* loaded from: classes.dex */
    public interface OnMultiPointClickListener {
        boolean onMultiPointClick(MultiPoint multiPoint, MultiPointItem multiPointItem);
    }

    /* loaded from: classes.dex */
    public interface OnMyLocationClickListener {
        boolean onMyLocationClick();
    }

    /* loaded from: classes.dex */
    public interface OnPolylineClickListener {
        boolean onPolylineClick(Polyline polyline);
    }

    /* loaded from: classes.dex */
    public interface OnSynchronizationListener {
        void onMapStatusChangeReason(int i);
    }

    /* loaded from: classes.dex */
    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    public BaiduMap(Context context, MapSurfaceView mapSurfaceView, com.baidu.mapsdkplatform.comapi.map.u uVar) {
        this.h = mapSurfaceView;
        com.baidu.mapsdkplatform.comapi.map.c cVar = new com.baidu.mapsdkplatform.comapi.map.c(context, mapSurfaceView, uVar, (String) null, 0);
        this.j = cVar;
        mapSurfaceView.setBaseMap(cVar);
        this.d = com.baidu.mapsdkplatform.comapi.map.v.GLSurfaceView;
        d();
    }

    public BaiduMap(Context context, MapTextureView mapTextureView, com.baidu.mapsdkplatform.comapi.map.u uVar) {
        this.i = mapTextureView;
        com.baidu.mapsdkplatform.comapi.map.c cVar = new com.baidu.mapsdkplatform.comapi.map.c(context, mapTextureView, uVar, (String) null, 0);
        this.j = cVar;
        mapTextureView.setBaseMap(cVar);
        this.d = com.baidu.mapsdkplatform.comapi.map.v.TextureView;
        d();
    }

    private Point a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int i = 0;
        int i2 = 0;
        for (String str2 : str.replaceAll("^\\{", "").replaceAll("\\}$", "").split(",")) {
            String[] split = str2.replaceAll("\"", "").split(":");
            if ("x".equals(split[0])) {
                i = Integer.valueOf(split[1]).intValue();
            }
            if ("y".equals(split[0])) {
                i2 = Integer.valueOf(split[1]).intValue();
            }
        }
        return new Point(i, i2);
    }

    private com.baidu.mapsdkplatform.comapi.map.w a(MapStatusUpdate mapStatusUpdate) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return null;
        }
        com.baidu.mapsdkplatform.comapi.map.w C = cVar.C();
        MapStatus a = mapStatusUpdate.a(this.j, getMapStatus());
        if (a != null) {
            return a.b(C);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(int i) {
        if (i != 0) {
            switch (i) {
                case 1004:
                    return "网络连接错误";
                case 1005:
                    return "请求发送错误";
                case 1006:
                    return "响应数据读取失败";
                case com.xiaomi.stat.c.b.g /* 1007 */:
                    return "返回响应数据过大，数据溢出";
                case com.xiaomi.stat.c.b.h /* 1008 */:
                    return "当前网络类型有问题";
                case com.xiaomi.stat.c.b.i /* 1009 */:
                    return "数据不一致";
                case com.xiaomi.stat.c.b.j /* 1010 */:
                    return "请求取消";
                case com.xiaomi.stat.c.b.k /* 1011 */:
                    return "网络超时错误";
                case com.xiaomi.stat.c.b.l /* 1012 */:
                    return "网络连接超时";
                case 1013:
                    return "网络发送超时";
                case 1014:
                    return "网络接收超时";
                case 1015:
                    return "DNS解析错误";
                case 1016:
                    return "DNS解析超时";
                case 1017:
                    return "网络写错误";
                case 1018:
                    return "SSL握手错误";
                case 1019:
                    return "SSL握手超时";
                default:
                    return "";
            }
        }
        return "数据请求成功";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.baidu.mapapi.map.InfoWindow r8) {
        /*
            r7 = this;
            if (r8 == 0) goto Lb5
            boolean r0 = r7.V
            if (r0 == 0) goto L8
            goto Lb5
        L8:
            java.util.Map<com.baidu.mapapi.map.InfoWindow, com.baidu.mapapi.map.Marker> r0 = r7.M
            java.util.Set r0 = r0.keySet()
            boolean r1 = r0.isEmpty()
            r2 = 0
            if (r1 != 0) goto Lb2
            boolean r0 = r0.contains(r8)
            if (r0 != 0) goto L1d
            goto Lb2
        L1d:
            android.view.View r0 = r8.c
            r1 = 1
            if (r0 == 0) goto L6c
            boolean r3 = r8.k
            if (r3 == 0) goto L6c
            r0.destroyDrawingCache()
            com.baidu.mapapi.map.MapViewLayoutParams$Builder r3 = new com.baidu.mapapi.map.MapViewLayoutParams$Builder
            r3.<init>()
            com.baidu.mapapi.map.MapViewLayoutParams$ELayoutMode r4 = com.baidu.mapapi.map.MapViewLayoutParams.ELayoutMode.mapMode
            com.baidu.mapapi.map.MapViewLayoutParams$Builder r3 = r3.layoutMode(r4)
            com.baidu.mapapi.model.LatLng r4 = r8.d
            com.baidu.mapapi.map.MapViewLayoutParams$Builder r3 = r3.position(r4)
            int r4 = r8.g
            com.baidu.mapapi.map.MapViewLayoutParams$Builder r3 = r3.yOffset(r4)
            com.baidu.mapapi.map.MapViewLayoutParams r3 = r3.build()
            int[] r4 = com.baidu.mapapi.map.b.b
            com.baidu.mapsdkplatform.comapi.map.v r5 = r7.d
            int r5 = r5.ordinal()
            r4 = r4[r5]
            if (r4 == r1) goto L5c
            r5 = 2
            if (r4 == r5) goto L54
            goto L66
        L54:
            com.baidu.mapapi.map.MapView r4 = r7.a
            if (r4 == 0) goto L66
            r4.addView(r0, r3)
            goto L66
        L5c:
            com.baidu.mapapi.map.TextureMapView r4 = r7.b
            if (r4 == 0) goto L66
            r4.addView(r0, r3)
            r0.setLayoutParams(r3)
        L66:
            boolean r0 = r8.j
            if (r0 == 0) goto L6c
            r0 = r2
            goto L6d
        L6c:
            r0 = r1
        L6d:
            com.baidu.mapapi.map.BitmapDescriptor r3 = r7.b(r8)
            java.util.Map<com.baidu.mapapi.map.InfoWindow, com.baidu.mapapi.map.Marker> r4 = r7.M
            java.lang.Object r4 = r4.get(r8)
            com.baidu.mapapi.map.Marker r4 = (com.baidu.mapapi.map.Marker) r4
            if (r4 == 0) goto Lb1
            android.os.Bundle r5 = new android.os.Bundle
            r5.<init>()
            com.baidu.mapapi.map.BitmapDescriptor r6 = r8.b
            if (r6 == 0) goto L97
            com.baidu.mapsdkplatform.comapi.map.h r6 = com.baidu.mapsdkplatform.comapi.map.h.popup
            r4.type = r6
            r4.b = r3
            android.view.View r3 = r8.c
            java.lang.String r6 = "draw_with_view"
            if (r3 == 0) goto L94
            r5.putInt(r6, r1)
            goto L97
        L94:
            r5.putInt(r6, r2)
        L97:
            com.baidu.mapapi.model.LatLng r1 = r8.d
            r4.a = r1
            int r8 = r8.g
            r4.i = r8
            r4.a(r5)
            com.baidu.mapsdkplatform.comapi.map.c r8 = r7.j
            if (r8 == 0) goto Lb1
            if (r0 == 0) goto Lb1
            boolean r8 = r7.V
            if (r8 != 0) goto Lb1
            com.baidu.mapsdkplatform.comapi.map.c r8 = r7.j
            r8.c(r5)
        Lb1:
            return
        Lb2:
            r7.showInfoWindow(r8, r2)
        Lb5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.BaiduMap.a(com.baidu.mapapi.map.InfoWindow):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0150  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void a(com.baidu.mapapi.map.MyLocationData r21, com.baidu.mapapi.map.MyLocationConfiguration r22) {
        /*
            Method dump skipped, instructions count: 489
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.BaiduMap.a(com.baidu.mapapi.map.MyLocationData, com.baidu.mapapi.map.MyLocationConfiguration):void");
    }

    private BitmapDescriptor b(InfoWindow infoWindow) {
        View view = infoWindow.c;
        if (view == null || !infoWindow.k) {
            return infoWindow.b;
        }
        if (!infoWindow.h) {
            return BitmapDescriptorFactory.fromView(view);
        }
        if (infoWindow.i <= 0) {
            infoWindow.i = SysOSUtil.getDensityDpi();
        }
        return BitmapDescriptorFactory.fromViewWithDpi(infoWindow.c, infoWindow.i);
    }

    private void d() {
        this.V = false;
        this.k = new CopyOnWriteArrayList();
        this.l = new CopyOnWriteArrayList();
        this.m = new CopyOnWriteArrayList();
        this.L = new ConcurrentHashMap();
        this.M = new ConcurrentHashMap();
        this.n = new CopyOnWriteArrayList();
        this.U = new Point((int) (SysOSUtil.getDensity() * 40.0f), (int) (SysOSUtil.getDensity() * 40.0f));
        this.g = new UiSettings(this.j);
        this.o = new a(this);
        this.p = new c(this);
        this.j.a(new d(this));
        this.j.a(new f(this));
        this.j.a(new g(this));
        this.Q = this.j.A();
        this.R = this.j.B();
    }

    public void a() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.u();
    }

    public void a(HeatMap heatMap) {
        this.J.lock();
        try {
            HeatMap heatMap2 = this.I;
            if (heatMap2 != null && this.j != null && heatMap == heatMap2) {
                heatMap2.b();
                this.I.c();
                this.I.a = null;
                this.j.o();
                this.I = null;
                this.j.q(false);
            }
        } finally {
            this.J.unlock();
        }
    }

    public void a(TileOverlay tileOverlay) {
        this.K.lock();
        if (tileOverlay != null) {
            try {
                if (this.H == tileOverlay) {
                    tileOverlay.b();
                    tileOverlay.a = null;
                    com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
                    if (cVar != null) {
                        cVar.e();
                    }
                }
            } finally {
                this.H = null;
                this.K.unlock();
            }
        }
    }

    public void addHeatMap(HeatMap heatMap) {
        if (heatMap == null || this.j == null) {
            return;
        }
        this.J.lock();
        try {
            HeatMap heatMap2 = this.I;
            if (heatMap == heatMap2) {
                return;
            }
            if (heatMap2 != null) {
                heatMap2.b();
                this.I.c();
                this.I.a = null;
                this.j.o();
            }
            this.I = heatMap;
            heatMap.a = this;
            this.j.q(true);
        } finally {
            this.J.unlock();
        }
    }

    public final Overlay addOverlay(OverlayOptions overlayOptions) {
        if (overlayOptions == null || this.V) {
            return null;
        }
        Overlay a = overlayOptions.a();
        a.listener = this.o;
        if (a instanceof Marker) {
            Marker marker = (Marker) a;
            marker.x = this.p;
            ArrayList<BitmapDescriptor> arrayList = marker.p;
            if (arrayList != null && arrayList.size() != 0) {
                this.l.add(marker);
                com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
                if (cVar != null) {
                    cVar.b(true);
                }
            }
            this.m.add(marker);
            InfoWindow infoWindow = marker.w;
            if (infoWindow != null) {
                showInfoWindow(infoWindow, false);
            }
        }
        Bundle bundle = new Bundle();
        a.a(bundle);
        if (this.j != null && !this.V) {
            this.j.b(bundle);
        }
        this.k.add(a);
        return a;
    }

    public final List<Overlay> addOverlays(List<OverlayOptions> list) {
        if (list == null || this.V) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        int i = size / StatusCode.BAD_REQUEST;
        int i2 = 0;
        while (i2 < i + 1) {
            Bundle[] bundleArr = new Bundle[i == 0 ? size : i2 == i ? size - (i * StatusCode.BAD_REQUEST) : 400];
            for (int i3 = 0; i3 < 400; i3++) {
                int i4 = (i2 * StatusCode.BAD_REQUEST) + i3;
                if (i4 >= size) {
                    break;
                } else if (this.V) {
                    return null;
                } else {
                    OverlayOptions overlayOptions = list.get(i4);
                    if (overlayOptions != null) {
                        Bundle bundle = new Bundle();
                        Overlay a = overlayOptions.a();
                        a.listener = this.o;
                        if (a instanceof Marker) {
                            Marker marker = (Marker) a;
                            marker.x = this.p;
                            ArrayList<BitmapDescriptor> arrayList2 = marker.p;
                            if (arrayList2 != null && arrayList2.size() != 0) {
                                this.l.add(marker);
                                com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
                                if (cVar != null) {
                                    cVar.b(true);
                                }
                            }
                            this.m.add(marker);
                        }
                        this.k.add(a);
                        arrayList.add(a);
                        a.a(bundle);
                        com.baidu.mapsdkplatform.comapi.map.c cVar2 = this.j;
                        if (cVar2 != null) {
                            cVar2.e(bundle);
                        }
                        bundleArr[i3] = bundle;
                    }
                }
            }
            com.baidu.mapsdkplatform.comapi.map.c cVar3 = this.j;
            if (cVar3 != null) {
                cVar3.a(bundleArr);
            }
            i2++;
        }
        return arrayList;
    }

    public TileOverlay addTileLayer(TileOverlayOptions tileOverlayOptions) {
        if (tileOverlayOptions == null) {
            return null;
        }
        TileOverlay tileOverlay = this.H;
        if (tileOverlay != null) {
            tileOverlay.b();
            this.H.a = null;
        }
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null || !cVar.a(tileOverlayOptions.a())) {
            return null;
        }
        TileOverlay a = tileOverlayOptions.a(this);
        this.H = a;
        return a;
    }

    public final TraceOverlay addTraceOverlay(TraceOptions traceOptions, TraceAnimationListener traceAnimationListener) {
        com.baidu.mapsdkplatform.comapi.map.a.c cVar;
        if (traceOptions == null) {
            return null;
        }
        com.baidu.mapsdkplatform.comapi.map.a.c cVar2 = this.W;
        if (cVar2 == null || cVar2.d()) {
            com.baidu.mapsdkplatform.comapi.map.v vVar = this.d;
            if (vVar == com.baidu.mapsdkplatform.comapi.map.v.GLSurfaceView) {
                cVar = new com.baidu.mapsdkplatform.comapi.map.a.c(this.h);
            } else if (vVar != com.baidu.mapsdkplatform.comapi.map.v.TextureView) {
                return null;
            } else {
                cVar = new com.baidu.mapsdkplatform.comapi.map.a.c(this.i);
            }
            this.W = cVar;
            this.W.a();
        }
        this.W.a(traceAnimationListener);
        return this.W.a(traceOptions);
    }

    public final void animateMapStatus(MapStatusUpdate mapStatusUpdate) {
        animateMapStatus(mapStatusUpdate, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
    }

    public final void animateMapStatus(MapStatusUpdate mapStatusUpdate, int i) {
        if (mapStatusUpdate == null || i <= 0) {
            return;
        }
        com.baidu.mapsdkplatform.comapi.map.w a = a(mapStatusUpdate);
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        mapStatusReason |= 256;
        if (!this.T) {
            cVar.a(a);
        } else {
            cVar.a(a, i);
        }
    }

    public boolean b() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.f();
    }

    public void c() {
        this.V = true;
        com.baidu.mapsdkplatform.comapi.map.a.c cVar = this.W;
        if (cVar != null) {
            cVar.c();
            this.W = null;
        }
    }

    public void changeLocationLayerOrder(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.e(z);
    }

    public void cleanCache(int i) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.a(i);
    }

    public final void clear() {
        if (this.V) {
            return;
        }
        this.k.clear();
        this.l.clear();
        this.m.clear();
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.b(false);
            this.j.n();
        }
        hideInfoWindow();
    }

    public List<InfoWindow> getAllInfoWindows() {
        return this.n;
    }

    public final Point getCompassPosition() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            return a(cVar.h());
        }
        return null;
    }

    public MapBaseIndoorMapInfo getFocusedBaseIndoorMapInfo() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return null;
        }
        return cVar.p();
    }

    public final int getFontSizeLevel() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.F();
            return 1;
        }
        return 1;
    }

    public MapSurfaceView getGLMapView() {
        return this.h;
    }

    @Deprecated
    public final MyLocationConfiguration getLocationConfigeration() {
        return getLocationConfiguration();
    }

    public final MyLocationConfiguration getLocationConfiguration() {
        return this.P;
    }

    public final MyLocationData getLocationData() {
        return this.O;
    }

    public final String getMapApprovalNumber() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        return cVar == null ? "" : cVar.N();
    }

    public MapLanguage getMapLanguage() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            return MapLanguage.values()[cVar.O()];
        }
        return MapLanguage.CHINESE;
    }

    public final MapStatus getMapStatus() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return null;
        }
        return MapStatus.a(cVar.C());
    }

    public final LatLngBounds getMapStatusLimit() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return null;
        }
        return cVar.D();
    }

    public MapTextureView getMapTextureView() {
        return this.i;
    }

    public final int getMapType() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return 1;
        }
        if (!cVar.l()) {
            return 3;
        }
        return this.j.k() ? 2 : 1;
    }

    public List<Marker> getMarkersInBounds(LatLngBounds latLngBounds) {
        if (getMapStatus() == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (this.m.size() == 0) {
            return null;
        }
        for (Marker marker : this.m) {
            if (latLngBounds.contains(marker.getPosition())) {
                arrayList.add(marker);
            }
        }
        return arrayList;
    }

    public final float getMaxZoomLevel() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return 0.0f;
        }
        return cVar.b();
    }

    public final float getMinZoomLevel() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return 0.0f;
        }
        return cVar.b;
    }

    public final Projection getProjection() {
        return this.f;
    }

    public float[] getProjectionMatrix() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return null;
        }
        return cVar.L();
    }

    public final UiSettings getUiSettings() {
        return this.g;
    }

    public float[] getViewMatrix() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return null;
        }
        return cVar.M();
    }

    public float getZoomToBound(int i, int i2, int i3, int i4, int i5, int i6) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return 0.0f;
        }
        return cVar.a(i, i2, i3, i4, i5, i6);
    }

    @Deprecated
    public MapSurfaceView getmGLMapView() {
        return this.h;
    }

    public void hideInfoWindow() {
        View view;
        MapView mapView;
        Collection<InfoWindow> values = this.L.values();
        if (!values.isEmpty()) {
            for (InfoWindow infoWindow : values) {
                if (infoWindow != null && (view = infoWindow.c) != null) {
                    int i = b.b[this.d.ordinal()];
                    if (i == 1) {
                        TextureMapView textureMapView = this.b;
                        if (textureMapView != null) {
                            textureMapView.removeView(view);
                        }
                    } else if (i == 2 && (mapView = this.a) != null) {
                        mapView.removeView(view);
                    }
                }
            }
        }
        for (Overlay overlay : this.k) {
            Set<String> keySet = this.L.keySet();
            String str = overlay.z;
            if ((overlay instanceof Marker) && !keySet.isEmpty() && keySet.contains(str)) {
                overlay.remove();
            }
        }
        this.L.clear();
        this.M.clear();
        this.n.clear();
    }

    public void hideInfoWindow(InfoWindow infoWindow) {
        MapView mapView;
        Set<InfoWindow> keySet = this.M.keySet();
        if (infoWindow == null || keySet.isEmpty() || !keySet.contains(infoWindow)) {
            return;
        }
        View view = infoWindow.c;
        if (view != null) {
            int i = b.b[this.d.ordinal()];
            if (i == 1) {
                TextureMapView textureMapView = this.b;
                if (textureMapView != null) {
                    textureMapView.removeView(view);
                }
            } else if (i == 2 && (mapView = this.a) != null) {
                mapView.removeView(view);
            }
        }
        Marker marker = this.M.get(infoWindow);
        if (marker != null) {
            marker.remove();
            this.L.remove(marker.z);
        }
        this.M.remove(infoWindow);
        this.n.remove(infoWindow);
    }

    public void hideSDKLayer() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.c();
    }

    public final boolean isBaiduHeatMapEnabled() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.i();
    }

    public boolean isBaseIndoorMapMode() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.q();
    }

    public final boolean isBuildingsEnabled() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.m();
    }

    public final boolean isMyLocationEnabled() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.t();
    }

    public final boolean isShowMapPoi() {
        return this.Q;
    }

    public final boolean isSupportBaiduHeatMap() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.j();
    }

    public final boolean isTrafficEnabled() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.g();
    }

    public final void removeMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (this.x.contains(onMarkerClickListener)) {
            this.x.remove(onMarkerClickListener);
        }
    }

    public void removeOverLays(List<Overlay> list) {
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        if (list == null || this.V) {
            return;
        }
        int size = list.size();
        int i = size / StatusCode.BAD_REQUEST;
        int i2 = 0;
        while (i2 < i + 1) {
            Bundle[] bundleArr = new Bundle[i == 0 ? size : i2 == i ? size - (i * StatusCode.BAD_REQUEST) : 400];
            for (int i3 = 0; i3 < 400; i3++) {
                int i4 = (i2 * StatusCode.BAD_REQUEST) + i3;
                if (i4 >= size) {
                    break;
                } else if (this.V) {
                    return;
                } else {
                    Overlay overlay = list.get(i4);
                    if (overlay != null) {
                        Bundle a = overlay.a();
                        com.baidu.mapsdkplatform.comapi.map.c cVar2 = this.j;
                        if (cVar2 != null) {
                            cVar2.e(a);
                        }
                        bundleArr[i3] = a;
                        List<Marker> list2 = this.m;
                        if (list2 != null && list2.contains(overlay)) {
                            this.m.remove(overlay);
                        }
                        if (this.l.contains(overlay)) {
                            Marker marker = (Marker) overlay;
                            if (marker.p != null) {
                                this.l.remove(marker);
                                if (this.l.size() == 0 && (cVar = this.j) != null) {
                                    cVar.b(false);
                                }
                            }
                        }
                    }
                }
            }
            com.baidu.mapsdkplatform.comapi.map.c cVar3 = this.j;
            if (cVar3 != null) {
                cVar3.b(bundleArr);
            }
            i2++;
        }
        this.k.removeAll(list);
    }

    public final void setBaiduHeatMapEnabled(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.j(z);
        }
    }

    public final void setBuildingsEnabled(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.l(z);
        }
    }

    public void setCompassEnable(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.g(z);
    }

    public void setCompassIcon(Bitmap bitmap) {
        if (bitmap != null) {
            com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
            if (cVar == null) {
                return;
            }
            cVar.a(bitmap);
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: compass's icon can not be null");
    }

    public void setCompassPosition(Point point) {
        if (this.j == null) {
            return;
        }
        if (!this.j.a(new Point(point.x, point.y))) {
            return;
        }
        this.U = point;
    }

    @Deprecated
    public boolean setCustomTrafficColor(String str, String str2, String str3, String str4) {
        if (this.j == null) {
            return false;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
            if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3) || !TextUtils.isEmpty(str4)) {
                return true;
            }
            this.j.a(Color.parseColor("#ffffffff"), Color.parseColor("#ffffffff"), Color.parseColor("#ffffffff"), Color.parseColor("#ffffffff"), false);
            return true;
        } else if (!str.matches("^#[0-9a-fA-F]{8}$") || !str2.matches("^#[0-9a-fA-F]{8}$") || !str3.matches("^#[0-9a-fA-F]{8}$") || !str4.matches("^#[0-9a-fA-F]{8}$")) {
            Log.e(e, "the string of the input customTrafficColor is error");
            return false;
        } else {
            this.j.a(Color.parseColor(str), Color.parseColor(str2), Color.parseColor(str3), Color.parseColor(str4), true);
            return true;
        }
    }

    public final void setFontSizeLevel(int i) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.b(i);
        }
    }

    public final void setIndoorEnable(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            this.S = z;
            cVar.n(z);
        }
        OnBaseIndoorMapListener onBaseIndoorMapListener = this.E;
        if (onBaseIndoorMapListener == null || z) {
            return;
        }
        onBaseIndoorMapListener.onBaseIndoorMapMode(false, null);
    }

    public void setLayerClickable(MapLayer mapLayer, boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.a(mapLayer, z);
    }

    public final void setMapLanguage(MapLanguage mapLanguage) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.c(mapLanguage.ordinal());
        }
    }

    public final void setMapStatus(MapStatusUpdate mapStatusUpdate) {
        if (mapStatusUpdate == null) {
            return;
        }
        com.baidu.mapsdkplatform.comapi.map.w a = a(mapStatusUpdate);
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.a(a);
        OnMapStatusChangeListener onMapStatusChangeListener = this.q;
        if (onMapStatusChangeListener == null) {
            return;
        }
        onMapStatusChangeListener.onMapStatusChange(getMapStatus());
    }

    public final void setMapStatusLimits(LatLngBounds latLngBounds) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.a(latLngBounds);
        setMapStatus(MapStatusUpdateFactory.newLatLngBounds(latLngBounds));
    }

    public final void setMapType(int i) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        if (i == 1) {
            cVar.a(false);
            this.j.y(this.Q);
            this.j.z(this.R);
            this.j.i(true);
            this.j.n(this.S);
        } else if (i == 2) {
            cVar.a(true);
            this.j.y(this.Q);
            this.j.z(this.R);
            this.j.i(true);
        } else if (i == 3) {
            if (cVar.A()) {
                this.j.y(false);
            }
            if (this.j.B()) {
                this.j.z(false);
            }
            this.j.i(false);
            this.j.n(false);
        }
        if (!OpenLogUtil.isMapLogEnable()) {
            return;
        }
        com.baidu.mapsdkplatform.comapi.commonutils.b a = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
        a.a("BasicMap setMapType type = " + i);
    }

    public final void setMaxAndMinZoomLevel(float f, float f2) {
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        if (f <= 21.0f && f2 >= 4.0f && f >= f2 && (cVar = this.j) != null) {
            cVar.a(f, f2);
        }
    }

    @Deprecated
    public final void setMyLocationConfigeration(MyLocationConfiguration myLocationConfiguration) {
        setMyLocationConfiguration(myLocationConfiguration);
    }

    public final void setMyLocationConfiguration(MyLocationConfiguration myLocationConfiguration) {
        this.P = myLocationConfiguration;
        a(this.O, myLocationConfiguration);
    }

    public final void setMyLocationData(MyLocationData myLocationData) {
        this.O = myLocationData;
        if (this.P == null) {
            this.P = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null);
        }
        a(myLocationData, this.P);
    }

    public final void setMyLocationEnabled(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.p(z);
        }
    }

    public final void setOnBaseIndoorMapListener(OnBaseIndoorMapListener onBaseIndoorMapListener) {
        this.E = onBaseIndoorMapListener;
    }

    public final void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        this.s = onMapClickListener;
    }

    public final void setOnMapDoubleClickListener(OnMapDoubleClickListener onMapDoubleClickListener) {
        this.v = onMapDoubleClickListener;
    }

    public final void setOnMapDrawFrameCallback(OnMapDrawFrameCallback onMapDrawFrameCallback) {
        this.D = onMapDrawFrameCallback;
    }

    public void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        this.t = onMapLoadedCallback;
    }

    public final void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        this.w = onMapLongClickListener;
    }

    public void setOnMapRenderCallbadk(OnMapRenderCallback onMapRenderCallback) {
        this.u = onMapRenderCallback;
    }

    public final void setOnMapRenderValidDataListener(OnMapRenderValidDataListener onMapRenderValidDataListener) {
        this.F = onMapRenderValidDataListener;
    }

    public final void setOnMapStatusChangeListener(OnMapStatusChangeListener onMapStatusChangeListener) {
        this.q = onMapStatusChangeListener;
    }

    public final void setOnMapTouchListener(OnMapTouchListener onMapTouchListener) {
        this.r = onMapTouchListener;
    }

    public final void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (onMarkerClickListener == null || this.x.contains(onMarkerClickListener)) {
            return;
        }
        this.x.add(onMarkerClickListener);
    }

    public final void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        this.A = onMarkerDragListener;
    }

    public final void setOnMultiPointClickListener(OnMultiPointClickListener onMultiPointClickListener) {
        if (onMultiPointClickListener != null) {
            this.z.add(onMultiPointClickListener);
        }
    }

    public final void setOnMyLocationClickListener(OnMyLocationClickListener onMyLocationClickListener) {
        this.B = onMyLocationClickListener;
    }

    public final void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        if (onPolylineClickListener != null) {
            this.y.add(onPolylineClickListener);
        }
    }

    public final void setOnSynchronizationListener(OnSynchronizationListener onSynchronizationListener) {
        this.G = onSynchronizationListener;
    }

    public void setOverlayUnderPoi(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.d(z);
    }

    @Deprecated
    public final void setPadding(int i, int i2, int i3, int i4) {
        setViewPadding(i, i2, i3, i4);
    }

    public void setPixelFormatTransparent(boolean z) {
        MapSurfaceView mapSurfaceView = this.h;
        if (mapSurfaceView == null) {
            return;
        }
        mapSurfaceView.setPixelFormatTransparent(z);
    }

    public final void setTrafficEnabled(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.k(z);
        }
    }

    public final void setViewPadding(int i, int i2, int i3, int i4) {
        ViewGroup viewGroup;
        MapView mapView;
        if (i < 0 || i2 < 0 || i3 < 0 || i4 < 0 || this.j == null) {
            return;
        }
        int i5 = b.b[this.d.ordinal()];
        if (i5 == 1) {
            TextureMapView textureMapView = this.b;
            if (textureMapView == null) {
                return;
            }
            float width = ((textureMapView.getWidth() - i) - i3) / this.b.getWidth();
            float height = ((this.b.getHeight() - i2) - i4) / this.b.getHeight();
            com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
            Point point = this.U;
            cVar.a(new Point((int) (i + (point.x * width)), (int) (i2 + (point.y * height))));
            this.b.setPadding(i, i2, i3, i4);
            viewGroup = this.b;
        } else if (i5 != 2 || (mapView = this.a) == null) {
            return;
        } else {
            float width2 = ((mapView.getWidth() - i) - i3) / this.a.getWidth();
            float height2 = ((this.a.getHeight() - i2) - i4) / this.a.getHeight();
            com.baidu.mapsdkplatform.comapi.map.c cVar2 = this.j;
            Point point2 = this.U;
            cVar2.a(new Point((int) (i + (point2.x * width2)), (int) (i2 + (point2.y * height2))));
            this.a.setPadding(i, i2, i3, i4);
            viewGroup = this.a;
        }
        viewGroup.invalidate();
    }

    public void showInfoWindow(InfoWindow infoWindow) {
        showInfoWindow(infoWindow, true);
    }

    public void showInfoWindow(InfoWindow infoWindow, boolean z) {
        boolean z2;
        MapView mapView;
        Set<InfoWindow> keySet = this.M.keySet();
        if (infoWindow == null || keySet.contains(infoWindow) || this.V) {
            return;
        }
        if (z) {
            hideInfoWindow();
        }
        infoWindow.f = this.p;
        View view = infoWindow.c;
        if (view == null || !infoWindow.k) {
            z2 = true;
        } else {
            view.destroyDrawingCache();
            MapViewLayoutParams build = new MapViewLayoutParams.Builder().layoutMode(MapViewLayoutParams.ELayoutMode.mapMode).position(infoWindow.d).yOffset(infoWindow.g).build();
            int i = b.b[this.d.ordinal()];
            if (i == 1) {
                TextureMapView textureMapView = this.b;
                if (textureMapView != null) {
                    textureMapView.addView(view, build);
                }
            } else if (i == 2 && (mapView = this.a) != null) {
                mapView.addView(view, build);
            }
            z2 = false;
        }
        BitmapDescriptor b = b(infoWindow);
        if (b == null) {
            return;
        }
        Overlay a = new MarkerOptions().perspective(false).icon(b).position(infoWindow.d).zIndex(Integer.MAX_VALUE).yOffset(infoWindow.g).infoWindow(infoWindow).a();
        a.listener = this.o;
        a.type = com.baidu.mapsdkplatform.comapi.map.h.popup;
        Bundle bundle = new Bundle();
        a.a(bundle);
        if (infoWindow.c != null) {
            bundle.putInt("draw_with_view", 1);
        } else {
            bundle.putInt("draw_with_view", 0);
        }
        if (this.j != null && z2 && !this.V) {
            this.j.b(bundle);
            this.k.add(a);
        }
        Marker marker = (Marker) a;
        marker.x = this.p;
        this.L.put(marker.z, infoWindow);
        this.M.put(infoWindow, marker);
        this.n.add(infoWindow);
    }

    public void showInfoWindows(List<InfoWindow> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (InfoWindow infoWindow : list) {
            showInfoWindow(infoWindow, false);
        }
    }

    public final void showMapIndoorPoi(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.z(z);
            this.R = z;
        }
    }

    public final void showMapPoi(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.y(z);
            this.Q = z;
        }
    }

    public void showOperateLayer(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.c(z);
    }

    public void showSDKLayer() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.d();
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        MapSurfaceView mapSurfaceView;
        this.C = snapshotReadyCallback;
        int i = b.b[this.d.ordinal()];
        if (i != 1) {
            if (i != 2 || (mapSurfaceView = this.h) == null || mapSurfaceView.getController() == null) {
                return;
            }
            this.h.doCaptureMapView(new i(this), this.h.getController().getScreenWidth(), this.h.getController().getScreenHeight(), Bitmap.Config.ARGB_8888);
            this.h.requestRender();
            return;
        }
        MapTextureView mapTextureView = this.i;
        if (mapTextureView == null || mapTextureView.getController() == null) {
            return;
        }
        this.i.doCaptureMapView(new h(this), this.i.getController().getScreenWidth(), this.i.getController().getScreenHeight(), Bitmap.Config.ARGB_8888);
        this.i.requestRender();
    }

    public final void snapshotScope(Rect rect, SnapshotReadyCallback snapshotReadyCallback) {
        MapSurfaceView mapSurfaceView;
        if (this.j == null) {
            return;
        }
        this.C = snapshotReadyCallback;
        int i = b.b[this.d.ordinal()];
        if (i != 1) {
            if (i != 2 || (mapSurfaceView = this.h) == null) {
                return;
            }
            mapSurfaceView.doCaptureMapView(new k(this), rect, Bitmap.Config.ARGB_8888);
            this.h.requestRender();
            return;
        }
        MapTextureView mapTextureView = this.i;
        if (mapTextureView == null) {
            return;
        }
        mapTextureView.doCaptureMapView(new j(this), rect, Bitmap.Config.ARGB_8888);
        this.i.requestRender();
    }

    public MapBaseIndoorMapInfo.SwitchFloorError switchBaseIndoorMapFloor(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return MapBaseIndoorMapInfo.SwitchFloorError.FLOOR_INFO_ERROR;
        }
        MapBaseIndoorMapInfo focusedBaseIndoorMapInfo = getFocusedBaseIndoorMapInfo();
        if (focusedBaseIndoorMapInfo == null) {
            return MapBaseIndoorMapInfo.SwitchFloorError.SWITCH_ERROR;
        }
        if (!str2.equals(focusedBaseIndoorMapInfo.a)) {
            return MapBaseIndoorMapInfo.SwitchFloorError.FOCUSED_ID_ERROR;
        }
        ArrayList<String> floors = focusedBaseIndoorMapInfo.getFloors();
        if (floors == null || !floors.contains(str)) {
            return MapBaseIndoorMapInfo.SwitchFloorError.FLOOR_OVERLFLOW;
        }
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        return (cVar == null || !cVar.a(str, str2)) ? MapBaseIndoorMapInfo.SwitchFloorError.SWITCH_ERROR : MapBaseIndoorMapInfo.SwitchFloorError.SWITCH_OK;
    }

    public void switchLayerOrder(MapLayer mapLayer, MapLayer mapLayer2) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return;
        }
        cVar.a(mapLayer, mapLayer2);
    }

    public boolean switchOverlayLayerAndNavigationLayer(boolean z) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null) {
            return false;
        }
        return cVar.f(z);
    }
}
