package com.baidu.platform.comapi.map;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.MapBound;
import com.baidu.platform.comapi.map.MapStatus;
import com.baidu.platform.comapi.map.ah;
import com.baidu.platform.comapi.map.f;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MapSurfaceView extends ai implements View.OnKeyListener, MapRenderModeChangeListener, MapViewInterface, aj {
    private static int s;
    private static final ExecutorService z = Executors.newSingleThreadExecutor();
    private int A;
    private int B;
    private int C;
    public MapController a;
    public ae b;
    public l c;
    public o d;
    public volatile boolean e;
    public boolean f;
    public al g;
    public GestureDetector h;
    public ab i;
    public com.baidu.mapsdkplatform.comapi.map.c j;
    private volatile boolean l;
    private volatile boolean m;
    private volatile boolean n;
    private volatile boolean o;
    private boolean p;
    private boolean q;
    private LocationOverlay r;
    private boolean t;
    private List<Overlay> u;
    private int v;
    private int w;
    private HashSet<aa> x;
    private boolean y;

    /* loaded from: classes.dex */
    public class a implements f.InterfaceC0022f {
        private int b;

        private a() {
            this.b = 12440;
        }

        public /* synthetic */ a(MapSurfaceView mapSurfaceView, s sVar) {
            this();
        }

        private String a(int i) {
            switch (i) {
                case 12288:
                    return "EGL_SUCCESS";
                case 12289:
                    return "EGL_NOT_INITIALIZED";
                case 12290:
                    return "EGL_BAD_ACCESS";
                case 12291:
                    return "EGL_BAD_ALLOC";
                case 12292:
                    return "EGL_BAD_ATTRIBUTE";
                case 12293:
                    return "EGL_BAD_CONFIG";
                case 12294:
                    return "EGL_BAD_CONTEXT";
                case 12295:
                    return "EGL_BAD_CURRENT_SURFACE";
                case 12296:
                    return "EGL_BAD_DISPLAY";
                case 12297:
                    return "EGL_BAD_MATCH";
                case 12298:
                    return "EGL_BAD_NATIVE_PIXMAP";
                case 12299:
                    return "EGL_BAD_NATIVE_WINDOW";
                case 12300:
                    return "EGL_BAD_PARAMETER";
                case 12301:
                    return "EGL_BAD_SURFACE";
                case 12302:
                    return "EGL_CONTEXT_LOST";
                default:
                    return b(i);
            }
        }

        private String b(int i) {
            return "0x" + Integer.toHexString(i);
        }

        @Override // com.baidu.platform.comapi.map.f.InterfaceC0022f
        public EGLContext a(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{this.b, 2, 12344});
        }

        public void a(String str, int i) {
            throw new RuntimeException(b(str, i));
        }

        @Override // com.baidu.platform.comapi.map.f.InterfaceC0022f
        public void a(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (!egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                Log.e("MapContextFactory", "display:" + eGLDisplay + " context: " + eGLContext);
                a("eglDestroyContex", egl10.eglGetError());
            }
            MapSurfaceView.this.onRecycle();
        }

        public String b(String str, int i) {
            return str + " failed: " + a(i);
        }
    }

    /* loaded from: classes.dex */
    public class b extends GestureDetector.SimpleOnGestureListener {
        private b() {
        }

        public /* synthetic */ b(MapSurfaceView mapSurfaceView, s sVar) {
            this();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            super.onLongPress(motionEvent);
            MapController mapController = MapSurfaceView.this.a;
            if (mapController == null || mapController.getBaseMap() == null) {
                return;
            }
            MapController mapController2 = MapSurfaceView.this.a;
            if (!mapController2.mIsMapLoadFinish) {
                return;
            }
            String GetNearlyObjID = mapController2.getBaseMap().GetNearlyObjID(-1L, (int) motionEvent.getX(), (int) motionEvent.getY(), MapSurfaceView.this.a.nearlyRadius);
            GeoPoint geoPoint = null;
            if (GetNearlyObjID == null || GetNearlyObjID.equals("")) {
                MapSurfaceView mapSurfaceView = MapSurfaceView.this;
                if (mapSurfaceView.a.mListeners == null) {
                    return;
                }
                if (mapSurfaceView.getProjection() != null) {
                    geoPoint = MapSurfaceView.this.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
                }
                if (geoPoint == null) {
                    return;
                }
                for (ak akVar : MapSurfaceView.this.a.mListeners) {
                    if (akVar != null) {
                        akVar.c(geoPoint);
                    }
                }
                return;
            }
            MapSurfaceView mapSurfaceView2 = MapSurfaceView.this;
            if (mapSurfaceView2.a.mListeners == null) {
                return;
            }
            if (mapSurfaceView2.getProjection() != null) {
                geoPoint = MapSurfaceView.this.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            for (ak akVar2 : MapSurfaceView.this.a.mListeners) {
                if (akVar2 != null) {
                    if (akVar2.b(GetNearlyObjID)) {
                        MapSurfaceView.this.a.mHasMapObjDraging = true;
                    } else if (geoPoint != null) {
                        akVar2.c(geoPoint);
                    }
                }
            }
        }
    }

    public MapSurfaceView(Context context) {
        super(context);
        this.l = false;
        this.m = false;
        this.n = false;
        this.o = true;
        this.p = true;
        this.q = true;
        this.a = null;
        this.b = null;
        this.c = null;
        this.e = false;
        this.t = true;
        this.u = new ArrayList();
        this.v = 0;
        this.w = 0;
        this.x = new HashSet<>();
        this.f = true;
        this.y = true;
        this.A = 0;
        this.B = 0;
        this.C = 0;
        s++;
    }

    public MapSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.l = false;
        this.m = false;
        this.n = false;
        this.o = true;
        this.p = true;
        this.q = true;
        this.a = null;
        this.b = null;
        this.c = null;
        this.e = false;
        this.t = true;
        this.u = new ArrayList();
        this.v = 0;
        this.w = 0;
        this.x = new HashSet<>();
        this.f = true;
        this.y = true;
        this.A = 0;
        this.B = 0;
        this.C = 0;
        s++;
    }

    public MapSurfaceView(Context context, ah.a aVar) {
        super(context, aVar);
        this.l = false;
        this.m = false;
        this.n = false;
        this.o = true;
        this.p = true;
        this.q = true;
        this.a = null;
        this.b = null;
        this.c = null;
        this.e = false;
        this.t = true;
        this.u = new ArrayList();
        this.v = 0;
        this.w = 0;
        this.x = new HashSet<>();
        this.f = true;
        this.y = true;
        this.A = 0;
        this.B = 0;
        this.C = 0;
        s++;
    }

    @Override // com.baidu.platform.comapi.map.ai
    public ag a(ah.a aVar, Context context) {
        ag a2 = super.a(aVar, context);
        if (a2 instanceof f) {
            f fVar = (f) a2;
            if (this.y) {
                fVar.a(new a(this, null));
            }
        }
        return a2;
    }

    public void a() {
        MapController mapController = this.a;
        if (mapController == null || mapController.getBaseMap() == null || this.b == null) {
            return;
        }
        this.u.clear();
        this.b.a();
    }

    @Override // com.baidu.platform.comapi.map.ai
    public void a(Context context, ah.a aVar) {
        super.a(context, aVar);
        setBackgroundColor(Color.rgb((int) nexClip.AVC_Profile_High444, 242, 240));
        setPixelFormatTransparent(false);
        this.g = new al();
        this.h = new GestureDetector(context, this.g);
        o oVar = new o(new WeakReference(this), this);
        this.d = oVar;
        setRenderer(oVar);
        setRenderMode(1);
        this.g.a(new b(this, null));
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap surfaceView initView");
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public synchronized boolean addOverlay(Overlay overlay) {
        if (overlay != null) {
            MapController mapController = this.a;
            if (mapController != null) {
                AppBaseMap baseMap = mapController.getBaseMap();
                if (baseMap == null) {
                    return false;
                }
                if (overlay instanceof an) {
                    return ((InnerOverlay) overlay).addedToMapView();
                } else if (overlay instanceof InnerOverlay) {
                    if (((InnerOverlay) overlay).mBaseMap == null) {
                        ((InnerOverlay) overlay).mBaseMap = getController().getBaseMap();
                    }
                    if (!((InnerOverlay) overlay).addedToMapView()) {
                        return false;
                    }
                    synchronized (this) {
                        this.u.add(overlay);
                        this.b.a((InnerOverlay) overlay);
                    }
                    return true;
                } else if (!(overlay instanceof ItemizedOverlay)) {
                    return false;
                } else {
                    long AddLayer = baseMap.AddLayer(((ItemizedOverlay) overlay).getUpdateType(), 0, MapController.ITEM_LAYER_TAG);
                    overlay.mLayerID = AddLayer;
                    if (AddLayer == 0) {
                        return false;
                    }
                    synchronized (this) {
                        this.u.add(overlay);
                        ((ItemizedOverlay) overlay).a();
                        baseMap.SetLayersClickable(overlay.mLayerID, true);
                        baseMap.ShowLayers(overlay.mLayerID, true);
                        baseMap.UpdateLayers(overlay.mLayerID);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void addSimpleOnGestureListener(GestureDetector.SimpleOnGestureListener simpleOnGestureListener) {
        this.g.a(simpleOnGestureListener);
    }

    public void addStateListener(aa aaVar) {
        if (aaVar != null) {
            this.x.add(aaVar);
        }
    }

    public void animateTo(MapStatus mapStatus, int i) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.setMapStatusWithAnimation(mapStatus, i);
        }
    }

    public void animateTo(MapStatus mapStatus, int i, int i2) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.setMapStatusWithAnimation(mapStatus, i, i2);
        }
    }

    public void b() {
        MapController mapController = this.a;
        if (mapController == null || mapController.getBaseMap() == null) {
            return;
        }
        a();
    }

    public void beginLocationLayerAnimation() {
        LocationOverlay locationOverlay = this.r;
        if (locationOverlay != null) {
            locationOverlay.beginLocationLayerAnimation();
        }
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ Bitmap captureImageFromSurface(int i, int i2, int i3, int i4, Object obj, Bitmap.Config config) {
        return super.captureImageFromSurface(i, i2, i3, i4, obj, config);
    }

    public void clearDefaultLocationLayerData(Bundle bundle) {
        this.r.clearLocationLayerData(bundle);
    }

    public void doCaptureMapView(c cVar, int i, int i2) {
        this.d.a(cVar, i, i2);
    }

    public void doCaptureMapView(c cVar, int i, int i2, Bitmap.Config config) {
        this.d.a(cVar, i, i2, config);
    }

    public void doCaptureMapView(c cVar, Rect rect, Bitmap.Config config) {
        if (rect != null) {
            int i = rect.left;
            int i2 = this.w;
            int i3 = rect.bottom;
            int i4 = i2 < i3 ? 0 : i2 - i3;
            int width = rect.width();
            int height = rect.height();
            if (i < 0 || i4 < 0 || width <= 0 || height <= 0) {
                return;
            }
            if (width > this.v) {
                width = Math.abs(rect.width()) - (rect.right - this.v);
            }
            int i5 = width;
            int abs = height > this.w ? Math.abs(rect.height()) - (rect.bottom - this.w) : height;
            if (i > SysOSUtil.getScreenSizeX() || i4 > SysOSUtil.getScreenSizeY()) {
                return;
            }
            this.d.a(cVar, i, i4, i5, abs, config);
            requestRender();
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean enable3D() {
        return true;
    }

    public void forceSetTraffic(boolean z2) {
        if (this.a != null) {
            this.l = z2;
        }
        z.submit(new t(this));
    }

    public com.baidu.mapsdkplatform.comapi.map.c getBaseMap() {
        return this.j;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapController getController() {
        return this.a;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus getCurrentMapStatus() {
        MapController mapController = this.a;
        if (mapController != null) {
            return mapController.getCurrentMapStatus();
        }
        return null;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getCurrentZoomLevel() {
        MapController mapController = this.a;
        if (mapController != null) {
            return mapController.getCurrentZoomLevel();
        }
        return 0.0f;
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ int getDebugFlags() {
        return super.getDebugFlags();
    }

    public LocationOverlay getDefaultLocationLay() {
        return this.r;
    }

    public int getFPS() {
        return this.k.e();
    }

    public float getFZoomToBoundF(MapBound mapBound, MapBound mapBound2) {
        if (this.a == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("left", mapBound.leftBottomPt.getIntX());
        bundle.putInt("bottom", mapBound.leftBottomPt.getIntY());
        bundle.putInt("right", mapBound.rightTopPt.getIntX());
        bundle.putInt("top", mapBound.rightTopPt.getIntY());
        Bundle bundle2 = new Bundle();
        bundle2.putInt("left", mapBound2.leftBottomPt.getIntX());
        bundle2.putInt("bottom", mapBound2.leftBottomPt.getIntY());
        bundle2.putInt("right", mapBound2.rightTopPt.getIntX());
        bundle2.putInt("top", mapBound2.rightTopPt.getIntY());
        return this.a.GetFZoomToBoundF(bundle, bundle2);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus.GeoBound getGeoRound() {
        MapController mapController = this.a;
        if (mapController == null) {
            return null;
        }
        return mapController.getMapStatus().geoRound;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getLatitudeSpan() {
        l lVar = (l) getProjection();
        return (int) Math.abs(lVar.fromPixels(0, 0).getLatitude() - lVar.fromPixels(this.v - 1, this.w - 1).getLatitude());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getLongitudeSpan() {
        l lVar = (l) getProjection();
        return (int) Math.abs(lVar.fromPixels(this.v - 1, this.w - 1).getLongitude() - lVar.fromPixels(0, 0).getLongitude());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public GeoPoint getMapCenter() {
        MapController mapController = this.a;
        if (mapController == null) {
            return null;
        }
        MapStatus mapStatus = mapController.getMapStatus();
        return new GeoPoint(mapStatus.centerPtY, mapStatus.centerPtX);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getMapRotation() {
        MapController mapController = this.a;
        if (mapController == null) {
            return 0;
        }
        return mapController.getMapStatus().rotation;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus getMapStatus() {
        MapController mapController = this.a;
        if (mapController != null) {
            return mapController.getMapStatus();
        }
        return null;
    }

    public MapViewListener getMapViewListener() {
        MapController mapController = this.a;
        if (mapController != null) {
            return mapController.getMapViewListener();
        }
        return null;
    }

    public OnLongPressListener getOnLongPressListener() {
        return this.g.a();
    }

    public synchronized Overlay getOverlay(int i) {
        if (i == 21) {
            return null;
        }
        for (Overlay overlay : this.u) {
            if (overlay.mType == i) {
                return overlay;
            }
        }
        return null;
    }

    public synchronized Overlay getOverlay(Class<?> cls) {
        for (Overlay overlay : this.u) {
            if (overlay.getClass() == cls) {
                return overlay;
            }
        }
        return null;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public List<Overlay> getOverlays() {
        return this.u;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getOverlooking() {
        MapController mapController = this.a;
        if (mapController == null) {
            return 0;
        }
        return mapController.getMapStatus().overlooking;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public Projection getProjection() {
        return this.c;
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ ag getRenderControl() {
        return super.getRenderControl();
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ int getRenderMode() {
        return super.getRenderMode();
    }

    public ExecutorService getSingleThreadPool() {
        return z;
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ ah.a getViewType() {
        return super.getViewType();
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus.WinRound getWinRound() {
        MapController mapController = this.a;
        if (mapController == null) {
            return null;
        }
        return mapController.getMapStatus().winRound;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomLevel() {
        MapController mapController = this.a;
        if (mapController != null) {
            return mapController.getZoomLevel();
        }
        return 0.0f;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBound(MapBound mapBound) {
        com.baidu.platform.comapi.util.SysOSUtil sysOSUtil = com.baidu.platform.comapi.util.SysOSUtil.getInstance();
        return getZoomToBound(mapBound, sysOSUtil.getScreenWidth(), sysOSUtil.getScreenHeight());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBound(MapBound mapBound, int i, int i2) {
        if (this.a == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("left", mapBound.leftBottomPt.getIntX());
        bundle.putInt("bottom", mapBound.leftBottomPt.getIntY());
        bundle.putInt("right", mapBound.rightTopPt.getIntX());
        bundle.putInt("top", mapBound.rightTopPt.getIntY());
        return this.a.getZoomToBound(bundle, i, i2);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBoundF(MapBound mapBound) {
        com.baidu.platform.comapi.util.SysOSUtil sysOSUtil = com.baidu.platform.comapi.util.SysOSUtil.getInstance();
        return getZoomToBoundF(mapBound, sysOSUtil.getScreenWidth(), sysOSUtil.getScreenHeight());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBoundF(MapBound mapBound, int i, int i2) {
        if (this.a == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("left", mapBound.leftBottomPt.getIntX());
        bundle.putInt("bottom", mapBound.leftBottomPt.getIntY());
        bundle.putInt("right", mapBound.rightTopPt.getIntX());
        bundle.putInt("top", mapBound.rightTopPt.getIntY());
        return this.a.getZoomToBoundF(bundle);
    }

    public boolean inRangeOfView(float f, float f2) {
        float f3 = 0;
        return f >= f3 && f <= ((float) (this.v + 0)) && f2 >= f3 && f2 <= ((float) (this.w + 0));
    }

    public synchronized boolean insertOverlay(Overlay overlay, int i) {
        MapController mapController;
        if ((overlay instanceof InnerOverlay) && (mapController = this.a) != null) {
            if (((InnerOverlay) overlay).mBaseMap == null) {
                ((InnerOverlay) overlay).mBaseMap = mapController.getBaseMap();
            }
            this.u.add(overlay);
            this.b.a((InnerOverlay) overlay);
        }
        return false;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isBaseIndoorMap() {
        return this.o;
    }

    public boolean isPredictTraffic() {
        return this.A > 0 || this.B > 0 || this.C > 0;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isSatellite() {
        return this.m;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isStreetRoad() {
        return this.n;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isTraffic() {
        return this.l;
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.updateDrawFPS();
        }
        if (Build.VERSION.SDK_INT < 14) {
            this.f = false;
        }
    }

    public void onBackground() {
        if (this.q) {
            return;
        }
        MapController mapController = this.a;
        if (mapController != null && mapController.getBaseMap() != null) {
            this.a.getBaseMap().OnBackground();
        }
        this.q = true;
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceView, android.view.View
    public void onDetachedFromWindow() {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.updateDrawFPS();
        }
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT < 14) {
            this.f = true;
        }
    }

    public void onForeground() {
        if (this.q) {
            MapController mapController = this.a;
            if (mapController != null && mapController.getBaseMap() != null) {
                this.a.getBaseMap().OnForeground();
            }
            this.q = false;
            if (this.k.b() != ah.a.VULKAN) {
                return;
            }
            o oVar = this.d;
            if (oVar != null) {
                oVar.a();
            }
            super.onResume();
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (this == view && keyEvent.getAction() == 0) {
            switch (i) {
                case 19:
                    this.a.scrollBy(0, -50);
                    return true;
                case 20:
                    this.a.scrollBy(0, 50);
                    return true;
                case 21:
                    this.a.scrollBy(-50, 0);
                    return true;
                case 22:
                    this.a.scrollBy(50, 0);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override // com.baidu.platform.comapi.map.MapRenderModeChangeListener
    public void onMapRenderModeChange(int i) {
        ab abVar;
        if (i == 1) {
            requestRender();
        } else if (i == 0) {
            if (getRenderMode() == 0) {
                return;
            }
            setRenderMode(0);
        } else if (i != 2 || (abVar = this.i) == null) {
        } else {
            abVar.a();
        }
    }

    @Override // com.baidu.platform.comapi.map.ai
    public void onPause() {
        if (this.p) {
            return;
        }
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap onPause");
        }
        o oVar = this.d;
        if (oVar != null) {
            oVar.b();
        }
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.onPause();
        }
        Iterator<aa> it = this.x.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
        super.onPause();
        this.p = true;
    }

    public void onRecycle() {
        MapController mapController = this.a;
        if (mapController == null || mapController.getBaseMap() == null) {
            return;
        }
        this.a.getBaseMap().ResetImageRes();
    }

    @Override // com.baidu.platform.comapi.map.MapRenderModeChangeListener
    public void onRequestRender() {
        requestRender();
    }

    @Override // com.baidu.platform.comapi.map.ai
    public void onResume() {
        if (this.p) {
            if (OpenLogUtil.isMapLogEnable()) {
                com.baidu.mapsdkplatform.comapi.commonutils.b a2 = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
                a2.a("BasicMap onResume isInited = " + this.e);
            }
            if (!this.e) {
                return;
            }
            o oVar = this.d;
            if (oVar != null) {
                oVar.a();
            }
            MapController mapController = this.a;
            if (mapController != null) {
                mapController.onResume();
            }
            Iterator<aa> it = this.x.iterator();
            while (it.hasNext()) {
                it.next().b(this);
            }
            setRenderMode(1);
            super.onResume();
            this.p = false;
        }
    }

    @Override // android.view.View, com.baidu.platform.comapi.map.MapViewInterface
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            GestureDetector gestureDetector = this.h;
            if (gestureDetector != null && gestureDetector.onTouchEvent(motionEvent)) {
                return true;
            }
            MapController mapController = this.a;
            if (mapController != null) {
                if (mapController.handleTouchEvent(motionEvent)) {
                    return true;
                }
            }
            return super.onTouchEvent(motionEvent);
        } catch (Exception unused) {
            return super.onTouchEvent(motionEvent);
        }
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ void queueEvent(Runnable runnable) {
        super.queueEvent(runnable);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void refresh(Overlay overlay) {
        if (overlay == null || this.a == null) {
            return;
        }
        if (overlay instanceof ItemizedOverlay) {
            ItemizedOverlay itemizedOverlay = (ItemizedOverlay) overlay;
            if (itemizedOverlay.b()) {
                if (itemizedOverlay.getAllItem().size() <= 0) {
                    this.a.getBaseMap().ClearLayer(overlay.mLayerID);
                    this.a.getBaseMap().ShowLayers(overlay.mLayerID, false);
                } else {
                    this.a.getBaseMap().ShowLayers(overlay.mLayerID, true);
                }
                this.a.getBaseMap().UpdateLayers(overlay.mLayerID);
                itemizedOverlay.a(false);
            }
        }
        MapController mapController = this.a;
        if (mapController == null || mapController.getBaseMap() == null) {
            return;
        }
        this.a.getBaseMap().UpdateLayers(overlay.mLayerID);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public synchronized boolean removeOverlay(Overlay overlay) {
        if (overlay != null) {
            MapController mapController = this.a;
            if (mapController != null) {
                AppBaseMap baseMap = mapController.getBaseMap();
                if (baseMap == null) {
                    return false;
                }
                baseMap.ClearLayer(overlay.mLayerID);
                baseMap.ShowLayers(overlay.mLayerID, false);
                baseMap.UpdateLayers(overlay.mLayerID);
                baseMap.RemoveLayer(overlay.mLayerID);
                synchronized (this) {
                    if (overlay instanceof ItemizedOverlay) {
                        this.u.remove(overlay);
                    } else if (overlay instanceof InnerOverlay) {
                        this.u.remove(overlay);
                        this.b.a(overlay);
                    }
                    overlay.mLayerID = 0L;
                }
                return true;
            }
        }
        return false;
    }

    public void removeSimpleOnGestureListener(GestureDetector.SimpleOnGestureListener simpleOnGestureListener) {
        this.g.b(simpleOnGestureListener);
    }

    public void removeStateListener(aa aaVar) {
        if (aaVar != null) {
            this.x.remove(aaVar);
        }
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ void requestRender() {
        super.requestRender();
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void saveScreenToLocal(String str) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.saveScreenToLocal(str);
        }
    }

    public void saveScreenToLocal(String str, Rect rect) {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar == null || cVar.a() == null) {
            return;
        }
        String str2 = null;
        if (rect != null) {
            int i = rect.left;
            int i2 = this.w;
            int i3 = rect.bottom;
            int i4 = i2 < i3 ? 0 : i2 - i3;
            int width = rect.width();
            int height = rect.height();
            if (i < 0 || i4 < 0 || width <= 0 || height <= 0) {
                return;
            }
            if (width > this.v) {
                width = Math.abs(rect.width()) - (rect.right - this.v);
            }
            if (height > this.w) {
                height = Math.abs(rect.height()) - (rect.bottom - this.w);
            }
            if (i > SysOSUtil.getScreenSizeX() || i4 > SysOSUtil.getScreenSizeY()) {
                this.j.a().SaveScreenToLocal(str, null);
                return;
            } else if (width != 0 && height != 0) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("x", i);
                    jSONObject.put("y", i4);
                    jSONObject.put(nexExportFormat.TAG_FORMAT_WIDTH, width);
                    jSONObject.put(nexExportFormat.TAG_FORMAT_HEIGHT, height);
                    str2 = jSONObject.toString();
                } catch (Exception unused) {
                }
            }
        }
        this.j.a().SaveScreenToLocal(str, str2);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setBaseIndoorMap(boolean z2) {
        if (this.a != null) {
            this.o = z2;
        }
        z.submit(new w(this, z2));
    }

    public void setBaseMap(com.baidu.mapsdkplatform.comapi.map.c cVar) {
        this.j = cVar;
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ void setDebugFlags(int i) {
        super.setDebugFlags(i);
    }

    public void setDefaultLocationLayerData(List<OverlayLocationData> list) {
        this.r.setLocationLayerData(list);
    }

    public void setFPS(int i) {
        this.k.a(i);
    }

    public void setFirstFrameListener(e eVar) {
        o oVar = this.d;
        if (oVar != null) {
            oVar.a(eVar);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setGeoRound(MapStatus.GeoBound geoBound) {
    }

    public boolean setItsPreTime(int i, int i2, int i3) {
        AppBaseMap baseMap;
        if (this.A == i && this.B == i2 && this.C == i3) {
            return true;
        }
        MapController mapController = this.a;
        if (mapController == null || (baseMap = mapController.getBaseMap()) == null) {
            return false;
        }
        this.A = i;
        this.B = i2;
        this.C = i3;
        return baseMap.SetItsPreTime(i, i2, i3);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setMapCenter(GeoPoint geoPoint) {
        MapController mapController = this.a;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.centerPtX = geoPoint.getLongitude();
            mapStatus.centerPtY = geoPoint.getLatitude();
            this.a.setMapStatus(mapStatus);
        }
    }

    public void setMapController(MapController mapController) {
        if (this.a != null) {
            return;
        }
        this.a = mapController;
        this.d.a(mapController.getBaseMap());
        this.d.a(true);
        ae aeVar = new ae(this.a.getBaseMap());
        this.b = aeVar;
        this.a.setOverlayMapCallBack(aeVar);
        this.a.setMapViewInterface(this);
        b();
        this.a.setMapRenderModeChangeListener(this);
        this.e = true;
        this.c = new l(this.a);
        this.g.a(this.a);
    }

    public void setMapRenderStableListener(ab abVar) {
        this.i = abVar;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setMapStatus(MapStatus mapStatus) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setMapTo2D(boolean z2) {
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.g.a(onLongPressListener);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setOverlooking(int i) {
        MapController mapController = this.a;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.overlooking = i;
            this.a.setMapStatus(mapStatus);
        }
    }

    public void setPixelFormatTransparent(boolean z2) {
        SurfaceHolder holder;
        int i;
        if (z2) {
            holder = getHolder();
            i = -3;
        } else {
            holder = getHolder();
            i = -1;
        }
        holder.setFormat(i);
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ void setRenderMode(int i) {
        super.setRenderMode(i);
    }

    @Override // com.baidu.platform.comapi.map.ai
    public /* bridge */ /* synthetic */ void setRenderer(ap apVar) {
        super.setRenderer(apVar);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setRotation(int i) {
        MapController mapController = this.a;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.rotation = i;
            this.a.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setSatellite(boolean z2) {
        if (this.a != null) {
            this.m = z2;
        }
        z.submit(new s(this));
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setStreetRoad(boolean z2) {
        if (this.a != null) {
            this.n = z2;
        }
        z.submit(new v(this));
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setTraffic(boolean z2) {
        if (this.l == z2) {
            return;
        }
        if (this.a != null) {
            this.l = z2;
        }
        z.submit(new u(this));
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setWinRound(MapStatus.WinRound winRound) {
        MapController mapController = this.a;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.winRound = winRound;
            this.a.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setZoomLevel(float f) {
        if (this.a == null) {
            return;
        }
        int i = 21;
        if (getController().getFocusedBaseIndoorMapInfo() != null) {
            i = 22;
        }
        if (f < 4.0f) {
            f = 4.0f;
        } else {
            float f2 = i;
            if (f > f2) {
                f = f2;
            }
        }
        MapStatus mapStatus = getMapStatus();
        if (mapStatus == null) {
            return;
        }
        mapStatus.level = f;
        animateTo(mapStatus, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setZoomLevel(int i) {
        setZoomLevel(i);
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.updateDrawFPS();
        }
        o oVar = this.d;
        if (oVar != null) {
            oVar.a = i2;
            oVar.b = i3;
            oVar.c = 0;
        }
        this.v = i2;
        this.w = i3;
        super.surfaceChanged(surfaceHolder, i, i2, i3);
        if (this.a != null) {
            MapStatus mapStatus = getMapStatus();
            if (mapStatus != null) {
                MapStatus.WinRound winRound = mapStatus.winRound;
                winRound.left = 0;
                winRound.top = 0;
                winRound.bottom = i3;
                winRound.right = i2;
                if (!this.t) {
                    this.a.setMapStatus(mapStatus, false);
                } else {
                    this.t = false;
                    setMapStatus(mapStatus);
                }
            }
            this.a.setScreenSize(this.v, this.w);
            if (this.a.isNaviMode() && this.a.getNaviMapViewListener() != null) {
                this.a.getNaviMapViewListener().resizeScreen(i2, i3);
            }
        }
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            cVar.a(this.v, this.w);
        }
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.updateDrawFPS();
        }
        super.surfaceCreated(surfaceHolder);
        if (surfaceHolder == null || surfaceHolder.getSurface().isValid()) {
            return;
        }
        surfaceDestroyed(surfaceHolder);
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        MapController mapController = this.a;
        if (mapController != null) {
            mapController.updateDrawFPS();
        }
        super.surfaceDestroyed(surfaceHolder);
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceHolder.Callback2
    @Deprecated
    public /* bridge */ /* synthetic */ void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
        super.surfaceRedrawNeeded(surfaceHolder);
    }

    @Override // com.baidu.platform.comapi.map.ai, android.view.SurfaceHolder.Callback2
    @TargetApi(26)
    public /* bridge */ /* synthetic */ void surfaceRedrawNeededAsync(SurfaceHolder surfaceHolder, Runnable runnable) {
        super.surfaceRedrawNeededAsync(surfaceHolder, runnable);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean switchOverlay(Overlay overlay, Overlay overlay2) {
        MapController mapController;
        AppBaseMap baseMap;
        if (overlay == null || overlay2 == null || (mapController = this.a) == null || (baseMap = mapController.getBaseMap()) == null) {
            return false;
        }
        return baseMap.SwitchLayer(overlay.mLayerID, overlay2.mLayerID);
    }

    public void unInit() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.j;
        if (cVar != null) {
            List<ak> list = cVar.h;
            if (list != null) {
                for (ak akVar : list) {
                    if (akVar != null) {
                        akVar.d();
                    }
                }
            }
            this.j.K();
            this.j = null;
        }
        this.a.unInit();
        this.a = null;
        this.b.a();
        this.b = null;
        this.c = null;
        this.d = null;
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap surfaceView unInit");
        }
    }
}
