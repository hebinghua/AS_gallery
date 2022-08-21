package com.baidu.mapsdkplatform.comapi.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapLayer;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.LocationOverlay;
import com.baidu.platform.comapi.map.MapController;
import com.baidu.platform.comapi.map.MapSurfaceView;
import com.baidu.platform.comapi.map.MapTextureView;
import com.baidu.platform.comapi.map.MapViewInterface;
import com.baidu.platform.comapi.map.OverlayLocationData;
import com.baidu.platform.comapi.map.ak;
import com.baidu.platform.comapi.util.SysOSUtil;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class c implements com.baidu.mapsdkplatform.comjni.a.a.a {
    private static int D = 0;
    private static int E = 0;
    public static float d = 1096.0f;
    public static long j = 0;
    private static final String l = "c";
    private z A;
    private k B;
    private l C;
    private int F;
    private int G;
    private MapController Q;
    private LocationOverlay R;
    private com.baidu.platform.comapi.map.d S;
    public AppBaseMap i;
    private boolean m;
    private boolean n;
    private ab v;
    private aa w;
    private Context x;
    private List<b> y;
    private HashMap<MapLayer, b> z;
    public float a = 21.0f;
    public float b = 4.0f;
    public float c = 21.0f;
    private boolean o = true;
    private boolean p = false;
    private boolean q = false;
    private boolean r = false;
    private boolean s = true;
    public boolean e = true;
    public boolean f = true;
    public boolean g = false;
    private boolean t = true;
    private boolean u = false;
    private boolean H = false;
    private boolean I = false;
    private long J = 0;
    private long K = 0;
    private boolean L = false;
    private Queue<a> M = new LinkedList();
    public MapStatusUpdate k = null;
    private boolean N = false;
    private boolean O = false;
    private boolean P = false;
    private boolean T = false;
    public List<ak> h = new CopyOnWriteArrayList();

    /* loaded from: classes.dex */
    public static class a {
        public Bundle a;

        public a(Bundle bundle) {
            this.a = bundle;
        }
    }

    public c(Context context, MapSurfaceView mapSurfaceView, u uVar, String str, int i) {
        this.x = context;
        MapController mapController = new MapController();
        this.Q = mapController;
        mapController.initAppBaseMap();
        a(this.Q);
        mapSurfaceView.setMapController(this.Q);
        this.i = this.Q.getBaseMap();
        a("com.baidu.platform.comapi.wnplatform.walkmap.WNaviBaiduMap", "setId", this.Q.getMapId());
        R();
        a(uVar);
        this.Q.getBaseMap().SetSDKLayerCallback(this);
        this.Q.onResume();
    }

    public c(Context context, MapTextureView mapTextureView, u uVar, String str, int i) {
        this.x = context;
        MapController mapController = new MapController();
        this.Q = mapController;
        mapController.initAppBaseMap();
        a(this.Q);
        mapTextureView.attachBaseMapController(this.Q);
        this.i = this.Q.getBaseMap();
        R();
        this.i = this.Q.getBaseMap();
        a(uVar);
        this.Q.getBaseMap().SetSDKLayerCallback(this);
        this.Q.onResume();
    }

    private void P() {
        try {
            D = (int) (SysOSUtil.getInstance().getDensity() * 40.0f);
            E = (int) (SysOSUtil.getInstance().getDensity() * 40.0f);
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("x", D);
            jSONObject2.put("y", D);
            jSONObject2.put("hidetime", 1000);
            jSONArray.put(jSONObject2);
            jSONObject.put("dataset", jSONArray);
            com.baidu.platform.comapi.map.d dVar = this.S;
            if (dVar == null) {
                return;
            }
            dVar.setData(jSONObject.toString());
            this.S.UpdateOverlay();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void Q() {
        if (!this.q && !this.n && !this.m && !this.r) {
            float f = this.c;
            this.a = f;
            MapController mapController = this.Q;
            if (mapController == null) {
                return;
            }
            mapController.mMaxZoomLevel = f;
            return;
        }
        if (this.a > 20.0f) {
            this.a = 20.0f;
            MapController mapController2 = this.Q;
            if (mapController2 != null) {
                mapController2.mMaxZoomLevel = 20.0f;
            }
        }
        if (C().a <= 20.0f) {
            return;
        }
        w C = C();
        C.a = 20.0f;
        a(C);
    }

    private void R() {
        this.y = new CopyOnWriteArrayList();
        this.z = new HashMap<>();
        z zVar = new z();
        this.A = zVar;
        a(zVar);
        this.z.put(MapLayer.MAP_LAYER_OVERLAY, this.A);
        n(false);
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            appBaseMap.setDEMEnable(false);
        }
    }

    private void S() {
        MapController mapController = this.Q;
        if (mapController != null && !mapController.mIsMoving) {
            mapController.mIsMoving = true;
            mapController.mIsAnimating = false;
            if (this.h == null) {
                return;
            }
            w C = C();
            for (int i = 0; i < this.h.size(); i++) {
                ak akVar = this.h.get(i);
                if (akVar != null) {
                    akVar.a(C);
                }
            }
        }
    }

    private long a(MapLayer mapLayer) {
        AppBaseMap appBaseMap;
        String str;
        if (this.i == null) {
            return -1L;
        }
        int i = d.a[mapLayer.ordinal()];
        if (i == 1) {
            LocationOverlay locationOverlay = this.R;
            if (locationOverlay == null) {
                return -1L;
            }
            return locationOverlay.mLayerID;
        } else if (i == 2) {
            z zVar = this.A;
            if (zVar == null) {
                return -1L;
            }
            return zVar.a;
        } else {
            if (i == 3) {
                appBaseMap = this.i;
                str = "poiindoormarklayer";
            } else if (i != 4) {
                return -1L;
            } else {
                appBaseMap = this.i;
                str = "basepoi";
            }
            return appBaseMap.getLayerIDByTag(str);
        }
    }

    private void a(b bVar) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        bVar.a = appBaseMap.AddLayer(bVar.c, bVar.d, bVar.b);
        synchronized (this.y) {
            this.y.add(bVar);
        }
    }

    private void a(u uVar) {
        if (uVar == null) {
            uVar = new u();
        }
        w wVar = uVar.a;
        boolean z = uVar.f;
        this.s = z;
        this.t = uVar.d;
        this.e = uVar.e;
        this.f = uVar.g;
        x(z);
        w(this.t);
        r(this.e);
        s(this.f);
        this.i.SetMapStatus(wVar.a(this));
        this.i.SetMapControlMode(t.DEFAULT.ordinal());
        boolean z2 = uVar.b;
        this.o = z2;
        if (z2) {
            if (this.S == null) {
                this.S = new com.baidu.platform.comapi.map.d(this.i);
                MapViewInterface mapView = this.Q.getMapView();
                if (mapView != null) {
                    mapView.addOverlay(this.S);
                    P();
                }
            }
            this.i.ShowLayers(this.S.mLayerID, true);
            this.i.ResetImageRes();
        }
        int i = uVar.c;
        if (i == 2) {
            a(true);
        }
        if (i == 3) {
            if (A()) {
                y(false);
            }
            if (B()) {
                z(false);
            }
            i(false);
            n(false);
        }
    }

    private void a(MapController mapController) {
        if (!com.baidu.platform.comapi.b.a()) {
            synchronized (com.baidu.platform.comapi.b.class) {
            }
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("level", 12.0d);
        bundle.putDouble("centerptx", 1.295815798E7d);
        bundle.putDouble("centerpty", 4825999.74d);
        bundle.putDouble("centerptz", SearchStatUtils.POW);
        bundle.putInt("left", 0);
        bundle.putInt("top", 0);
        int screenHeight = SysOSUtil.getInstance().getScreenHeight();
        bundle.putInt("right", SysOSUtil.getInstance().getScreenWidth());
        bundle.putInt("bottom", screenHeight);
        bundle.putString("modulePath", SysOSUtil.getInstance().getOutputDirPath());
        bundle.putString("appSdcardPath", SysOSUtil.getInstance().getExternalFilesDir());
        bundle.putString("appCachePath", SysOSUtil.getInstance().getOutputCache());
        bundle.putString("appSecondCachePath", SysOSUtil.getInstance().getOutputCache());
        bundle.putInt("mapTmpMax", EnvironmentUtilities.getMapTmpStgMax());
        bundle.putInt("domTmpMax", EnvironmentUtilities.getDomTmpStgMax());
        bundle.putInt("itsTmpMax", EnvironmentUtilities.getItsTmpStgMax());
        bundle.putInt("ssgTmpMax", EnvironmentUtilities.getSsgTmpStgMax());
        mapController.initMapResources(bundle);
    }

    private void a(String str, String str2, long j2) {
        try {
            Class<?> cls = Class.forName(str);
            cls.getMethod(str2, Long.TYPE).invoke(cls.newInstance(), Long.valueOf(j2));
        } catch (Exception unused) {
        }
    }

    private boolean f(Bundle bundle) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return false;
        }
        return appBaseMap.addSDKTileData(bundle);
    }

    private boolean g(Bundle bundle) {
        AppBaseMap appBaseMap;
        if (bundle == null || (appBaseMap = this.i) == null) {
            return false;
        }
        boolean updateSDKTile = appBaseMap.updateSDKTile(bundle);
        if (updateSDKTile) {
            h(updateSDKTile);
            this.i.UpdateLayers(this.v.a);
        }
        return updateSDKTile;
    }

    public boolean A() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            return appBaseMap.LayersIsShow(appBaseMap.getLayerIDByTag("basepoi"));
        }
        return false;
    }

    public boolean B() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            return appBaseMap.LayersIsShow(appBaseMap.getLayerIDByTag("poiindoormarklayer"));
        }
        return false;
    }

    public w C() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return null;
        }
        Bundle GetMapStatus = appBaseMap.GetMapStatus();
        w wVar = new w();
        wVar.a(GetMapStatus);
        return wVar;
    }

    public LatLngBounds D() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return null;
        }
        Bundle mapStatusLimits = appBaseMap.getMapStatusLimits();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int i = mapStatusLimits.getInt("maxCoorx");
        int i2 = mapStatusLimits.getInt("minCoorx");
        builder.include(CoordUtil.mc2ll(new GeoPoint(mapStatusLimits.getInt("minCoory"), i))).include(CoordUtil.mc2ll(new GeoPoint(mapStatusLimits.getInt("maxCoory"), i2)));
        return builder.build();
    }

    public MapStatusUpdate E() {
        return this.k;
    }

    public int F() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            appBaseMap.getFontSizeLevel();
            return 1;
        }
        return 1;
    }

    public int G() {
        return this.F;
    }

    public int H() {
        return this.G;
    }

    public w I() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return null;
        }
        Bundle GetMapStatus = appBaseMap.GetMapStatus(false);
        w wVar = new w();
        wVar.a(GetMapStatus);
        return wVar;
    }

    public double J() {
        return C().m;
    }

    public void K() {
    }

    public float[] L() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return null;
        }
        return appBaseMap.getProjectionMatrix();
    }

    public float[] M() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return null;
        }
        return appBaseMap.getViewMatrix();
    }

    public String N() {
        return this.T ? "" : VersionInfo.MAP_APPROVAL_NUMBER;
    }

    public int O() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return 0;
        }
        return appBaseMap.getMapLanguage();
    }

    public float a(int i, int i2, int i3, int i4, int i5, int i6) {
        if (!this.Q.mIsMapLoadFinish) {
            return 12.0f;
        }
        if (this.i == null) {
            return 0.0f;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("left", i);
        bundle.putInt("right", i3);
        bundle.putInt("bottom", i4);
        bundle.putInt("top", i2);
        bundle.putInt("hasHW", 1);
        bundle.putInt(nexExportFormat.TAG_FORMAT_WIDTH, i5);
        bundle.putInt(nexExportFormat.TAG_FORMAT_HEIGHT, i6);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("left", 0);
        bundle2.putInt("bottom", i6);
        bundle2.putInt("right", i5);
        bundle2.putInt("top", 0);
        return this.i.GetFZoomToBoundF(bundle, bundle2);
    }

    @Override // com.baidu.mapsdkplatform.comjni.a.a.a, com.baidu.platform.comjni.map.basemap.a
    public int a(Bundle bundle, long j2, int i) {
        y yVar;
        k kVar = this.B;
        if (kVar == null || j2 != kVar.a) {
            ab abVar = this.v;
            if (abVar == null || j2 != abVar.a) {
                return 0;
            }
            bundle.putBundle("param", this.w.a(bundle.getInt("x"), bundle.getInt("y"), bundle.getInt("zoom"), this.x));
            yVar = this.v;
        } else {
            bundle.putBundle("param", this.C.a(bundle.getInt("x"), bundle.getInt("y"), bundle.getInt("zoom")));
            yVar = this.B;
        }
        return yVar.e;
    }

    public Point a(GeoPoint geoPoint) {
        com.baidu.platform.comapi.basestruct.Point pixels = this.Q.getMapView().getProjection().toPixels(geoPoint, null);
        return pixels != null ? new Point(pixels.getIntX(), pixels.getIntY()) : new Point();
    }

    public AppBaseMap a() {
        return this.i;
    }

    public void a(float f, float f2) {
        this.a = f;
        this.c = f;
        this.b = f2;
        MapController mapController = this.Q;
        if (mapController != null) {
            mapController.setMaxAndMinZoomLevel(f, f2);
        }
        if (this.i != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("maxLevel", (int) f);
            bundle.putInt("minLevel", (int) f2);
            this.i.setMaxAndMinZoomLevel(bundle);
        }
    }

    public void a(int i) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.CleanCache(i);
    }

    public void a(int i, int i2) {
        this.F = i;
        this.G = i2;
    }

    public void a(long j2, long j3, long j4, long j5, boolean z) {
    }

    public void a(Bitmap bitmap) {
        Bundle bundle;
        if (this.i == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, 0);
            jSONObject2.put("x", D);
            jSONObject2.put("y", E);
            jSONObject2.put("hidetime", 1000);
            jSONArray.put(jSONObject2);
            jSONObject.put("dataset", jSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            bundle = null;
        } else {
            Bundle bundle2 = new Bundle();
            Bundle bundle3 = new Bundle();
            ByteBuffer allocate = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
            bitmap.copyPixelsToBuffer(allocate);
            bundle3.putByteArray("imgData", allocate.array());
            bundle3.putString("imgKey", bitmap.hashCode() + "_" + System.currentTimeMillis());
            bundle3.putInt("imgH", bitmap.getHeight());
            bundle3.putInt("imgW", bitmap.getWidth());
            bundle3.putInt("hasIcon", 1);
            bundle2.putBundle("iconData", bundle3);
            bundle = bundle2;
        }
        if (this.S == null) {
            return;
        }
        if (!TextUtils.isEmpty(jSONObject.toString())) {
            this.S.setData(jSONObject.toString());
        }
        if (bundle != null) {
            this.S.setParam(bundle);
        }
        this.S.UpdateOverlay();
    }

    public void a(MapLayer mapLayer, MapLayer mapLayer2) {
        if (this.i == null) {
            return;
        }
        long a2 = a(mapLayer);
        long a3 = a(mapLayer2);
        if (a2 == -1 || a3 == -1) {
            return;
        }
        this.i.SwitchLayer(a2, a3);
    }

    public void a(MapLayer mapLayer, boolean z) {
        if (this.i == null) {
            return;
        }
        long a2 = a(mapLayer);
        if (a2 == -1) {
            return;
        }
        this.i.SetLayersClickable(a2, z);
    }

    public void a(MapStatusUpdate mapStatusUpdate) {
        this.k = mapStatusUpdate;
    }

    public void a(LatLngBounds latLngBounds) {
        if (latLngBounds == null || this.i == null) {
            return;
        }
        LatLng latLng = latLngBounds.northeast;
        LatLng latLng2 = latLngBounds.southwest;
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        GeoPoint ll2mc2 = CoordUtil.ll2mc(latLng2);
        int longitudeE6 = (int) ll2mc2.getLongitudeE6();
        int latitudeE6 = (int) ll2mc.getLatitudeE6();
        Bundle bundle = new Bundle();
        bundle.putInt("maxCoorx", (int) ll2mc.getLongitudeE6());
        bundle.putInt("minCoory", (int) ll2mc2.getLatitudeE6());
        bundle.putInt("minCoorx", longitudeE6);
        bundle.putInt("maxCoory", latitudeE6);
        this.i.setMapStatusLimits(bundle);
    }

    public void a(aa aaVar) {
        this.w = aaVar;
    }

    public void a(l lVar) {
        this.C = lVar;
    }

    public void a(w wVar) {
        if (this.i == null || wVar == null) {
            return;
        }
        Bundle a2 = wVar.a(this);
        a2.putInt("animation", 0);
        a2.putInt("animatime", 0);
        S();
        this.i.SetMapStatus(a2);
    }

    public void a(w wVar, int i) {
        if (this.i == null || wVar == null) {
            return;
        }
        Bundle a2 = wVar.a(this);
        a2.putInt("animation", 1);
        a2.putInt("animatime", i);
        if (this.L) {
            this.M.add(new a(a2));
            return;
        }
        z();
        this.i.SetMapStatus(a2);
    }

    public void a(ak akVar) {
        if (akVar == null || this.h == null) {
            return;
        }
        this.Q.registMapViewListener(akVar);
        this.h.add(akVar);
    }

    public void a(String str, Bundle bundle) {
        LocationOverlay locationOverlay = this.R;
        if (locationOverlay == null) {
            return;
        }
        locationOverlay.setData(str);
        this.R.setParam(bundle);
        this.R.UpdateOverlay();
    }

    public void a(List<OverlayLocationData> list) {
        LocationOverlay locationOverlay = this.R;
        if (locationOverlay == null) {
            return;
        }
        locationOverlay.setLocationLayerData(list);
    }

    public void a(boolean z) {
        int i;
        Bundle bundle;
        if (this.i == null) {
            return;
        }
        this.n = z;
        Q();
        this.i.ShowSatelliteMap(this.n);
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        if (z) {
            i = 2;
            bundle = new Bundle();
        } else {
            i = 1;
            bundle = new Bundle();
        }
        mapController.setMapTheme(i, bundle);
    }

    public void a(Bundle[] bundleArr) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null || bundleArr == null) {
            return;
        }
        appBaseMap.addOverlayItems(bundleArr, bundleArr.length);
    }

    @Override // com.baidu.mapsdkplatform.comjni.a.a.a, com.baidu.platform.comjni.map.basemap.a
    public boolean a(long j2) {
        synchronized (this.y) {
            Iterator<b> it = this.y.iterator();
            while (it.hasNext()) {
                if (it.next().a == j2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean a(Point point) {
        int i;
        int i2;
        if (point != null && this.i != null && (i = point.x) >= 0 && (i2 = point.y) >= 0) {
            D = i;
            E = i2;
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("x", D);
                jSONObject2.put("y", E);
                jSONObject2.put("hidetime", 1000);
                jSONArray.put(jSONObject2);
                jSONObject.put("dataset", jSONArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (this.S != null) {
                if (!TextUtils.isEmpty(jSONObject.toString())) {
                    this.S.setData(jSONObject.toString());
                }
                this.S.UpdateOverlay();
                return true;
            }
        }
        return false;
    }

    public boolean a(Bundle bundle) {
        if (this.i == null) {
            return false;
        }
        ab abVar = new ab();
        this.v = abVar;
        long AddLayer = this.i.AddLayer(abVar.c, abVar.d, abVar.b);
        if (AddLayer != 0) {
            this.v.a = AddLayer;
            synchronized (this.y) {
                this.y.add(this.v);
            }
            bundle.putLong("sdktileaddr", AddLayer);
            if (f(bundle) && g(bundle)) {
                return true;
            }
        }
        return false;
    }

    public boolean a(String str, String str2) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return false;
        }
        return appBaseMap.SwitchBaseIndoorMapFloor(str, str2);
    }

    public float b() {
        MapController mapController = this.Q;
        return mapController != null ? mapController.mMaxZoomLevel : this.a;
    }

    public GeoPoint b(int i, int i2) {
        return this.Q.getMapView().getProjection().fromPixels(i, i2);
    }

    public void b(int i) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            appBaseMap.setFontSizeLevel(i);
        }
    }

    public void b(Bundle bundle) {
        if (this.i == null) {
            return;
        }
        e(bundle);
        this.i.addOneOverlayItem(bundle);
    }

    public void b(String str, String str2) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.initCustomStyle(str, str2);
    }

    public void b(boolean z) {
        this.u = z;
    }

    public void b(Bundle[] bundleArr) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.removeOverlayItems(bundleArr);
    }

    public void c() {
        if (this.i == null) {
            return;
        }
        synchronized (this.y) {
            for (b bVar : this.y) {
                this.i.ShowLayers(bVar.a, false);
            }
        }
    }

    public void c(int i) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.setMapLanguage(i);
    }

    public void c(Bundle bundle) {
        if (this.i == null) {
            return;
        }
        e(bundle);
        this.i.updateOneOverlayItem(bundle);
    }

    public void c(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        this.i.ShowLayers(appBaseMap.getLayerIDByTag("opgrid"), z);
    }

    public void d() {
        if (this.i == null) {
            return;
        }
        synchronized (this.y) {
            for (b bVar : this.y) {
                if (!(bVar instanceof com.baidu.mapsdkplatform.comapi.map.a) && !(bVar instanceof k)) {
                    this.i.ShowLayers(bVar.a, true);
                }
                this.i.ShowLayers(bVar.a, false);
            }
        }
        this.i.ShowTrafficMap(false);
    }

    public void d(Bundle bundle) {
        if (this.i == null) {
            return;
        }
        e(bundle);
        this.i.removeOneOverlayItem(bundle);
    }

    public void d(boolean z) {
        boolean z2;
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        if (z) {
            if (this.N) {
                return;
            }
            appBaseMap.SwitchLayer(appBaseMap.getLayerIDByTag("indoorlayer"), this.A.a);
            z2 = true;
        } else if (!this.N) {
            return;
        } else {
            appBaseMap.SwitchLayer(this.A.a, appBaseMap.getLayerIDByTag("indoorlayer"));
            z2 = false;
        }
        this.N = z2;
    }

    public void e() {
        ab abVar;
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null || (abVar = this.v) == null) {
            return;
        }
        appBaseMap.RemoveLayer(abVar.a);
        this.y.remove(this.v);
    }

    public void e(Bundle bundle) {
        int i;
        int i2;
        if (bundle.get("param") == null ? (i = bundle.getInt(nexExportFormat.TAG_FORMAT_TYPE)) != h.ground.ordinal() && i < h.arc.ordinal() : (i2 = (bundle = (Bundle) bundle.get("param")).getInt(nexExportFormat.TAG_FORMAT_TYPE)) != h.ground.ordinal() && i2 < h.arc.ordinal()) {
            h.popup.ordinal();
        }
        bundle.putLong("layer_addr", this.A.a);
    }

    public void e(boolean z) {
        LocationOverlay locationOverlay;
        boolean z2;
        LocationOverlay locationOverlay2;
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        if (z) {
            if (this.O || (locationOverlay2 = this.R) == null) {
                return;
            }
            appBaseMap.SwitchLayer(this.A.a, locationOverlay2.mLayerID);
            z2 = true;
        } else if (!this.O || (locationOverlay = this.R) == null) {
            return;
        } else {
            appBaseMap.SwitchLayer(locationOverlay.mLayerID, this.A.a);
            z2 = false;
        }
        this.O = z2;
    }

    public boolean f() {
        AppBaseMap appBaseMap;
        ab abVar = this.v;
        if (abVar == null || (appBaseMap = this.i) == null) {
            return false;
        }
        return appBaseMap.cleanSDKTileDataCache(abVar.a);
    }

    public boolean f(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return false;
        }
        long layerIDByTag = appBaseMap.getLayerIDByTag("routeicon");
        long layerIDByTag2 = this.i.getLayerIDByTag(MapController.ANDROID_SDK_LAYER_TAG);
        if (layerIDByTag == 0 || layerIDByTag2 == 0) {
            return false;
        }
        if (z) {
            if (this.P) {
                return false;
            }
            boolean SwitchLayer = this.i.SwitchLayer(layerIDByTag, layerIDByTag2);
            this.P = true;
            return SwitchLayer;
        } else if (!this.P) {
            return false;
        } else {
            boolean SwitchLayer2 = this.i.SwitchLayer(layerIDByTag2, layerIDByTag);
            this.P = false;
            return SwitchLayer2;
        }
    }

    public void g(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        if (this.S == null) {
            this.S = new com.baidu.platform.comapi.map.d(appBaseMap);
            MapViewInterface mapView = this.Q.getMapView();
            if (mapView != null) {
                mapView.addOverlay(this.S);
                P();
            }
        }
        this.i.ShowLayers(this.S.mLayerID, z);
    }

    public boolean g() {
        return this.m;
    }

    public String h() {
        return null;
    }

    public void h(boolean z) {
        ab abVar;
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null || (abVar = this.v) == null) {
            return;
        }
        appBaseMap.ShowLayers(abVar.a, z);
    }

    public void i(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.ShowLayers(appBaseMap.getLayerIDByTag("basemap"), z);
    }

    public boolean i() {
        return this.r;
    }

    public void j(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        this.r = z;
        appBaseMap.ShowHotMap(z, 0);
    }

    public boolean j() {
        return false;
    }

    public void k(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        this.m = z;
        appBaseMap.ShowTrafficMap(z);
    }

    public boolean k() {
        return this.n;
    }

    public void l(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.setDrawHouseHeightEnable(z);
    }

    public boolean l() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return false;
        }
        return appBaseMap.LayersIsShow(appBaseMap.getLayerIDByTag("basemap"));
    }

    public void m(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        this.o = z;
        if (this.S == null) {
            this.S = new com.baidu.platform.comapi.map.d(appBaseMap);
            MapViewInterface mapView = this.Q.getMapView();
            if (mapView != null) {
                mapView.addOverlay(this.S);
                P();
            }
        }
        this.i.ShowLayers(this.S.mLayerID, z);
    }

    public boolean m() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return false;
        }
        return appBaseMap.getDrawHouseHeightEnable();
    }

    public void n() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        appBaseMap.ClearSDKLayer(this.A.a);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001a, code lost:
        if (r2 != null) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x001c, code lost:
        r2.mMaxZoomLevel = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x000f, code lost:
        if (r2 != null) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void n(boolean r4) {
        /*
            r3 = this;
            com.baidu.platform.comjni.map.basemap.AppBaseMap r0 = r3.i
            if (r0 != 0) goto L5
            return
        L5:
            if (r4 == 0) goto L12
            r1 = 1102053376(0x41b00000, float:22.0)
            r3.a = r1
            r3.c = r1
            com.baidu.platform.comapi.map.MapController r2 = r3.Q
            if (r2 == 0) goto L1e
            goto L1c
        L12:
            r1 = 1101529088(0x41a80000, float:21.0)
            r3.a = r1
            r3.c = r1
            com.baidu.platform.comapi.map.MapController r2 = r3.Q
            if (r2 == 0) goto L1e
        L1c:
            r2.mMaxZoomLevel = r1
        L1e:
            r0.ShowBaseIndoorMap(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapsdkplatform.comapi.map.c.n(boolean):void");
    }

    public void o() {
        k kVar;
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null || (kVar = this.B) == null) {
            return;
        }
        appBaseMap.clearHeatMapLayerCache(kVar.a);
        this.i.UpdateLayers(this.B.a);
    }

    public void o(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        this.T = z;
        appBaseMap.setCustomStyleEnable(z);
        if (!OpenLogUtil.isMapLogEnable()) {
            return;
        }
        com.baidu.mapsdkplatform.comapi.commonutils.b a2 = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
        a2.a("CustomMap setMapCustomEnable enable = " + z);
    }

    public MapBaseIndoorMapInfo p() {
        String GetFocusedBaseIndoorMapInfo;
        String str;
        String str2;
        String str3 = "";
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null || (GetFocusedBaseIndoorMapInfo = appBaseMap.GetFocusedBaseIndoorMapInfo()) == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(1);
        try {
            JSONObject jSONObject = new JSONObject(GetFocusedBaseIndoorMapInfo);
            str2 = jSONObject.optString("focusindoorid");
            try {
                str3 = jSONObject.optString("curfloor");
                JSONArray optJSONArray = jSONObject.optJSONArray("floorlist");
                if (optJSONArray != null) {
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        arrayList.add(optJSONArray.get(i).toString());
                    }
                }
            } catch (JSONException e) {
                e = e;
                str = str3;
                str3 = str2;
                e.printStackTrace();
                String str4 = str;
                str2 = str3;
                str3 = str4;
                return new MapBaseIndoorMapInfo(str2, str3, arrayList);
            }
        } catch (JSONException e2) {
            e = e2;
            str = str3;
        }
        return new MapBaseIndoorMapInfo(str2, str3, arrayList);
    }

    public void p(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return;
        }
        this.p = z;
        LocationOverlay locationOverlay = this.R;
        if (locationOverlay != null) {
            appBaseMap.ShowLayers(locationOverlay.mLayerID, z);
            return;
        }
        MapViewInterface mapView = this.Q.getMapView();
        if (mapView == null) {
            return;
        }
        LocationOverlay locationOverlay2 = new LocationOverlay(this.i);
        this.R = locationOverlay2;
        mapView.addOverlay(locationOverlay2);
    }

    public void q(boolean z) {
        if (this.i == null) {
            return;
        }
        if (this.B == null) {
            k kVar = new k();
            this.B = kVar;
            a(kVar);
        }
        this.q = z;
        this.i.ShowLayers(this.B.a, z);
    }

    public boolean q() {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null) {
            return false;
        }
        return appBaseMap.IsBaseIndoorMapMode();
    }

    public void r(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.setCanTouchMove(z);
        this.e = z;
    }

    public boolean r() {
        return this.o;
    }

    public void s() {
        this.R.clearLocationLayerData(null);
    }

    public void s(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.setEnableZoom(z);
        this.f = z;
    }

    public void t(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.setDoubleClickZoom(z);
    }

    public boolean t() {
        return this.p;
    }

    public void u() {
        k kVar;
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap == null || (kVar = this.B) == null) {
            return;
        }
        appBaseMap.UpdateLayers(kVar.a);
    }

    public void u(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.setTwoTouchClickZoomEnabled(z);
    }

    public void v(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.setEnlargeCenterWithDoubleClickEnable(z);
    }

    public boolean v() {
        return this.e;
    }

    public void w(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.set3DGestureEnable(z);
        this.t = z;
    }

    public boolean w() {
        return this.f;
    }

    public void x(boolean z) {
        MapController mapController = this.Q;
        if (mapController == null) {
            return;
        }
        mapController.setOverlookGestureEnable(z);
        this.s = z;
    }

    public boolean x() {
        return this.t;
    }

    public void y(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            appBaseMap.ShowLayers(appBaseMap.getLayerIDByTag("basepoi"), z);
        }
    }

    public boolean y() {
        return this.s;
    }

    public void z() {
        MapController mapController = this.Q;
        if (mapController != null && !mapController.mIsMoving && !mapController.mIsAnimating) {
            mapController.mIsAnimating = true;
            if (this.h == null) {
                return;
            }
            w C = C();
            for (int i = 0; i < this.h.size(); i++) {
                ak akVar = this.h.get(i);
                if (akVar != null) {
                    akVar.a(C);
                }
            }
        }
    }

    public void z(boolean z) {
        AppBaseMap appBaseMap = this.i;
        if (appBaseMap != null) {
            appBaseMap.ShowLayers(appBaseMap.getLayerIDByTag("poiindoormarklayer"), z);
        }
    }
}
