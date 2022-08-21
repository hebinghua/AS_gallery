package com.baidu.platform.comapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.MapBound;
import com.baidu.platform.comapi.map.MapStatus;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* loaded from: classes.dex */
public class MapTextureView extends h implements MapRenderModeChangeListener, MapViewInterface, aj {
    public com.baidu.mapsdkplatform.comapi.map.c a;
    public MapController b;
    public ae c;
    public l d;
    public o e;
    public int f;
    public int g;
    public List<Overlay> h;
    public ab i;
    public al j;
    public GestureDetector k;
    private c l;

    /* loaded from: classes.dex */
    public class a implements GLSurfaceView.EGLContextFactory {
        private int b;

        private a() {
            this.b = 12440;
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

        public void a(String str, int i) {
            throw new RuntimeException(b(str, i));
        }

        public String b(String str, int i) {
            return str + " failed: " + a(i);
        }

        @Override // android.opengl.GLSurfaceView.EGLContextFactory
        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{this.b, 2, 12344});
        }

        @Override // android.opengl.GLSurfaceView.EGLContextFactory
        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            if (!egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
                a("eglDestroyContex", egl10.eglGetError());
            }
            MapTextureView.this.onRecycle();
        }
    }

    /* loaded from: classes.dex */
    public class b extends GestureDetector.SimpleOnGestureListener {
        private b() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            super.onLongPress(motionEvent);
            MapController mapController = MapTextureView.this.b;
            if (mapController == null || mapController.getBaseMap() == null) {
                return;
            }
            MapController mapController2 = MapTextureView.this.b;
            if (!mapController2.mIsMapLoadFinish) {
                return;
            }
            String GetNearlyObjID = mapController2.getBaseMap().GetNearlyObjID(-1L, (int) motionEvent.getX(), (int) motionEvent.getY(), MapTextureView.this.b.nearlyRadius);
            GeoPoint geoPoint = null;
            if (GetNearlyObjID == null || GetNearlyObjID.equals("")) {
                MapTextureView mapTextureView = MapTextureView.this;
                if (mapTextureView.b.mListeners == null) {
                    return;
                }
                if (mapTextureView.getProjection() != null) {
                    geoPoint = MapTextureView.this.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
                }
                if (geoPoint == null) {
                    return;
                }
                for (ak akVar : MapTextureView.this.b.mListeners) {
                    if (akVar != null) {
                        akVar.c(geoPoint);
                    }
                }
                return;
            }
            MapTextureView mapTextureView2 = MapTextureView.this;
            if (mapTextureView2.b.mListeners == null) {
                return;
            }
            if (mapTextureView2.getProjection() != null) {
                geoPoint = MapTextureView.this.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            for (ak akVar2 : MapTextureView.this.b.mListeners) {
                if (akVar2 != null) {
                    if (akVar2.b(GetNearlyObjID)) {
                        MapTextureView.this.b.mHasMapObjDraging = true;
                    } else if (geoPoint != null) {
                        akVar2.c(geoPoint);
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void a(int i);
    }

    public MapTextureView(Context context) {
        super(context);
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.h = new ArrayList();
        a(context);
    }

    public MapTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.h = new ArrayList();
        a(context);
    }

    public MapTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.h = new ArrayList();
        a(context);
    }

    private void a(Context context) {
        setEGLContextClientVersion(2);
        this.j = new al();
        this.k = new GestureDetector(context, this.j);
        this.j.a(new b());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean addOverlay(Overlay overlay) {
        MapController mapController;
        AppBaseMap baseMap;
        if (overlay == null || (mapController = this.b) == null || (baseMap = mapController.getBaseMap()) == null) {
            return false;
        }
        if (overlay instanceof InnerOverlay) {
            InnerOverlay innerOverlay = (InnerOverlay) overlay;
            if (innerOverlay.mBaseMap == null) {
                innerOverlay.mBaseMap = getController().getBaseMap();
            }
            if (!innerOverlay.addedToMapView()) {
                return false;
            }
            this.h.add(overlay);
            this.c.a(innerOverlay);
            return true;
        }
        if (overlay instanceof ItemizedOverlay) {
            ItemizedOverlay itemizedOverlay = (ItemizedOverlay) overlay;
            long AddLayer = baseMap.AddLayer(itemizedOverlay.getUpdateType(), 0, MapController.ITEM_LAYER_TAG);
            overlay.mLayerID = AddLayer;
            if (AddLayer == 0) {
                return false;
            }
            this.h.add(overlay);
            itemizedOverlay.a();
            baseMap.SetLayersClickable(overlay.mLayerID, true);
            baseMap.ShowLayers(overlay.mLayerID, true);
            baseMap.UpdateLayers(overlay.mLayerID);
            return true;
        }
        return false;
    }

    public void animateTo(MapStatus mapStatus, int i) {
        MapController mapController = this.b;
        if (mapController != null) {
            mapController.setMapStatusWithAnimation(mapStatus, i);
        }
    }

    public void attachBaseMapController(MapController mapController) {
        o oVar = new o(this, this);
        this.e = oVar;
        this.b = mapController;
        oVar.a(mapController.getBaseMap());
        setEGLContextFactory(new a());
        setRenderer(this.e);
        setRenderMode(0);
        this.e.a(true);
        ae aeVar = new ae(this.b.getBaseMap());
        this.c = aeVar;
        this.b.setOverlayMapCallBack(aeVar);
        this.b.setMapViewInterface(this);
        b();
        this.b.setMapRenderModeChangeListener(this);
        this.d = new l(this.b);
        this.j.a(this.b);
    }

    public void b() {
        MapController mapController = this.b;
        if (mapController == null || mapController.getBaseMap() == null) {
            return;
        }
        c();
    }

    public void c() {
        MapController mapController = this.b;
        if (mapController == null || mapController.getBaseMap() == null || this.c == null) {
            return;
        }
        this.h.clear();
        this.c.a();
    }

    public void destroyForMultiViews() {
        MapController mapController = this.b;
        if (mapController != null) {
            mapController.unInitForMultiTextureView();
            this.b = null;
        }
        ae aeVar = this.c;
        if (aeVar != null) {
            aeVar.a();
            this.c = null;
        }
        this.d = null;
    }

    public void doCaptureMapView(com.baidu.platform.comapi.map.c cVar, int i, int i2, Bitmap.Config config) {
        this.e.a(cVar, i, i2, config);
    }

    public void doCaptureMapView(com.baidu.platform.comapi.map.c cVar, Rect rect, Bitmap.Config config) {
        if (rect != null) {
            int i = rect.left;
            int i2 = this.g;
            int i3 = rect.bottom;
            int i4 = i2 < i3 ? 0 : i2 - i3;
            int width = rect.width();
            int height = rect.height();
            if (i < 0 || i4 < 0 || width <= 0 || height <= 0) {
                return;
            }
            if (width > this.f) {
                width = Math.abs(rect.width()) - (rect.right - this.f);
            }
            int i5 = width;
            int abs = height > this.g ? Math.abs(rect.height()) - (rect.bottom - this.g) : height;
            if (i > SysOSUtil.getScreenSizeX() || i4 > SysOSUtil.getScreenSizeY()) {
                return;
            }
            this.e.a(cVar, i, i4, i5, abs, config);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean enable3D() {
        return false;
    }

    public com.baidu.mapsdkplatform.comapi.map.c getBaseMap() {
        return this.a;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapController getController() {
        return this.b;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus getCurrentMapStatus() {
        MapController mapController = this.b;
        if (mapController != null) {
            return mapController.getCurrentMapStatus();
        }
        return null;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getCurrentZoomLevel() {
        MapController mapController = this.b;
        if (mapController != null) {
            return mapController.getCurrentZoomLevel();
        }
        return 0.0f;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus.GeoBound getGeoRound() {
        MapController mapController = this.b;
        if (mapController == null) {
            return null;
        }
        return mapController.getMapStatus().geoRound;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getLatitudeSpan() {
        l lVar = (l) getProjection();
        return (int) Math.abs(lVar.fromPixels(0, 0).getLatitude() - lVar.fromPixels(this.f - 1, this.g - 1).getLatitude());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getLongitudeSpan() {
        l lVar = (l) getProjection();
        return (int) Math.abs(lVar.fromPixels(this.f - 1, this.g - 1).getLongitude() - lVar.fromPixels(0, 0).getLongitude());
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public GeoPoint getMapCenter() {
        MapController mapController = this.b;
        if (mapController == null) {
            return null;
        }
        MapStatus mapStatus = mapController.getMapStatus();
        return new GeoPoint(mapStatus.centerPtY, mapStatus.centerPtX);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getMapRotation() {
        MapController mapController = this.b;
        if (mapController == null) {
            return 0;
        }
        return mapController.getMapStatus().rotation;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus getMapStatus() {
        MapController mapController = this.b;
        if (mapController != null) {
            return mapController.getMapStatus();
        }
        return null;
    }

    public synchronized Overlay getOverlay(int i) {
        for (Overlay overlay : this.h) {
            if (overlay.mType == i) {
                return overlay;
            }
        }
        return null;
    }

    public synchronized Overlay getOverlay(Class<?> cls) {
        for (Overlay overlay : this.h) {
            if (overlay.getClass() == cls) {
                return overlay;
            }
        }
        return null;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public List<Overlay> getOverlays() {
        return this.h;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public int getOverlooking() {
        MapController mapController = this.b;
        if (mapController == null) {
            return 0;
        }
        return mapController.getMapStatus().overlooking;
    }

    public Overlay getPopupOverlay() {
        return null;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public Projection getProjection() {
        return this.d;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public MapStatus.WinRound getWinRound() {
        MapController mapController = this.b;
        if (mapController == null) {
            return null;
        }
        return mapController.getMapStatus().winRound;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomLevel() {
        MapController mapController = this.b;
        if (mapController != null) {
            return mapController.getZoomLevel();
        }
        return 0.0f;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBound(MapBound mapBound) {
        return getZoomToBound(mapBound, this.f, this.g);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBound(MapBound mapBound, int i, int i2) {
        if (this.b == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("left", mapBound.leftBottomPt.getIntX());
        bundle.putInt("bottom", mapBound.leftBottomPt.getIntY());
        bundle.putInt("right", mapBound.rightTopPt.getIntX());
        bundle.putInt("top", mapBound.rightTopPt.getIntY());
        return this.b.getZoomToBound(bundle, i, i2);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBoundF(MapBound mapBound) {
        return getZoomToBoundF(mapBound, this.f, this.g);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public float getZoomToBoundF(MapBound mapBound, int i, int i2) {
        if (this.b == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("left", mapBound.leftBottomPt.getIntX());
        bundle.putInt("bottom", mapBound.leftBottomPt.getIntY());
        bundle.putInt("right", mapBound.rightTopPt.getIntX());
        bundle.putInt("top", mapBound.rightTopPt.getIntY());
        return this.b.getZoomToBoundF(bundle);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isBaseIndoorMap() {
        return false;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isSatellite() {
        return false;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isStreetRoad() {
        return false;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean isTraffic() {
        return false;
    }

    public void listenMapRenderMessage(c cVar) {
        this.l = cVar;
    }

    public void onDestroy() {
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.a;
        if (cVar != null) {
            List<ak> list = cVar.h;
            if (list != null) {
                for (ak akVar : list) {
                    if (akVar != null) {
                        akVar.d();
                    }
                }
            }
            this.a.K();
            this.a = null;
        }
        this.b.unInit();
        this.b = null;
        this.c.a();
        this.c = null;
        this.d = null;
    }

    @Override // com.baidu.platform.comapi.map.MapRenderModeChangeListener
    public void onMapRenderModeChange(int i) {
        ab abVar;
        c cVar = this.l;
        if (cVar != null) {
            cVar.a(i);
        }
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

    @Override // com.baidu.platform.comapi.map.h
    public void onPause() {
        MapController mapController = this.b;
        if (mapController != null) {
            mapController.getBaseMap().OnPause();
        }
        super.onPause();
    }

    public void onRecycle() {
        MapController mapController = this.b;
        if (mapController == null || mapController.getBaseMap() == null) {
            return;
        }
        this.b.getBaseMap().ResetImageRes();
    }

    @Override // com.baidu.platform.comapi.map.MapRenderModeChangeListener
    public void onRequestRender() {
    }

    @Override // com.baidu.platform.comapi.map.h
    public void onResume() {
        MapController mapController = this.b;
        if (mapController != null) {
            mapController.getBaseMap().OnResume();
        }
        super.onResume();
    }

    @Override // com.baidu.platform.comapi.map.h, android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        super.onSurfaceTextureAvailable(surfaceTexture, i, i2);
        this.f = i;
        this.g = i2;
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.a;
        if (cVar != null) {
            cVar.a(i, i2);
        }
    }

    @Override // com.baidu.platform.comapi.map.h, android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        super.onSurfaceTextureDestroyed(surfaceTexture);
        return true;
    }

    @Override // com.baidu.platform.comapi.map.h, android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        super.onSurfaceTextureSizeChanged(surfaceTexture, i, i2);
        this.f = i;
        this.g = i2;
        o oVar = this.e;
        oVar.a = i;
        oVar.b = i2;
        oVar.c = 0;
        if (this.b != null) {
            MapStatus mapStatus = getMapStatus();
            MapStatus.WinRound winRound = mapStatus.winRound;
            winRound.left = 0;
            winRound.top = 0;
            winRound.bottom = i2;
            winRound.right = i;
            setMapStatus(mapStatus);
            this.b.setScreenSize(this.f, this.g);
        }
        com.baidu.mapsdkplatform.comapi.map.c cVar = this.a;
        if (cVar != null) {
            cVar.a(this.f, this.g);
        }
    }

    @Override // com.baidu.platform.comapi.map.h, android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        super.onSurfaceTextureUpdated(surfaceTexture);
    }

    @Override // android.view.View, com.baidu.platform.comapi.map.MapViewInterface
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            GestureDetector gestureDetector = this.k;
            if (gestureDetector != null && gestureDetector.onTouchEvent(motionEvent)) {
                return true;
            }
            MapController mapController = this.b;
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

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void refresh(Overlay overlay) {
        if (overlay == null || this.b == null) {
            return;
        }
        if (overlay instanceof ItemizedOverlay) {
            ItemizedOverlay itemizedOverlay = (ItemizedOverlay) overlay;
            if (itemizedOverlay.b()) {
                if (itemizedOverlay.getAllItem().size() <= 0) {
                    this.b.getBaseMap().ClearLayer(overlay.mLayerID);
                    this.b.getBaseMap().ShowLayers(overlay.mLayerID, false);
                } else {
                    this.b.getBaseMap().ShowLayers(overlay.mLayerID, true);
                }
                this.b.getBaseMap().UpdateLayers(overlay.mLayerID);
                itemizedOverlay.a(false);
            }
        }
        MapController mapController = this.b;
        if (mapController == null || mapController.getBaseMap() == null) {
            return;
        }
        this.b.getBaseMap().UpdateLayers(overlay.mLayerID);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean removeOverlay(Overlay overlay) {
        MapController mapController;
        AppBaseMap baseMap;
        if (overlay == null || (mapController = this.b) == null || (baseMap = mapController.getBaseMap()) == null) {
            return false;
        }
        baseMap.ClearLayer(overlay.mLayerID);
        baseMap.ShowLayers(overlay.mLayerID, false);
        baseMap.UpdateLayers(overlay.mLayerID);
        baseMap.RemoveLayer(overlay.mLayerID);
        if (overlay instanceof ItemizedOverlay) {
            this.h.remove(overlay);
        } else if (overlay instanceof InnerOverlay) {
            this.h.remove(overlay);
            this.c.a(overlay);
        }
        overlay.mLayerID = 0L;
        return true;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void saveScreenToLocal(String str) {
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setBaseIndoorMap(boolean z) {
    }

    public void setBaseMap(com.baidu.mapsdkplatform.comapi.map.c cVar) {
        this.a = cVar;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setGeoRound(MapStatus.GeoBound geoBound) {
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setMapCenter(GeoPoint geoPoint) {
        MapController mapController = this.b;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.centerPtX = geoPoint.getLongitude();
            mapStatus.centerPtY = geoPoint.getLatitude();
            this.b.setMapStatus(mapStatus);
        }
    }

    public void setMapRenderStableListener(ab abVar) {
        this.i = abVar;
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setMapStatus(MapStatus mapStatus) {
        MapController mapController = this.b;
        if (mapController != null) {
            mapController.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setMapTo2D(boolean z) {
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setOverlooking(int i) {
        MapController mapController = this.b;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.overlooking = i;
            this.b.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setRotation(int i) {
        MapController mapController = this.b;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.rotation = i;
            this.b.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setSatellite(boolean z) {
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setStreetRoad(boolean z) {
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setTraffic(boolean z) {
        AppBaseMap baseMap;
        MapController mapController = this.b;
        if (mapController == null || (baseMap = mapController.getBaseMap()) == null) {
            return;
        }
        baseMap.ShowTrafficMap(z);
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setWinRound(MapStatus.WinRound winRound) {
        MapController mapController = this.b;
        if (mapController != null) {
            MapStatus mapStatus = mapController.getMapStatus();
            mapStatus.winRound = winRound;
            this.b.setMapStatus(mapStatus);
        }
    }

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public void setZoomLevel(float f) {
        if (this.b == null) {
            return;
        }
        int i = 21;
        if (getController().getFocusedBaseIndoorMapInfo() != null) {
            i = 22;
        }
        if (f < 4.0f) {
            f = 4.0f;
        } else if (f > i) {
            f = 21.0f;
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

    @Override // com.baidu.platform.comapi.map.MapViewInterface
    public boolean switchOverlay(Overlay overlay, Overlay overlay2) {
        MapController mapController;
        AppBaseMap baseMap;
        if (overlay == null || overlay2 == null || (mapController = this.b) == null || (baseMap = mapController.getBaseMap()) == null) {
            return false;
        }
        return baseMap.SwitchLayer(overlay.mLayerID, overlay2.mLayerID);
    }

    public void unListenMapRenderMessage() {
        this.l = null;
    }
}
