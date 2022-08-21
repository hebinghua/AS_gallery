package com.baidu.platform.comapi.map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import ch.qos.logback.classic.Level;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.Point;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.baidu.platform.comapi.map.MapStatus;
import com.baidu.platform.comapi.util.SysOSUtil;
import com.baidu.platform.comjni.engine.MessageProxy;
import com.baidu.platform.comjni.map.basemap.AppBaseMap;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexCrop;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MapController {
    public static final String ANDROID_SDK_LAYER_TAG = "android_sdk";
    public static final String CITY_AREA_TAG = "cityarea";
    public static final String COMPASS_LAYER_TAG = "compass";
    public static final String DEFAULT_LAYER_TAG = "default";
    public static final String DYNAMIC_MAP_LAYER_TAG = "dynamicmap";
    public static final String FOOTSURFACE_LAYER_TAG = "footsurface";
    public static final String HEATMAP_LAYER_TAG = "heatmap";
    public static final String ITEM_LAYER_TAG = "item";
    public static final String ITSROUTE_LAYER_TAG = "itsroute";
    public static final String LOCAL_LIMIT_MAP_LAYER_TAG = "dynamiclimit";
    public static final String LOCATION_LAYER_TAG = "location";
    public static final int MSG_LONGLINK_CONNECT = 1;
    public static final int MSG_LONGLINK_DISCONNECT = 2;
    private static long O = 0;
    public static final String POISON_LAYER_TAG = "poison";
    public static final String POPUP_LAYER_TAG = "popup";
    public static final String RTPOPUP_LAYER_TAG = "rtpopup";
    public static final String RT_POPUP_LAYER_TAG = "rtpopup";
    public static final String SHARELOCATION_BUBBLE = "smshare";
    public static final String STREETPOPUP_LAYER_TAG = "streetpopup";
    public static final String STREETROUTE_LAYER_TAG = "streetroute";
    private static List<AppBaseMap> T = new ArrayList();
    public static boolean isCompass = false;
    private static final String j = "MapController";
    public static boolean mLocIconOnScreen = true;
    public static boolean m_registered_SENSOR_ORIENTATION = false;
    private static boolean w = true;
    private static float x;
    private static float y;
    private static boolean z;
    private long M;
    private long U;
    public SoftReference<MapViewInterface> h;
    public NaviMapViewListener i;
    private com.baidu.platform.comapi.map.b.d k;
    public boolean mHasMapObjDraging;
    public boolean mIsMapLoadFinish;
    private MapFirstFrameCallback p;
    private Handler t;
    private boolean l = true;
    public int a = 0;
    private int m = 1;
    private int n = 1;
    private boolean o = false;
    private boolean q = false;
    private AppBaseMap r = null;
    private long s = 0;
    public int nearlyRadius = 20;
    private boolean A = false;
    private boolean B = false;
    private boolean C = false;
    private boolean D = false;
    private a E = new a();
    private boolean F = true;
    private boolean G = false;
    private boolean H = true;
    private boolean I = false;
    private float J = -1.0f;
    private float K = -1.0f;
    private float L = 0.0f;
    private boolean N = false;
    private boolean P = true;
    private boolean Q = true;
    private boolean R = true;
    private boolean S = true;
    public MapViewListener b = null;
    public CaptureMapListener c = null;
    public k d = null;
    public am e = null;
    public MapRenderModeChangeListener f = null;
    public EngineMsgListener g = null;
    public float mMaxZoomLevel = 21.0f;
    public float mMinZoomLevel = 4.0f;
    public boolean mIsMoving = false;
    public boolean mIsAnimating = false;
    private boolean V = false;
    private boolean W = false;
    private com.baidu.platform.comapi.map.b.b X = new com.baidu.platform.comapi.map.b.b(this);
    private MapControlMode Y = MapControlMode.DEFAULT;
    public List<ak> mListeners = new CopyOnWriteArrayList();
    private int u = SysOSUtil.getInstance().getScreenWidth();
    private int v = SysOSUtil.getInstance().getScreenHeight();

    /* loaded from: classes.dex */
    public enum HeatMapType {
        CITY(0),
        SCENERY(1),
        CEMETERY(2);
        
        private final int a;

        HeatMapType(int i) {
            this.a = i;
        }

        public int getId() {
            return this.a;
        }
    }

    /* loaded from: classes.dex */
    public enum MapControlMode {
        DEFAULT(1),
        INDOOR(2),
        STREET(3),
        STREET_WAITING(4);
        
        private final int a;

        MapControlMode(int i) {
            this.a = i;
        }
    }

    /* loaded from: classes.dex */
    public interface MapFirstFrameCallback {
        void onFirstFrameDrawing(MapController mapController);
    }

    /* loaded from: classes.dex */
    public enum MapLayerType {
        DEFAULT(1),
        SATELLITE(2),
        INDOOR(3),
        STREET(5);
        
        private final int a;

        MapLayerType(int i) {
            this.a = i;
        }
    }

    /* loaded from: classes.dex */
    public enum MapSceneMode {
        DEFAULT(0),
        POI(1),
        ROUTE(2),
        INTERNAL(3),
        INDOOR(7);
        
        private final int a;

        MapSceneMode(int i) {
            this.a = i;
        }

        public int getMode() {
            return this.a;
        }
    }

    /* loaded from: classes.dex */
    public enum MapStyleMode {
        DEFAULT(1),
        SEARCH_POI(2),
        SEARCH_ROUTE(3),
        NAV_DAY(4),
        NAV_NIGHT(5),
        WALK_DAY(6),
        INTERNAL(7),
        INTERNAL_SPECIAL(8),
        FOOT_PRINT(9);
        
        private final int a;

        MapStyleMode(int i) {
            this.a = i;
        }

        public int getMode() {
            return this.a;
        }
    }

    /* loaded from: classes.dex */
    public enum RecommendPoiScene {
        BASE(0),
        INTERNATIONAL(1);
        
        public int value;

        RecommendPoiScene(int i) {
            this.value = i;
        }
    }

    /* loaded from: classes.dex */
    public enum RecycleMemoryLevel {
        NORMAL(0),
        FULL(1);
        
        private final int a;

        RecycleMemoryLevel(int i) {
            this.a = i;
        }

        public int getLevel() {
            return this.a;
        }
    }

    /* loaded from: classes.dex */
    public class a {
        public boolean a = false;
        public float b = 0.0f;
        public GeoPoint c;
        public Point d;

        public a() {
        }

        public void a() {
            this.a = false;
            this.b = 0.0f;
            this.c = null;
            this.d = null;
        }
    }

    /* loaded from: classes.dex */
    public class b extends com.baidu.platform.comapi.util.h {
        public b() {
            super(Looper.getMainLooper());
        }

        @Override // com.baidu.platform.comapi.util.h
        public void a(Message message) {
            boolean z;
            NaviMapViewListener naviMapViewListener;
            NaviMapViewListener naviMapViewListener2;
            k kVar;
            CaptureMapListener captureMapListener;
            if (message.what == 4000 && (captureMapListener = MapController.this.c) != null) {
                captureMapListener.onGetCaptureMap(message.arg2 == 1);
            }
            if (message.what == 519 && (kVar = MapController.this.d) != null) {
                kVar.a();
            }
            int i = message.what;
            if (i == 39) {
                if (((Long) message.obj).longValue() != MapController.this.s) {
                    return;
                }
                int i2 = message.arg1;
                if (i2 != 2) {
                    if (i2 == 100) {
                        if (MapController.this.N) {
                            SoftReference<MapViewInterface> softReference = MapController.this.h;
                            if (softReference == null || softReference.get() == null) {
                                return;
                            }
                            com.baidu.platform.comapi.util.i.b().execute(new m(this));
                        }
                        MapController.this.B = false;
                        MapController mapController = MapController.this;
                        mapController.mIsMoving = false;
                        mapController.mIsAnimating = false;
                        if (mapController.getMapViewListener() != null) {
                            MapController.this.getMapViewListener().onMapAnimationFinish();
                        }
                        if (MapController.this.isNaviMode() && (naviMapViewListener = MapController.this.i) != null) {
                            naviMapViewListener.onMapAnimationFinish();
                        }
                        com.baidu.mapsdkplatform.comapi.map.w mapStatusInner = MapController.this.getMapStatusInner();
                        if (MapController.this.mListeners != null) {
                            for (int i3 = 0; i3 < MapController.this.mListeners.size(); i3++) {
                                ak akVar = MapController.this.mListeners.get(i3);
                                if (akVar != null) {
                                    akVar.c(mapStatusInner);
                                }
                            }
                        }
                    } else if (i2 == 200) {
                        MapController.this.mIsMoving = false;
                    } else if (i2 != 300) {
                        MapRenderModeChangeListener mapRenderModeChangeListener = MapController.this.f;
                        if (mapRenderModeChangeListener != null) {
                            mapRenderModeChangeListener.onMapRenderModeChange(i2);
                        }
                        if (MapController.this.isNaviMode() && (naviMapViewListener2 = MapController.this.i) != null) {
                            naviMapViewListener2.onMapRenderModeChange(message.arg1);
                        }
                    } else if (MapController.this.p != null) {
                        MapController.this.p.onFirstFrameDrawing(MapController.this);
                    }
                } else if (MapController.this.mListeners == null) {
                    return;
                } else {
                    for (int i4 = 0; i4 < MapController.this.mListeners.size(); i4++) {
                        ak akVar2 = MapController.this.mListeners.get(i4);
                        if (akVar2 != null) {
                            akVar2.c();
                        }
                    }
                    MapController mapController2 = MapController.this;
                    mapController2.mIsMoving = false;
                    mapController2.mIsAnimating = false;
                }
                MapController mapController3 = MapController.this;
                if (!mapController3.mIsMapLoadFinish && mapController3.v > 0 && MapController.this.u > 0 && MapController.this.getMapView() != null && MapController.this.getMapView().getProjection() != null && MapController.this.getMapView().getProjection().fromPixels(0, 0) != null) {
                    MapController.this.mIsMapLoadFinish = true;
                    com.baidu.platform.comapi.util.i.a(new n(this), 0L);
                }
                if (MapController.this.mListeners != null) {
                    for (int i5 = 0; i5 < MapController.this.mListeners.size(); i5++) {
                        ak akVar3 = MapController.this.mListeners.get(i5);
                        if (akVar3 != null) {
                            akVar3.a();
                        }
                    }
                }
            } else if (i == 41) {
                if (((Long) message.obj).longValue() != MapController.this.s) {
                    return;
                }
                MapController mapController4 = MapController.this;
                if (mapController4.mListeners == null) {
                    return;
                }
                if (mapController4.mIsMoving || mapController4.mIsAnimating) {
                    com.baidu.mapsdkplatform.comapi.map.w mapStatusInner2 = mapController4.getMapStatusInner();
                    for (int i6 = 0; i6 < MapController.this.mListeners.size(); i6++) {
                        ak akVar4 = MapController.this.mListeners.get(i6);
                        if (akVar4 != null) {
                            akVar4.b(mapStatusInner2);
                        }
                    }
                }
            } else if (i == 2082) {
                int i7 = message.arg1;
                if (i7 == 1003) {
                    z = true;
                    i7 = 0;
                } else {
                    z = false;
                }
                if (OpenLogUtil.isMapLogEnable()) {
                    com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("onMapRenderValidFrame isValid = " + z + "; errorCode = " + i7);
                }
                if (MapController.this.mListeners != null) {
                    for (int i8 = 0; i8 < MapController.this.mListeners.size(); i8++) {
                        ak akVar5 = MapController.this.mListeners.get(i8);
                        if (akVar5 != null) {
                            akVar5.a(z, i7);
                        }
                    }
                }
            }
            if (message.what == 512) {
                int i9 = message.arg1;
                if (MapController.this.getMapViewListener() != null) {
                    MapController.this.getMapViewListener().onClickedPopup(i9);
                }
            }
            if (message.what == 50) {
                if (OpenLogUtil.isMapLogEnable()) {
                    com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("EngineMeassage IndoorMap msg.what = " + message.what + "; msg.arg1 = " + message.arg1);
                }
                MapController mapController5 = MapController.this;
                EngineMsgListener engineMsgListener = mapController5.g;
                if (engineMsgListener != null) {
                    int i10 = message.arg1;
                    if (i10 == 1) {
                        MapController.this.g.onEnterIndoorMapMode(mapController5.getFocusedBaseIndoorMapInfo());
                    } else if (i10 == 0) {
                        engineMsgListener.onExitIndoorMapMode();
                    }
                }
                MapController mapController6 = MapController.this;
                if (mapController6.mListeners == null) {
                    return;
                }
                IndoorMapInfo focusedBaseIndoorMapInfo = mapController6.getFocusedBaseIndoorMapInfo();
                for (int i11 = 0; i11 < MapController.this.mListeners.size(); i11++) {
                    ak akVar6 = MapController.this.mListeners.get(i11);
                    if (akVar6 != null) {
                        int i12 = message.arg1;
                        if (i12 != 0) {
                            if (i12 == 1) {
                                if (MapController.this.getMapStatus().level >= 18.0f && focusedBaseIndoorMapInfo != null) {
                                    akVar6.a(true);
                                    MapController.this.mMaxZoomLevel = 22.0f;
                                }
                            }
                        }
                        akVar6.a(false);
                        MapController.this.mMaxZoomLevel = 21.0f;
                    }
                }
            }
            if (message.what == 51) {
                MapController.this.setNetStatus(message.arg1);
            }
            if (message.what == 65301) {
                MapController mapController7 = MapController.this;
                if (mapController7.g == null) {
                    return;
                }
                int i13 = message.arg1;
                if (i13 == 1) {
                    mapController7.getMapBarData();
                } else if (i13 == 0) {
                    com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a());
                }
            }
        }
    }

    public MapController() {
        this.t = null;
        this.t = new b();
        c();
    }

    public static native int CleanAfterDBClick(long j2, float f, float f2);

    public static int GetAdaptKeyCode(int i) {
        switch (i) {
            case 19:
                return 17;
            case 20:
                return 19;
            case 21:
                return 16;
            case 22:
                return 18;
            default:
                return 0;
        }
    }

    public static native int MapProc(long j2, int i, int i2, int i3, int i4, int i5, double d, double d2, double d3, double d4);

    private MapStatus a(boolean z2) {
        Bundle GetMapStatus;
        if (a() && (GetMapStatus = this.r.GetMapStatus(z2)) != null) {
            MapStatus mapStatus = new MapStatus();
            mapStatus.level = (float) GetMapStatus.getDouble("level");
            mapStatus.rotation = (int) GetMapStatus.getDouble(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION);
            mapStatus.overlooking = (int) GetMapStatus.getDouble("overlooking");
            mapStatus.centerPtX = GetMapStatus.getDouble("centerptx");
            mapStatus.centerPtY = GetMapStatus.getDouble("centerpty");
            mapStatus.centerPtZ = GetMapStatus.getDouble("centerptz");
            mapStatus.winRound.left = GetMapStatus.getInt("left");
            mapStatus.winRound.right = GetMapStatus.getInt("right");
            mapStatus.winRound.top = GetMapStatus.getInt("top");
            mapStatus.winRound.bottom = GetMapStatus.getInt("bottom");
            mapStatus.geoRound.left = GetMapStatus.getLong("gleft");
            mapStatus.geoRound.right = GetMapStatus.getLong("gright");
            mapStatus.geoRound.top = GetMapStatus.getLong("gtop");
            mapStatus.geoRound.bottom = GetMapStatus.getLong("gbottom");
            mapStatus.xOffset = GetMapStatus.getFloat("xoffset");
            mapStatus.yOffset = GetMapStatus.getFloat("yoffset");
            boolean z3 = false;
            mapStatus.bfpp = GetMapStatus.getInt("bfpp") == 1;
            mapStatus.panoId = GetMapStatus.getString("panoid");
            mapStatus.streetIndicateAngle = GetMapStatus.getFloat("siangle");
            mapStatus.isBirdEye = GetMapStatus.getInt("isbirdeye") == 1;
            mapStatus.streetExt = GetMapStatus.getInt("ssext");
            mapStatus.roadOffsetX = GetMapStatus.getFloat("roadOffsetX");
            mapStatus.roadOffsetY = GetMapStatus.getFloat("roadOffsetY");
            if (GetMapStatus.getInt("boverlookback") == 1) {
                z3 = true;
            }
            mapStatus.bOverlookSpringback = z3;
            mapStatus.minOverlooking = (int) GetMapStatus.getFloat("minoverlook");
            MapStatus.GeoBound geoBound = mapStatus.geoRound;
            if (geoBound.left <= -20037508) {
                geoBound.left = -20037508L;
            }
            if (geoBound.right >= 20037508) {
                geoBound.right = 20037508L;
            }
            if (geoBound.top >= 20037508) {
                geoBound.top = 20037508L;
            }
            if (geoBound.bottom <= -20037508) {
                geoBound.bottom = -20037508L;
            }
            return mapStatus;
        }
        return new MapStatus();
    }

    private void a(MotionEvent motionEvent) {
        int x2 = (int) motionEvent.getX();
        int y2 = (int) motionEvent.getY();
        x = x2;
        y = y2;
        MapMsgProc(4, 0, x2 | (y2 << 16));
        z = true;
        this.U = motionEvent.getDownTime();
    }

    private boolean a() {
        return this.C && this.r != null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0062, code lost:
        r7 = (org.json.JSONObject) new org.json.JSONObject(r8).getJSONArray("dataset").get(0);
        r8 = r7.getInt("itemindex");
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0079, code lost:
        r2 = r7.optInt("clickindex", -1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007f, code lost:
        r3 = true;
        r4 = r8;
        r7 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0093, code lost:
        r13 = r3;
        r11 = -1;
        r10 = r8;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean a(int r20, int r21, int r22) {
        /*
            r19 = this;
            r0 = r19
            boolean r1 = r19.a()
            r2 = 0
            if (r1 != 0) goto La
            return r2
        La:
            java.lang.ref.SoftReference<com.baidu.platform.comapi.map.MapViewInterface> r1 = r0.h
            if (r1 == 0) goto Lcd
            java.lang.Object r1 = r1.get()
            if (r1 != 0) goto L16
            goto Lcd
        L16:
            java.lang.ref.SoftReference<com.baidu.platform.comapi.map.MapViewInterface> r1 = r0.h
            java.lang.Object r1 = r1.get()
            com.baidu.platform.comapi.map.MapViewInterface r1 = (com.baidu.platform.comapi.map.MapViewInterface) r1
            r3 = 0
            r5 = 1
            r6 = -1
            java.util.List r7 = r1.getOverlays()     // Catch: org.json.JSONException -> L92
            int r7 = r7.size()     // Catch: org.json.JSONException -> L92
            int r7 = r7 - r5
        L2b:
            if (r7 < 0) goto L89
            java.util.List r8 = r1.getOverlays()     // Catch: org.json.JSONException -> L92
            java.lang.Object r8 = r8.get(r7)     // Catch: org.json.JSONException -> L92
            com.baidu.platform.comapi.map.Overlay r8 = (com.baidu.platform.comapi.map.Overlay) r8     // Catch: org.json.JSONException -> L92
            int r9 = r8.mType     // Catch: org.json.JSONException -> L92
            r10 = 27
            if (r9 == r10) goto L3e
            goto L86
        L3e:
            long r3 = r8.mLayerID     // Catch: org.json.JSONException -> L92
            int r8 = r0.nearlyRadius     // Catch: org.json.JSONException -> L92
            double r8 = (double) r8     // Catch: org.json.JSONException -> L92
            double r10 = r19.getZoomUnitsInMeter()     // Catch: org.json.JSONException -> L92
            double r8 = r8 * r10
            int r8 = (int) r8     // Catch: org.json.JSONException -> L92
            com.baidu.platform.comjni.map.basemap.AppBaseMap r11 = r0.r     // Catch: org.json.JSONException -> L92
            if (r11 == 0) goto L86
            r12 = r3
            r14 = r21
            r15 = r22
            r16 = r8
            java.lang.String r8 = r11.GetNearlyObjID(r12, r14, r15, r16)     // Catch: org.json.JSONException -> L92
            if (r8 == 0) goto L86
            java.lang.String r9 = ""
            boolean r9 = r8.equals(r9)     // Catch: org.json.JSONException -> L92
            if (r9 != 0) goto L86
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch: org.json.JSONException -> L92
            r7.<init>(r8)     // Catch: org.json.JSONException -> L92
            java.lang.String r8 = "dataset"
            org.json.JSONArray r7 = r7.getJSONArray(r8)     // Catch: org.json.JSONException -> L92
            java.lang.Object r7 = r7.get(r2)     // Catch: org.json.JSONException -> L92
            org.json.JSONObject r7 = (org.json.JSONObject) r7     // Catch: org.json.JSONException -> L92
            java.lang.String r8 = "itemindex"
            int r8 = r7.getInt(r8)     // Catch: org.json.JSONException -> L92
            java.lang.String r9 = "clickindex"
            int r2 = r7.optInt(r9, r6)     // Catch: org.json.JSONException -> L93
            r17 = r3
            r3 = r5
            r4 = r8
            r7 = r17
            goto L8d
        L86:
            int r7 = r7 + (-1)
            goto L2b
        L89:
            r7 = r3
            r4 = r6
            r3 = r2
            r2 = r4
        L8d:
            r11 = r2
            r2 = r3
            r10 = r4
            r13 = r7
            goto L96
        L92:
            r8 = r6
        L93:
            r13 = r3
            r11 = r6
            r10 = r8
        L96:
            r3 = r20
            if (r3 != r5) goto Lcd
            com.baidu.platform.comapi.map.MapViewListener r3 = r19.getMapViewListener()
            if (r3 == 0) goto Lcd
            com.baidu.platform.comapi.map.MapViewInterface r3 = r19.getMapView()
            if (r3 == 0) goto Lcd
            com.baidu.platform.comapi.map.MapViewInterface r3 = r19.getMapView()
            com.baidu.platform.comapi.map.Projection r3 = r3.getProjection()
            if (r3 == 0) goto Lcd
            com.baidu.platform.comapi.map.Projection r1 = r1.getProjection()
            r3 = r21
            r4 = r22
            com.baidu.platform.comapi.basestruct.GeoPoint r12 = r1.fromPixels(r3, r4)
            if (r11 == r6) goto Lc6
            com.baidu.platform.comapi.map.MapViewListener r9 = r19.getMapViewListener()
            r9.onClickedItem(r10, r11, r12, r13)
            goto Lcd
        Lc6:
            com.baidu.platform.comapi.map.MapViewListener r1 = r19.getMapViewListener()
            r1.onClickedItem(r10, r12, r13)
        Lcd:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.MapController.a(int, int, int):boolean");
    }

    private void b() {
        this.G = false;
        this.L = 0.0f;
        this.J = -1.0f;
        this.K = -1.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0195 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0211 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0245 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0279 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x028a A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x029a A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:153:0x02aa A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x02ba A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02ca A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x02db A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x02ee A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:168:0x02fe A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:171:0x030e A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x031c A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x032a A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:180:0x033a A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x034b A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0369 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0387 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0398 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x03a8 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:198:0x03b8 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x03c8 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:202:0x03d3 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x03de A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:206:0x03e7 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:209:0x03f2 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x03fb A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0406 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x040f A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0419 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0422 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x042c A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0435 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0440 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0449 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0454 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x045d A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0467 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0470 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x047a A[Catch: JSONException -> 0x00fe, TRY_LEAVE, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x048e A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0498 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x04a3 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:248:0x04ac A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:251:0x04b6 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:252:0x04bf A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:255:0x04c9 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x04d5 A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0505  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x067a  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0686  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x072b A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:444:0x073f A[Catch: JSONException -> 0x00fe, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:447:0x074a A[Catch: JSONException -> 0x00fe, TRY_LEAVE, TryCatch #3 {JSONException -> 0x00fe, blocks: (B:50:0x00eb, B:100:0x018f, B:102:0x0195, B:372:0x064b, B:105:0x01b7, B:107:0x01c4, B:111:0x01d1, B:114:0x01d9, B:116:0x01e2, B:118:0x01f0, B:119:0x01f4, B:126:0x0209, B:128:0x0211, B:135:0x0241, B:142:0x0271, B:144:0x0279, B:145:0x0282, B:147:0x028a, B:148:0x0292, B:150:0x029a, B:151:0x02a2, B:153:0x02aa, B:154:0x02b2, B:156:0x02ba, B:157:0x02c2, B:159:0x02ca, B:160:0x02d2, B:162:0x02db, B:163:0x02e4, B:165:0x02ee, B:166:0x02f6, B:168:0x02fe, B:169:0x0306, B:171:0x030e, B:172:0x0316, B:174:0x031c, B:175:0x0322, B:177:0x032a, B:178:0x0332, B:180:0x033a, B:181:0x0342, B:183:0x034b, B:184:0x0360, B:186:0x0369, B:187:0x037e, B:189:0x0387, B:190:0x0390, B:192:0x0398, B:193:0x03a0, B:195:0x03a8, B:196:0x03b0, B:198:0x03b8, B:199:0x03c0, B:201:0x03c8, B:203:0x03d6, B:205:0x03de, B:207:0x03ea, B:209:0x03f2, B:211:0x03fe, B:213:0x0406, B:215:0x0411, B:217:0x0419, B:219:0x0424, B:221:0x042c, B:223:0x0438, B:225:0x0440, B:227:0x044c, B:229:0x0454, B:231:0x045f, B:233:0x0467, B:235:0x0472, B:237:0x047a, B:241:0x0486, B:243:0x048e, B:245:0x049b, B:247:0x04a3, B:249:0x04ae, B:251:0x04b6, B:253:0x04c1, B:255:0x04c9, B:258:0x04d5, B:260:0x04f6, B:371:0x0648, B:302:0x0556, B:308:0x0568, B:321:0x0588, B:323:0x058e, B:411:0x06ba, B:413:0x06c0, B:415:0x06c6, B:416:0x06ce, B:418:0x06d4, B:419:0x06d8, B:420:0x06dc, B:422:0x06e2, B:423:0x06ea, B:425:0x06f0, B:426:0x06f5, B:428:0x06fb, B:429:0x0703, B:431:0x0709, B:432:0x0711, B:436:0x071b, B:437:0x0723, B:438:0x0727, B:439:0x072b, B:441:0x0731, B:442:0x0739, B:444:0x073f, B:445:0x0744, B:447:0x074a, B:328:0x059f, B:330:0x05a5, B:335:0x05b5, B:336:0x05b8, B:338:0x05be, B:341:0x05cc, B:343:0x05d2, B:348:0x05e3, B:351:0x05ed, B:353:0x05f3, B:359:0x060c, B:360:0x0610, B:362:0x0616, B:363:0x061e, B:367:0x0636, B:252:0x04bf, B:248:0x04ac, B:244:0x0498, B:234:0x0470, B:230:0x045d, B:226:0x0449, B:222:0x0435, B:218:0x0422, B:214:0x040f, B:210:0x03fb, B:206:0x03e7, B:202:0x03d3, B:134:0x023a, B:131:0x0228, B:136:0x0245, B:138:0x0251, B:140:0x0259, B:120:0x01f7, B:122:0x01ff, B:115:0x01e0, B:85:0x0134, B:88:0x0145, B:91:0x0158, B:94:0x0162, B:96:0x0176, B:98:0x0182), top: B:458:0x00e9 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean b(int r30, int r31) {
        /*
            Method dump skipped, instructions count: 1898
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.MapController.b(int, int):boolean");
    }

    private void c() {
        MessageProxy.registerMessageHandler(4000, this.t);
        MessageProxy.registerMessageHandler(519, this.t);
        MessageProxy.registerMessageHandler(39, this.t);
        MessageProxy.registerMessageHandler(512, this.t);
        MessageProxy.registerMessageHandler(65297, this.t);
        MessageProxy.registerMessageHandler(UIMsg.MsgDefine.V_WM_VSTREETCLICKBACKGROUND, this.t);
        MessageProxy.registerMessageHandler(50, this.t);
        MessageProxy.registerMessageHandler(51, this.t);
        MessageProxy.registerMessageHandler(65301, this.t);
        MessageProxy.registerMessageHandler(41, this.t);
        MessageProxy.registerMessageHandler(UIMsg.MsgDefine.MSG_MAP_DATA_NET_RESPONSE, this.t);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003b A[LOOP:0: B:18:0x003b->B:24:0x0056, LOOP_START, PHI: r1 
      PHI: (r1v3 int) = (r1v0 int), (r1v4 int) binds: [B:17:0x0039, B:24:0x0056] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0094 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean c(int r9, int r10) {
        /*
            r8 = this;
            boolean r0 = r8.a()
            r1 = 0
            if (r0 != 0) goto L8
            return r1
        L8:
            com.baidu.platform.comjni.map.basemap.AppBaseMap r2 = r8.r
            r3 = -1
            int r7 = r8.nearlyRadius
            r5 = r9
            r6 = r10
            java.lang.String r0 = r2.GetNearlyObjID(r3, r5, r6, r7)
            r2 = 0
            if (r0 == 0) goto L59
            java.lang.String r3 = ""
            boolean r3 = r0.equals(r3)
            if (r3 != 0) goto L59
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: org.json.JSONException -> L32
            r3.<init>(r0)     // Catch: org.json.JSONException -> L32
            java.lang.String r0 = "px"
            r3.put(r0, r9)     // Catch: org.json.JSONException -> L2f
            java.lang.String r9 = "py"
            r3.put(r9, r10)     // Catch: org.json.JSONException -> L2f
            goto L37
        L2f:
            r9 = move-exception
            r2 = r3
            goto L33
        L32:
            r9 = move-exception
        L33:
            r9.printStackTrace()
            r3 = r2
        L37:
            java.util.List<com.baidu.platform.comapi.map.ak> r9 = r8.mListeners
            if (r9 == 0) goto L94
        L3b:
            java.util.List<com.baidu.platform.comapi.map.ak> r9 = r8.mListeners
            int r9 = r9.size()
            if (r1 >= r9) goto L94
            java.util.List<com.baidu.platform.comapi.map.ak> r9 = r8.mListeners
            java.lang.Object r9 = r9.get(r1)
            com.baidu.platform.comapi.map.ak r9 = (com.baidu.platform.comapi.map.ak) r9
            if (r3 == 0) goto L56
            if (r9 == 0) goto L56
            java.lang.String r10 = r3.toString()
            r9.a(r10)
        L56:
            int r1 = r1 + 1
            goto L3b
        L59:
            java.util.List<com.baidu.platform.comapi.map.ak> r0 = r8.mListeners
            if (r0 == 0) goto L94
            com.baidu.platform.comapi.map.MapViewInterface r0 = r8.getMapView()
            if (r0 == 0) goto L93
            com.baidu.platform.comapi.map.MapViewInterface r0 = r8.getMapView()
            com.baidu.platform.comapi.map.Projection r0 = r0.getProjection()
            if (r0 != 0) goto L6e
            goto L93
        L6e:
            com.baidu.platform.comapi.map.MapViewInterface r0 = r8.getMapView()
            com.baidu.platform.comapi.map.Projection r0 = r0.getProjection()
            com.baidu.platform.comapi.basestruct.GeoPoint r9 = r0.fromPixels(r9, r10)
        L7a:
            java.util.List<com.baidu.platform.comapi.map.ak> r10 = r8.mListeners
            int r10 = r10.size()
            if (r1 >= r10) goto L94
            java.util.List<com.baidu.platform.comapi.map.ak> r10 = r8.mListeners
            java.lang.Object r10 = r10.get(r1)
            com.baidu.platform.comapi.map.ak r10 = (com.baidu.platform.comapi.map.ak) r10
            if (r10 != 0) goto L8d
            goto L90
        L8d:
            r10.a(r9)
        L90:
            int r1 = r1 + 1
            goto L7a
        L93:
            return r1
        L94:
            r9 = 1
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.MapController.c(int, int):boolean");
    }

    private void d() {
        MessageProxy.unRegisterMessageHandler(4000, this.t);
        MessageProxy.unRegisterMessageHandler(519, this.t);
        MessageProxy.unRegisterMessageHandler(39, this.t);
        MessageProxy.unRegisterMessageHandler(512, this.t);
        MessageProxy.unRegisterMessageHandler(65297, this.t);
        MessageProxy.unRegisterMessageHandler(UIMsg.MsgDefine.V_WM_VSTREETCLICKBACKGROUND, this.t);
        MessageProxy.unRegisterMessageHandler(50, this.t);
        MessageProxy.unRegisterMessageHandler(51, this.t);
        MessageProxy.unRegisterMessageHandler(65301, this.t);
        MessageProxy.unRegisterMessageHandler(41, this.t);
        MessageProxy.unRegisterMessageHandler(UIMsg.MsgDefine.MSG_MAP_DATA_NET_RESPONSE, this.t);
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x024c A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x025d A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0266 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0270 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0279 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0283 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x028c A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0296 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02a1 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x02ad A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x02b6 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x02c0 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:124:0x02c9 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02d3 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02e3 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x02ec A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0115 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x014d A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x017d A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0189 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0195 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01a3 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01af A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01bd A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01cb A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01d9 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01e7 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01fe A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0215 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0223 A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x023c A[Catch: JSONException -> 0x0318, TryCatch #1 {JSONException -> 0x0318, blocks: (B:13:0x0064, B:15:0x007e, B:16:0x0084, B:133:0x02f3, B:26:0x00b5, B:29:0x00d2, B:32:0x00e3, B:34:0x00ec, B:36:0x00fa, B:37:0x00fe, B:42:0x010f, B:44:0x0115, B:51:0x0149, B:58:0x0177, B:60:0x017d, B:61:0x0183, B:63:0x0189, B:64:0x018f, B:66:0x0195, B:67:0x019b, B:69:0x01a3, B:70:0x01a9, B:72:0x01af, B:73:0x01b5, B:75:0x01bd, B:76:0x01c3, B:78:0x01cb, B:79:0x01d1, B:81:0x01d9, B:82:0x01df, B:84:0x01e7, B:85:0x01f6, B:87:0x01fe, B:88:0x020d, B:90:0x0215, B:91:0x021b, B:93:0x0223, B:95:0x0234, B:97:0x023c, B:98:0x0244, B:100:0x024c, B:101:0x0254, B:103:0x025d, B:105:0x0268, B:107:0x0270, B:109:0x027b, B:111:0x0283, B:113:0x028e, B:115:0x0296, B:117:0x02a5, B:119:0x02ad, B:121:0x02b8, B:123:0x02c0, B:125:0x02cb, B:127:0x02d3, B:128:0x02d9, B:130:0x02e3, B:132:0x02ee, B:131:0x02ec, B:124:0x02c9, B:120:0x02b6, B:116:0x02a1, B:112:0x028c, B:108:0x0279, B:104:0x0266, B:50:0x0140, B:47:0x012c, B:52:0x014d, B:54:0x015d, B:56:0x0163, B:38:0x0101, B:40:0x0107, B:41:0x010c, B:33:0x00ea, B:22:0x0094, B:134:0x030e, B:19:0x008c), top: B:142:0x0064, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean d(int r31, int r32) {
        /*
            Method dump skipped, instructions count: 794
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.MapController.d(int, int):boolean");
    }

    public static int getScaleDis(int i) {
        switch (i) {
            case 1:
                return 10000000;
            case 2:
                return 5000000;
            case 3:
                return 2000000;
            case 4:
                return 1000000;
            case 5:
                return 500000;
            case 6:
                return 200000;
            case 7:
                return nexCrop.ABSTRACT_DIMENSION;
            case 8:
                return 50000;
            case 9:
                return 25000;
            case 10:
                return Level.INFO_INT;
            case 11:
                return 10000;
            case 12:
                return 5000;
            case 13:
                return 2000;
            case 14:
                return 1000;
            case 15:
                return 500;
            case 16:
                return 200;
            case 17:
                return 100;
            case 18:
                return 50;
            case 19:
                return 20;
            case 20:
                return 10;
            case 21:
                return 5;
            case 22:
                return 2;
            default:
                return 0;
        }
    }

    public float GetFZoomToBoundF(Bundle bundle, Bundle bundle2) {
        if (!a()) {
            return 0.0f;
        }
        return this.r.GetFZoomToBoundF(bundle, bundle2);
    }

    public int MapMsgProc(int i, int i2, int i3) {
        return MapMsgProc(i, i2, i3, 0, 0, SearchStatUtils.POW, SearchStatUtils.POW, SearchStatUtils.POW, SearchStatUtils.POW);
    }

    public int MapMsgProc(int i, int i2, int i3, int i4, int i5, double d, double d2, double d3, double d4) {
        if (!a()) {
            return -1;
        }
        return MapProc(this.s, i, i2, i3, i4, i5, d, d2, d3, d4);
    }

    public void SetStyleMode(int i) {
        setMapScene(i);
    }

    public void a(int i, int i2) {
        if (!a()) {
            return;
        }
        this.r.MoveToScrPoint(i, i2);
    }

    public void addOneOverlayItem(Bundle bundle) {
        this.r.addOneOverlayItem(bundle);
    }

    public void addStreetCustomMarker(Bundle bundle, Bitmap bitmap) {
        if (!a()) {
            return;
        }
        this.r.AddStreetCustomMarker(bundle, bitmap);
    }

    public void animateTo(GeoPoint geoPoint, int i) {
        if (!a()) {
            return;
        }
        MapStatus mapStatus = getMapStatus();
        mapStatus.centerPtX = geoPoint.getLongitude();
        mapStatus.centerPtY = geoPoint.getLatitude();
        setMapStatusWithAnimation(mapStatus, i);
    }

    public boolean cleanCache(MapLayerType mapLayerType) {
        AppBaseMap appBaseMap = this.r;
        return appBaseMap != null && appBaseMap.CleanCache(mapLayerType.a);
    }

    public void clearUniversalLayer() {
        if (a()) {
            this.r.clearUniversalLayer();
        }
    }

    public boolean createByDuplicateAppBaseMap(long j2) {
        AppBaseMap appBaseMap = new AppBaseMap();
        this.r = appBaseMap;
        if (!appBaseMap.CreateByDuplicate(j2)) {
            this.r = null;
            this.s = 0L;
            return false;
        }
        this.W = true;
        this.s = this.r.GetId();
        List<AppBaseMap> list = T;
        if (list != null) {
            list.add(this.r);
        }
        return true;
    }

    public void enablePOIAnimation(boolean z2) {
        if (a()) {
            this.r.enablePOIAnimation(z2);
        }
    }

    public void forceSetMapScene(int i) {
        this.m = i;
        if (a()) {
            this.r.setMapScene(this.m);
        }
    }

    public boolean forceSetMapThemeScene(int i, int i2, Bundle bundle) {
        this.n = i;
        this.m = i2;
        if (!a()) {
            return false;
        }
        return this.r.setMapThemeScene(i, i2, bundle);
    }

    public float getAdapterZoomUnitsEx() {
        if (!a()) {
            return 0.0f;
        }
        return this.r.GetAdapterZoomUnitsEx();
    }

    public AppBaseMap getBaseMap() {
        return this.r;
    }

    public int getCacheSize(MapLayerType mapLayerType) {
        AppBaseMap appBaseMap = this.r;
        if (appBaseMap == null) {
            return 0;
        }
        return appBaseMap.GetCacheSize(mapLayerType.a);
    }

    public CaptureMapListener getCaptureMapListener() {
        return this.c;
    }

    public String getCityInfoByID(int i) {
        AppBaseMap appBaseMap = this.r;
        if (appBaseMap != null) {
            return appBaseMap.GetCityInfoByID(i);
        }
        return null;
    }

    public MapStatus getCurrentMapStatus() {
        return a(false);
    }

    public float getCurrentZoomLevel() {
        Bundle GetMapStatus;
        AppBaseMap appBaseMap = this.r;
        if (appBaseMap == null || (GetMapStatus = appBaseMap.GetMapStatus(false)) == null) {
            return 4.0f;
        }
        return (float) GetMapStatus.getDouble("level");
    }

    public IndoorMapInfo getFocusedBaseIndoorMapInfo() {
        String[] strArr;
        int[] iArr;
        if (!a()) {
            return null;
        }
        String GetFocusedBaseIndoorMapInfo = this.r.GetFocusedBaseIndoorMapInfo();
        if (!TextUtils.isEmpty(GetFocusedBaseIndoorMapInfo)) {
            try {
                JSONObject jSONObject = new JSONObject(GetFocusedBaseIndoorMapInfo);
                String optString = jSONObject.optString("focusindoorid");
                String optString2 = jSONObject.optString("curfloor");
                int optInt = jSONObject.optInt("idrtype");
                JSONArray optJSONArray = jSONObject.optJSONArray("floorlist");
                if (optJSONArray != null) {
                    strArr = new String[optJSONArray.length()];
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        arrayList.add(optJSONArray.getString(i));
                    }
                    arrayList.toArray(strArr);
                } else {
                    strArr = null;
                }
                JSONArray optJSONArray2 = jSONObject.optJSONArray("floorattribute");
                if (optJSONArray2 != null) {
                    iArr = new int[optJSONArray2.length()];
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        iArr[i2] = optJSONArray2.optInt(i2);
                    }
                } else {
                    iArr = null;
                }
                return new IndoorMapInfo(optString, optString2, strArr, iArr, optInt, jSONObject.optInt("idrguide"), jSONObject.optString("idrsearch"));
            } catch (JSONException unused) {
            }
        }
        return null;
    }

    public com.baidu.platform.comapi.map.b.d getGestureMonitor() {
        if (this.k == null) {
            this.k = new com.baidu.platform.comapi.map.b.d(this);
        }
        return this.k;
    }

    public Bundle getGestureOptInfoForLog() {
        float f;
        Bundle bundle = null;
        if (!this.E.a) {
            return null;
        }
        MapStatus mapStatus = getMapStatus();
        int intX = this.E.d.getIntX();
        int intY = this.E.d.getIntY();
        int i = (Math.sqrt((intX * intX) + (intY * intY)) > 100.0d ? 1 : (Math.sqrt((intX * intX) + (intY * intY)) == 100.0d ? 0 : -1));
        boolean z2 = true;
        boolean z3 = i > 0;
        if (this.E.b <= 0.0f || Math.abs(mapStatus.level - f) < 0.5d) {
            z2 = false;
        }
        if (z3 || z2) {
            bundle = new Bundle();
            bundle.putDouble("pre_x", this.E.c.getLongitude());
            bundle.putDouble("pre_y", this.E.c.getLatitude());
            bundle.putFloat("pre_level", this.E.b);
        }
        this.E.a();
        return bundle;
    }

    public k getHideIndoorPopupListener() {
        return this.d;
    }

    public EngineMsgListener getIndoorMapListener() {
        return this.g;
    }

    public boolean getMapBarData() {
        if (!a()) {
            return false;
        }
        Bundle bundle = new Bundle();
        this.r.getMapBarData(bundle);
        byte[] bArr = new byte[0];
        String str = null;
        String string = bundle.containsKey("uid") ? bundle.getString("uid") : null;
        String string2 = bundle.containsKey("searchbound") ? bundle.getString("searchbound") : null;
        if (bundle.containsKey("curfloor")) {
            str = bundle.getString("curfloor");
        }
        if (bundle.containsKey("barinfo")) {
            bArr = bundle.getByteArray("barinfo");
        }
        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.b(string, string2, str, bArr));
        return true;
    }

    public boolean getMapBarShowData() {
        if (!a()) {
            return false;
        }
        return this.r.getMapBarData(new Bundle());
    }

    public boolean getMapClickEnable() {
        return this.A;
    }

    public MapControlMode getMapControlMode() {
        return this.Y;
    }

    public long getMapId() {
        return this.s;
    }

    public MapRenderModeChangeListener getMapRenderModeChangeListener() {
        return this.f;
    }

    public int getMapScene() {
        if (!a()) {
            return 0;
        }
        return this.r.getMapScene();
    }

    public MapStatus getMapStatus() {
        return a(true);
    }

    public com.baidu.mapsdkplatform.comapi.map.w getMapStatusInner() {
        if (!a()) {
            return null;
        }
        Bundle GetMapStatus = this.r.GetMapStatus();
        com.baidu.mapsdkplatform.comapi.map.w wVar = new com.baidu.mapsdkplatform.comapi.map.w();
        wVar.a(GetMapStatus);
        return wVar;
    }

    public int getMapTheme() {
        if (!a()) {
            return 0;
        }
        return this.r.getMapTheme();
    }

    public MapViewInterface getMapView() {
        SoftReference<MapViewInterface> softReference = this.h;
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }

    public MapViewListener getMapViewListener() {
        return this.b;
    }

    public NaviMapViewListener getNaviMapViewListener() {
        return this.i;
    }

    public String getProjectionPt(String str) {
        if (!a()) {
            return null;
        }
        return this.r.getProjectionPt(str);
    }

    public int getScaleLevel(int i, int i2) {
        if (!a()) {
            return 0;
        }
        return this.r.getScaleLevel(i, i2);
    }

    public int getSceneLayerScene() {
        return this.m;
    }

    public int getSceneLayerTheme() {
        return this.n;
    }

    public int getScreenHeight() {
        return this.v;
    }

    public int getScreenWidth() {
        return this.u;
    }

    public am getStreetArrowClickListener() {
        return this.e;
    }

    public int getVMPMapCityCode() {
        if (this.r != null) {
            Bundle bundle = new Bundle();
            bundle.putString("querytype", "map");
            this.r.GetVMPMapCityInfo(bundle);
            return bundle.getInt("code");
        }
        return 0;
    }

    public int getVMPMapCityItsInfo() {
        if (this.r != null) {
            Bundle bundle = new Bundle();
            bundle.putString("querytype", "its");
            this.r.GetVMPMapCityInfo(bundle);
            return bundle.getInt("rst");
        }
        return 0;
    }

    public int getVMPMapCityLevel() {
        if (this.r != null) {
            Bundle bundle = new Bundle();
            bundle.putString("querytype", "map");
            this.r.GetVMPMapCityInfo(bundle);
            return bundle.getInt("level");
        }
        return 0;
    }

    public int getVMPMapCitySatInfo() {
        if (this.r != null) {
            Bundle bundle = new Bundle();
            bundle.putString("querytype", "sat");
            this.r.GetVMPMapCityInfo(bundle);
            return bundle.getInt("rst");
        }
        return 0;
    }

    public float getZoomLevel() {
        Bundle GetMapStatus;
        AppBaseMap appBaseMap = this.r;
        if (appBaseMap == null || (GetMapStatus = appBaseMap.GetMapStatus()) == null) {
            return 4.0f;
        }
        return (float) GetMapStatus.getDouble("level");
    }

    public float getZoomToBound(Bundle bundle, int i, int i2) {
        if (!a()) {
            return 0.0f;
        }
        return this.r.GetZoomToBound(bundle, i, i2);
    }

    public float getZoomToBoundF(Bundle bundle) {
        if (!a()) {
            return 0.0f;
        }
        return this.r.GetZoomToBoundF(bundle);
    }

    public double getZoomUnitsInMeter() {
        Bundle GetMapStatus;
        AppBaseMap baseMap = getBaseMap();
        if (baseMap != null && (GetMapStatus = baseMap.GetMapStatus()) != null) {
            double d = GetMapStatus.getFloat("adapterZoomUnits");
            if (d > 1.0E-4d) {
                return d;
            }
        }
        return Math.pow(2.0d, 18.0f - getZoomLevel());
    }

    public void handleClick(MotionEvent motionEvent) {
        MapMsgProc(UIMsg.KEvent.V_WM_LBUTTONCLICK, 0, ((int) motionEvent.getX()) | (((int) motionEvent.getY()) << 16));
    }

    public void handleDoubleClickZoom(MotionEvent motionEvent) {
        if (System.currentTimeMillis() - this.M < 100) {
            return;
        }
        mapStatusChangeStart();
        this.mIsAnimating = true;
        float y2 = motionEvent.getY();
        float f = this.K - y2;
        MapMsgProc(8193, 3, (int) ((f / (getScreenHeight() / 9.0f)) * 10000.0f));
        this.L = f;
        this.K = y2;
        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.d());
        if (!isNaviMode() || getNaviMapViewListener() == null) {
            return;
        }
        getNaviMapViewListener().onAction(521, null);
    }

    public void handleDoubleDownClick(MotionEvent motionEvent) {
        this.G = true;
        this.J = motionEvent.getX();
        this.K = motionEvent.getY();
        this.M = System.currentTimeMillis();
        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.d());
    }

    public void handleDoubleTouch(MotionEvent motionEvent) {
        int i;
        GeoPoint geoPoint;
        GeoPoint fromPixels;
        float f;
        NaviMapViewListener naviMapViewListener;
        if (System.currentTimeMillis() - this.M > 150) {
            return;
        }
        if (isNaviMode() && (naviMapViewListener = this.i) != null) {
            naviMapViewListener.onAction(513, motionEvent);
            return;
        }
        if (this.F) {
            SoftReference<MapViewInterface> softReference = this.h;
            if (softReference == null || softReference.get() == null || this.h.get().getProjection() == null) {
                return;
            }
            float x2 = motionEvent.getX() - (getScreenWidth() / 2);
            float y2 = (motionEvent.getY() - (getScreenHeight() / 2)) * (-1.0f);
            float f2 = 0.0f;
            if (isCompass || this.I) {
                fromPixels = this.h.get().getProjection().fromPixels(getScreenWidth() / 2, getScreenHeight() / 2);
                x2 = 0.0f;
                y2 = 0.0f;
            } else {
                fromPixels = this.h.get().getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            if (fromPixels != null) {
                f2 = (float) fromPixels.getLongitudeE6();
                f = (float) fromPixels.getLatitudeE6();
            } else {
                f = 0.0f;
            }
            this.N = true;
            getGestureMonitor().b(this.h.get().getZoomLevel() + 1.0f);
            mapStatusChangeStart();
            MapMsgProc(8195, ((int) motionEvent.getX()) | (((int) motionEvent.getY()) << 16), ((this.v / 2) << 16) | (this.u / 2), 0, 0, f2, f, x2, y2);
            O = System.currentTimeMillis();
            i = 0;
            procGestureForLog(false, null);
            geoPoint = fromPixels;
        } else {
            i = 0;
            geoPoint = null;
        }
        if (geoPoint == null || this.mListeners == null) {
            return;
        }
        for (int i2 = i; i2 < this.mListeners.size(); i2++) {
            ak akVar = this.mListeners.get(i2);
            if (akVar != null) {
                akVar.b(geoPoint);
            }
        }
    }

    @SuppressLint({"FloatMath"})
    public boolean handleFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (w && this.S) {
            float sqrt = (float) ((((float) Math.sqrt((f * f) + (f2 * f2))) / (SysOSUtil.getInstance().getDensityDPI() / 310.0f)) * 1.3d);
            if (getMapControlMode() != MapControlMode.STREET && sqrt < 300.0f) {
                this.B = false;
                return false;
            }
            this.B = true;
            getGestureMonitor().a();
            mapStatusChangeStart();
            MapMsgProc(34, (int) sqrt, (((int) motionEvent2.getY()) << 16) | ((int) motionEvent2.getX()));
            if (getMapViewListener() != null) {
                com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.c());
            }
            this.V = false;
            if (this.mListeners != null) {
                for (int i = 0; i < this.mListeners.size(); i++) {
                    ak akVar = this.mListeners.get(i);
                    if (akVar != null) {
                        akVar.a(motionEvent2);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean handleKeyEvent(int i, KeyEvent keyEvent) {
        int GetAdaptKeyCode = GetAdaptKeyCode(i);
        if (GetAdaptKeyCode == 0) {
            return false;
        }
        MapMsgProc(1, GetAdaptKeyCode, 0);
        return true;
    }

    public void handleLongClick(MotionEvent motionEvent) {
        MapMsgProc(UIMsg.KEvent.V_WM_LBUTTONLONGCLICK, 0, ((int) motionEvent.getX()) | (((int) motionEvent.getY()) << 16));
    }

    public int handleMapModeGet() {
        return MapMsgProc(4113, 0, 0);
    }

    public boolean handlePopupClick(int i, int i2) {
        return false;
    }

    public void handleRightClick() {
        MapMsgProc(UIMsg.KEvent.V_WM_RBUTTONCLICK, 0, 0);
    }

    public void handleStreetscapeDoubleTouch(MotionEvent motionEvent) {
        float f;
        SoftReference<MapViewInterface> softReference = this.h;
        if (softReference == null || softReference.get() == null || this.h.get().getProjection() == null) {
            return;
        }
        GeoPoint fromPixels = this.h.get().getProjection().fromPixels(this.u / 2, this.v / 2);
        float f2 = 0.0f;
        if (fromPixels != null) {
            f2 = (float) fromPixels.getLongitudeE6();
            f = (float) fromPixels.getLatitudeE6();
        } else {
            f = 0.0f;
        }
        MapMsgProc(8195, (((int) motionEvent.getY()) << 16) | ((int) motionEvent.getX()), ((this.v / 2) << 16) | (this.u / 2), 0, 0, f2, f, SearchStatUtils.POW, SearchStatUtils.POW);
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        if (!a()) {
            return false;
        }
        if (!this.B) {
            this.X.a(motionEvent);
        }
        if (motionEvent.getPointerCount() == 2) {
            this.l = true;
            w = false;
            b();
            procGestureForLog(false, null);
        }
        if (motionEvent.getAction() != 2 && this.G) {
            this.l = true;
            b();
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.l = true;
            a(motionEvent);
        } else if (action == 1) {
            w = true;
            this.l = true;
            handleTouchUp(motionEvent);
        } else if (action != 2) {
            return false;
        } else {
            if (this.G) {
                handleDoubleClickZoom(motionEvent);
            } else if (this.S) {
                handleTouchMove(motionEvent);
            }
        }
        if (this.mListeners != null) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                ak akVar = this.mListeners.get(i);
                if (akVar != null) {
                    akVar.a(motionEvent);
                }
            }
        }
        return true;
    }

    public boolean handleTouchMove(MotionEvent motionEvent) {
        if (w && System.currentTimeMillis() - O >= 300) {
            if (this.mHasMapObjDraging) {
                if (getMapView() != null && getMapView().getProjection() != null) {
                    GeoPoint fromPixels = getMapView().getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
                    if (this.mListeners != null) {
                        for (int i = 0; i < this.mListeners.size(); i++) {
                            ak akVar = this.mListeners.get(i);
                            if (akVar != null && fromPixels != null) {
                                akVar.d(fromPixels);
                            }
                        }
                    }
                }
                return true;
            }
            float abs = Math.abs(motionEvent.getX() - x);
            float abs2 = Math.abs(motionEvent.getY() - y);
            double density = SysOSUtil.getInstance().getDensity();
            if (density > 1.5d) {
                density *= 1.5d;
            }
            float f = (float) density;
            if (z && abs / f <= 3.0f && abs2 / f <= 3.0f) {
                return true;
            }
            z = false;
            if (isCompass) {
                com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.a());
            }
            procGestureForLog(true, new Point(abs, abs2));
            int x2 = (int) motionEvent.getX();
            int y2 = (int) motionEvent.getY();
            if (x2 < 0) {
                x2 = 0;
            }
            if (y2 < 0) {
                y2 = 0;
            }
            if (this.l) {
                getGestureMonitor().b();
                this.l = false;
            }
            mapStatusChangeStart();
            MapMsgProc(3, 0, (y2 << 16) | x2);
            com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.b(false, true));
            this.B = false;
            this.q = true;
            this.V = true;
            return false;
        }
        return true;
    }

    public boolean handleTouchSingleClick(MotionEvent motionEvent) {
        NaviMapViewListener naviMapViewListener;
        int x2 = (int) motionEvent.getX();
        int y2 = (int) motionEvent.getY();
        c(x2, y2);
        if (!d(x2, y2) && !handlePopupClick(x2, y2) && !a(1, x2, y2)) {
            if (this.A && b(x2, y2)) {
                return true;
            }
            if (isNaviMode() && (naviMapViewListener = this.i) != null) {
                naviMapViewListener.onAction(514, motionEvent);
            }
            if (getMapViewListener() == null) {
                return false;
            }
            getMapViewListener().onClickedBackground((int) motionEvent.getX(), (int) motionEvent.getY());
            return false;
        }
        return true;
    }

    public boolean handleTouchUp(MotionEvent motionEvent) {
        int x2 = (int) motionEvent.getX();
        int y2 = (int) motionEvent.getY();
        if (x2 < 0) {
            x2 = 0;
        }
        if (y2 < 0) {
            y2 = 0;
        }
        if (this.mHasMapObjDraging) {
            if (this.mListeners != null && getMapView() != null && getMapView().getProjection() != null) {
                GeoPoint fromPixels = getMapView().getProjection().fromPixels(x2, y2);
                for (int i = 0; i < this.mListeners.size(); i++) {
                    ak akVar = this.mListeners.get(i);
                    if (akVar != null && fromPixels != null) {
                        akVar.e(fromPixels);
                    }
                }
            }
            this.mHasMapObjDraging = false;
            return true;
        }
        if (w) {
            MapMsgProc(5, 0, x2 | (y2 << 16));
        }
        if (!this.B && getMapViewListener() != null) {
            getMapViewListener().onMapAnimationFinish();
        }
        if (!this.B && isNaviMode() && getNaviMapViewListener() != null) {
            getNaviMapViewListener().onMapAnimationFinish();
        }
        boolean z2 = motionEvent.getEventTime() - this.U < 300 && Math.abs(motionEvent.getX() - x) < 10.0f && Math.abs(motionEvent.getY() - y) < 10.0f;
        if (!this.B && ((!z2 || this.V) && !this.N && !this.mIsAnimating && this.mListeners != null)) {
            com.baidu.mapsdkplatform.comapi.map.w mapStatusInner = getMapStatusInner();
            for (int i2 = 0; i2 < this.mListeners.size(); i2++) {
                ak akVar2 = this.mListeners.get(i2);
                if (akVar2 != null) {
                    akVar2.c(mapStatusInner);
                }
            }
        }
        this.V = false;
        this.B = false;
        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.b(true, false));
        com.baidu.platform.comapi.util.a.a().a(new com.baidu.platform.comapi.map.a.c());
        return true;
    }

    public boolean handleTrackballEvent(MotionEvent motionEvent) {
        if (!a()) {
            return false;
        }
        if (motionEvent.getAction() == 2) {
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            int i = rawX > 0.0f ? 18 : rawX < 0.0f ? 16 : 0;
            if (rawY > 0.0f) {
                i = 19;
            } else if (rawY < 0.0f) {
                i = 17;
            }
            if (i == 0) {
                return false;
            }
            MapMsgProc(1, i, 0);
        }
        return true;
    }

    public boolean handleZoomTo(int i) {
        int i2;
        if (i != 0) {
            if (i == 1) {
                i2 = 4096;
            }
            return false;
        }
        i2 = 4097;
        MapMsgProc(i2, -1, 0);
        return false;
    }

    public boolean importMapTheme(int i) {
        if (!a()) {
            return false;
        }
        return this.r.importMapTheme(i);
    }

    public void initAppBaseMap() {
        if (T.size() == 0) {
            initBaseMap();
        } else {
            createByDuplicateAppBaseMap(T.get(0).GetId());
        }
    }

    public void initBaseMap() {
        AppBaseMap appBaseMap = new AppBaseMap();
        this.r = appBaseMap;
        appBaseMap.Create();
        this.s = this.r.GetId();
        List<AppBaseMap> list = T;
        if (list != null) {
            list.add(this.r);
        }
    }

    public void initMapResources(Bundle bundle) {
        if (this.C || bundle == null || this.r == null) {
            return;
        }
        boolean z2 = SysOSUtil.getInstance().getDensityDPI() >= 180;
        this.nearlyRadius = (SysOSUtil.getInstance().getDensityDPI() * 25) / 240;
        String string = bundle.getString("modulePath");
        String string2 = bundle.getString("appSdcardPath");
        String string3 = bundle.getString("appCachePath");
        String string4 = bundle.getString("appSecondCachePath");
        String string5 = bundle.getString("engineErrorPath");
        int i = bundle.getInt("mapTmpMax");
        int i2 = bundle.getInt("domTmpMax");
        int i3 = bundle.getInt("itsTmpMax");
        int i4 = bundle.getInt("ssgTmpMax");
        String str = z2 ? "/h/" : "/l/";
        String str2 = string + "/cfg";
        String str3 = string2 + "/vmp";
        String str4 = str2 + "/a/";
        String str5 = str3 + str;
        String str6 = str3 + str;
        String str7 = string3 + "/tmp/";
        String str8 = string4 + "/tmp/";
        Bundle bundle2 = new Bundle();
        bundle2.putString("cfgdataroot", str4);
        bundle2.putString("vmpdataroot", str5);
        bundle2.putString("tmpdataroot", str7);
        bundle2.putString("tmpdatapast", str8);
        bundle2.putString("importroot", str6);
        bundle2.putString("stylerespath", str2 + "/a/");
        if (string5 != null && string5.length() > 0) {
            bundle2.putString("engineerrorpath", string5);
        }
        bundle2.putInt("cx", this.u);
        bundle2.putInt("cy", this.v);
        bundle2.putInt("ndpi", SysOSUtil.getInstance().getDensityDPI());
        bundle2.putFloat("fdpi", SysOSUtil.getInstance().getDensityDPI());
        bundle2.putInt("maptmpmax", i);
        bundle2.putInt("domtmpmax", i2);
        bundle2.putInt("itstmpmax", i3);
        bundle2.putInt("ssgtmpmax", i4);
        bundle2.putInt("pathchange", 0);
        if (bundle.containsKey("maptheme")) {
            bundle2.putInt("maptheme", bundle.getInt("maptheme"));
        }
        if (bundle.containsKey("mapscene")) {
            bundle2.putInt("mapscene", bundle.getInt("mapscene"));
        }
        if (bundle.containsKey("fontsizelevel")) {
            bundle2.putInt("fontsizelevel", bundle.getInt("fontsizelevel"));
        }
        if (!com.baidu.platform.comapi.b.g()) {
            com.baidu.platform.comapi.b.f();
        }
        if (this.r.initWithOptions(bundle2, false)) {
            this.r.SetMapStatus(bundle);
            this.C = true;
            return;
        }
        Log.e(j, "MapControl init fail!");
        if (!OpenLogUtil.isMapLogEnable()) {
            return;
        }
        com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("MapControl init fail");
    }

    public boolean is3DGestureEnable() {
        return this.P;
    }

    public boolean isBaseIndoorMapMode() {
        if (!a()) {
            return false;
        }
        return this.r.IsBaseIndoorMapMode();
    }

    public boolean isCanTouchMove() {
        return this.S;
    }

    public boolean isDoubleClickZoom() {
        return this.F;
    }

    public boolean isDuplicate() {
        return this.W;
    }

    public boolean isEnableDMoveZoom() {
        return this.G;
    }

    public boolean isEnableZoom() {
        return this.R;
    }

    public boolean isEnlargeCenterWithDoubleClickEnabled() {
        return this.I;
    }

    public boolean isInFocusBarBorder(GeoPoint geoPoint, double d) {
        return a() && geoPoint != null && this.r.IsPointInFocusBarBorder(geoPoint.getLongitude(), geoPoint.getLatitude(), d);
    }

    public boolean isInFocusIndoorBuilding(GeoPoint geoPoint) {
        return a() && geoPoint != null && this.r.IsPointInFocusIDRBorder(geoPoint.getLongitude(), geoPoint.getLatitude());
    }

    public boolean isMapAnimationRunning() {
        if (!a()) {
            return false;
        }
        return this.r.isAnimationRunning();
    }

    public boolean isMovedMap() {
        return this.q;
    }

    public boolean isNaviMode() {
        if (a()) {
            return this.r.isNaviMode();
        }
        return false;
    }

    public boolean isOverlookGestureEnable() {
        return this.Q;
    }

    public boolean isPressedOnPopup(int i, int i2) {
        return false;
    }

    public boolean isStreetArrowShown() {
        if (!a()) {
            return false;
        }
        return this.r.IsStreetArrowShown();
    }

    public boolean isStreetCustomMarkerShown() {
        if (!a()) {
            return false;
        }
        return this.r.IsStreetCustomMarkerShown();
    }

    public boolean isStreetPOIMarkerShown() {
        if (!a()) {
            return false;
        }
        return this.r.IsStreetPOIMarkerShown();
    }

    public boolean isStreetRoadClickable() {
        if (!a()) {
            return false;
        }
        return this.r.IsStreetRoadClickable();
    }

    public boolean isTwoTouchClickZoomEnabled() {
        return this.H;
    }

    public void mapStatusChangeStart() {
        if (this.mIsMoving) {
            return;
        }
        this.mIsMoving = true;
        this.mIsAnimating = false;
        com.baidu.mapsdkplatform.comapi.map.w mapStatusInner = getMapStatusInner();
        if (this.mListeners == null) {
            return;
        }
        for (int i = 0; i < this.mListeners.size(); i++) {
            ak akVar = this.mListeners.get(i);
            if (akVar != null) {
                akVar.a(mapStatusInner);
            }
        }
    }

    public void onPause() {
        if (a()) {
            this.r.OnPause();
        }
    }

    public void onResume() {
        if (a()) {
            this.r.OnResume();
        }
    }

    public void procGestureForLog(boolean z2, Point point) {
        if (!this.E.a) {
            MapStatus mapStatus = getMapStatus();
            a aVar = this.E;
            aVar.a = true;
            aVar.b = mapStatus.level;
            aVar.c = new GeoPoint(mapStatus.centerPtX, mapStatus.centerPtY);
            this.E.d = new Point(0, 0);
        }
        if (z2) {
            int abs = Math.abs(point.getIntX());
            int abs2 = Math.abs(point.getIntY());
            Point point2 = this.E.d;
            point2.setIntX(point2.getIntX() + abs);
            Point point3 = this.E.d;
            point3.setIntY(point3.getIntY() + abs2);
        }
    }

    public void recycleMemory(RecycleMemoryLevel recycleMemoryLevel) {
        if (a()) {
            this.r.recycleMemory(recycleMemoryLevel.getLevel());
        }
    }

    public void registMapViewListener(ak akVar) {
        List<ak> list;
        if (akVar == null || (list = this.mListeners) == null) {
            return;
        }
        list.add(akVar);
    }

    public void removeOneOverlayItem(Bundle bundle) {
        this.r.removeOneOverlayItem(bundle);
    }

    public void removeStreetAllCustomMarker() {
        if (!a()) {
            return;
        }
        this.r.RemoveStreetAllCustomMarker();
    }

    public void removeStreetCustomMarker(String str) {
        if (!a()) {
            return;
        }
        this.r.RemoveStreetCustomMaker(str);
    }

    public void saveScreenToLocal(String str) {
        saveScreenToLocal(str, 0, 0, 0, 0);
    }

    public void saveScreenToLocal(String str, int i, int i2, int i3, int i4) {
        if (!a() || TextUtils.isEmpty(str)) {
            return;
        }
        String str2 = null;
        if (i3 != 0 && i4 != 0) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("x", i);
                jSONObject.put("y", i2);
                jSONObject.put(nexExportFormat.TAG_FORMAT_WIDTH, i3);
                jSONObject.put(nexExportFormat.TAG_FORMAT_HEIGHT, i4);
                str2 = jSONObject.toString();
            } catch (Exception unused) {
            }
        }
        this.r.SaveScreenToLocal(str, str2);
    }

    public void scrollBy(int i, int i2) {
        if (i == 0 && i2 == 0) {
            return;
        }
        a((this.u / 2) + i, (this.v / 2) + i2);
    }

    public void set3DGestureEnable(boolean z2) {
        this.P = z2;
    }

    public void setAllStreetCustomMarkerVisibility(boolean z2) {
        if (!a()) {
            return;
        }
        this.r.SetAllStreetCustomMarkerVisibility(z2);
    }

    public void setCanTouchMove(boolean z2) {
        this.S = z2;
    }

    public void setCaptureMapListener(CaptureMapListener captureMapListener) {
        this.c = captureMapListener;
    }

    public void setDoubleClickZoom(boolean z2) {
        this.F = z2;
    }

    public void setEnableZoom(boolean z2) {
        this.R = z2;
    }

    public void setEngineMsgListener(EngineMsgListener engineMsgListener) {
        this.g = engineMsgListener;
    }

    public void setEnlargeCenterWithDoubleClickEnable(boolean z2) {
        this.I = z2;
    }

    public void setHideIndoorPopupListener(k kVar) {
        this.d = kVar;
    }

    public boolean setLayerSceneMode(long j2, MapSceneMode mapSceneMode) {
        if (!a()) {
            return false;
        }
        return this.r.SetLayerSceneMode(j2, mapSceneMode.getMode());
    }

    public void setMapClickEnable(boolean z2) {
        this.A = z2;
    }

    public int setMapControlMode(MapControlMode mapControlMode) {
        if (!a()) {
            return -1;
        }
        this.Y = mapControlMode;
        return this.r.SetMapControlMode(mapControlMode.a);
    }

    public void setMapFirstFrameCallback(MapFirstFrameCallback mapFirstFrameCallback) {
        this.p = mapFirstFrameCallback;
    }

    public void setMapRenderModeChangeListener(MapRenderModeChangeListener mapRenderModeChangeListener) {
        this.f = mapRenderModeChangeListener;
    }

    public void setMapScene(int i) {
        if (i == getMapScene()) {
            return;
        }
        this.m = i;
        if (!a()) {
            return;
        }
        this.r.setMapScene(this.m);
    }

    public void setMapStatus(Bundle bundle) {
        if (!a()) {
            return;
        }
        this.r.SetMapStatus(bundle);
    }

    public void setMapStatus(MapStatus mapStatus) {
        if (!a() || mapStatus == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("level", mapStatus.level);
        bundle.putDouble(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, mapStatus.rotation);
        bundle.putDouble("overlooking", mapStatus.overlooking);
        bundle.putDouble("centerptx", mapStatus.centerPtX);
        bundle.putDouble("centerpty", mapStatus.centerPtY);
        bundle.putDouble("centerptz", mapStatus.centerPtZ);
        bundle.putInt("left", mapStatus.winRound.left);
        bundle.putInt("right", mapStatus.winRound.right);
        bundle.putInt("top", mapStatus.winRound.top);
        bundle.putInt("bottom", mapStatus.winRound.bottom);
        bundle.putLong("gleft", mapStatus.geoRound.left);
        bundle.putLong("gbottom", mapStatus.geoRound.bottom);
        bundle.putLong("gtop", mapStatus.geoRound.top);
        bundle.putLong("gright", mapStatus.geoRound.right);
        bundle.putFloat("yoffset", mapStatus.yOffset);
        bundle.putFloat("xoffset", mapStatus.xOffset);
        bundle.putInt("animatime", mapStatus.animationTime);
        bundle.putInt("animation", mapStatus.hasAnimation);
        bundle.putInt("animatime", mapStatus.animationTime);
        bundle.putInt("bfpp", mapStatus.bfpp ? 1 : 0);
        bundle.putString("panoid", mapStatus.panoId);
        bundle.putInt("autolink", 0);
        bundle.putFloat("siangle", mapStatus.streetIndicateAngle);
        bundle.putInt("isbirdeye", mapStatus.isBirdEye ? 1 : 0);
        bundle.putInt("ssext", mapStatus.streetExt);
        bundle.putFloat("roadOffsetX", mapStatus.roadOffsetX);
        bundle.putFloat("roadOffsetY", mapStatus.roadOffsetY);
        mapStatusChangeStart();
        this.r.SetMapStatus(bundle);
    }

    public void setMapStatus(MapStatus mapStatus, boolean z2) {
        if (!a() || mapStatus == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("level", mapStatus.level);
        bundle.putDouble(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, mapStatus.rotation);
        bundle.putDouble("overlooking", mapStatus.overlooking);
        bundle.putDouble("centerptx", mapStatus.centerPtX);
        bundle.putDouble("centerpty", mapStatus.centerPtY);
        bundle.putDouble("centerptz", mapStatus.centerPtZ);
        bundle.putInt("left", mapStatus.winRound.left);
        bundle.putInt("right", mapStatus.winRound.right);
        bundle.putInt("top", mapStatus.winRound.top);
        bundle.putInt("bottom", mapStatus.winRound.bottom);
        bundle.putLong("gleft", mapStatus.geoRound.left);
        bundle.putLong("gbottom", mapStatus.geoRound.bottom);
        bundle.putLong("gtop", mapStatus.geoRound.top);
        bundle.putLong("gright", mapStatus.geoRound.right);
        bundle.putFloat("yoffset", mapStatus.yOffset);
        bundle.putFloat("xoffset", mapStatus.xOffset);
        bundle.putInt("animation", 0);
        bundle.putInt("animatime", 0);
        bundle.putInt("bfpp", mapStatus.bfpp ? 1 : 0);
        bundle.putString("panoid", mapStatus.panoId);
        bundle.putInt("autolink", z2 ? 1 : 0);
        bundle.putFloat("siangle", mapStatus.streetIndicateAngle);
        bundle.putInt("isbirdeye", mapStatus.isBirdEye ? 1 : 0);
        bundle.putInt("ssext", mapStatus.streetExt);
        this.r.SetMapStatus(bundle);
    }

    public void setMapStatusWithAnimation(MapStatus mapStatus, int i) {
        if (!a() || this.r == null || mapStatus == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("level", mapStatus.level);
        bundle.putDouble(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, mapStatus.rotation);
        bundle.putDouble("overlooking", mapStatus.overlooking);
        bundle.putDouble("centerptx", mapStatus.centerPtX);
        bundle.putDouble("centerpty", mapStatus.centerPtY);
        bundle.putDouble("centerptz", mapStatus.centerPtZ);
        bundle.putInt("left", mapStatus.winRound.left);
        bundle.putInt("right", mapStatus.winRound.right);
        bundle.putInt("top", mapStatus.winRound.top);
        bundle.putInt("bottom", mapStatus.winRound.bottom);
        bundle.putLong("gleft", mapStatus.geoRound.left);
        bundle.putLong("gright", mapStatus.geoRound.right);
        bundle.putLong("gbottom", mapStatus.geoRound.bottom);
        bundle.putLong("gtop", mapStatus.geoRound.top);
        bundle.putFloat("xoffset", mapStatus.xOffset);
        bundle.putFloat("yoffset", mapStatus.yOffset);
        bundle.putInt("animation", 1);
        bundle.putInt("animatime", i);
        bundle.putInt("bfpp", mapStatus.bfpp ? 1 : 0);
        bundle.putString("panoid", mapStatus.panoId);
        bundle.putInt("autolink", 0);
        bundle.putFloat("siangle", mapStatus.streetIndicateAngle);
        bundle.putInt("isbirdeye", mapStatus.isBirdEye ? 1 : 0);
        bundle.putInt("ssext", mapStatus.streetExt);
        bundle.putFloat("roadOffsetX", mapStatus.roadOffsetX);
        bundle.putFloat("roadOffsetY", mapStatus.roadOffsetY);
        mapStatusChangeStart();
        this.mIsAnimating = true;
        this.r.SetMapStatus(bundle);
    }

    public void setMapStatusWithAnimation(MapStatus mapStatus, int i, int i2) {
        if (!a() || this.r == null || mapStatus == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("level", mapStatus.level);
        bundle.putDouble(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, mapStatus.rotation);
        bundle.putDouble("overlooking", mapStatus.overlooking);
        bundle.putDouble("centerptx", mapStatus.centerPtX);
        bundle.putDouble("centerpty", mapStatus.centerPtY);
        bundle.putDouble("centerptz", mapStatus.centerPtZ);
        bundle.putInt("left", mapStatus.winRound.left);
        bundle.putInt("right", mapStatus.winRound.right);
        bundle.putInt("top", mapStatus.winRound.top);
        bundle.putInt("bottom", mapStatus.winRound.bottom);
        bundle.putLong("gleft", mapStatus.geoRound.left);
        bundle.putLong("gright", mapStatus.geoRound.right);
        bundle.putLong("gbottom", mapStatus.geoRound.bottom);
        bundle.putLong("gtop", mapStatus.geoRound.top);
        bundle.putFloat("xoffset", mapStatus.xOffset);
        bundle.putFloat("yoffset", mapStatus.yOffset);
        bundle.putInt("animationType", i);
        bundle.putInt("animatime", i2);
        bundle.putInt("bfpp", mapStatus.bfpp ? 1 : 0);
        bundle.putString("panoid", mapStatus.panoId);
        bundle.putInt("autolink", 0);
        bundle.putFloat("siangle", mapStatus.streetIndicateAngle);
        bundle.putInt("isbirdeye", mapStatus.isBirdEye ? 1 : 0);
        bundle.putInt("ssext", mapStatus.streetExt);
        bundle.putFloat("roadOffsetX", mapStatus.roadOffsetX);
        bundle.putFloat("roadOffsetY", mapStatus.roadOffsetY);
        this.r.SetNewMapStatus(bundle);
    }

    public void setMapStatusWithAnimation(MapStatus mapStatus, int i, boolean z2) {
        if (!a() || this.r == null || mapStatus == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("level", mapStatus.level);
        bundle.putDouble(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION, mapStatus.rotation);
        bundle.putDouble("overlooking", mapStatus.overlooking);
        bundle.putDouble("centerptx", mapStatus.centerPtX);
        bundle.putDouble("centerpty", mapStatus.centerPtY);
        bundle.putDouble("centerptz", mapStatus.centerPtZ);
        bundle.putInt("left", mapStatus.winRound.left);
        bundle.putInt("right", mapStatus.winRound.right);
        bundle.putInt("top", mapStatus.winRound.top);
        bundle.putInt("bottom", mapStatus.winRound.bottom);
        bundle.putLong("gleft", mapStatus.geoRound.left);
        bundle.putLong("gright", mapStatus.geoRound.right);
        bundle.putLong("gbottom", mapStatus.geoRound.bottom);
        bundle.putLong("gtop", mapStatus.geoRound.top);
        bundle.putFloat("xoffset", mapStatus.xOffset);
        bundle.putFloat("yoffset", mapStatus.yOffset);
        bundle.putInt("animation", 1);
        bundle.putInt("animatime", i);
        bundle.putInt("bfpp", mapStatus.bfpp ? 1 : 0);
        bundle.putString("panoid", mapStatus.panoId);
        bundle.putInt("autolink", z2 ? 1 : 0);
        bundle.putFloat("siangle", mapStatus.streetIndicateAngle);
        bundle.putInt("isbirdeye", mapStatus.isBirdEye ? 1 : 0);
        bundle.putInt("ssext", mapStatus.streetExt);
        bundle.putFloat("roadOffsetX", mapStatus.roadOffsetX);
        bundle.putFloat("roadOffsetY", mapStatus.roadOffsetY);
        this.r.SetMapStatus(bundle);
    }

    public boolean setMapTheme(int i, Bundle bundle) {
        if (!a()) {
            return false;
        }
        if (this.r.getMapTheme() == i) {
            return true;
        }
        this.n = i;
        return this.r.setMapTheme(i, bundle);
    }

    public boolean setMapThemeScene(int i, int i2, Bundle bundle) {
        if (!a()) {
            return false;
        }
        if (this.r.getMapTheme() == i && this.r.getMapScene() == i2) {
            return true;
        }
        this.n = i;
        this.m = i2;
        return this.r.setMapThemeScene(i, i2, bundle);
    }

    public void setMapViewInterface(MapViewInterface mapViewInterface) {
        this.h = new SoftReference<>(mapViewInterface);
    }

    public void setMapViewListener(MapViewListener mapViewListener) {
        this.b = mapViewListener;
    }

    public void setMaxAndMinZoomLevel(float f, float f2) {
        this.mMaxZoomLevel = f;
        this.mMinZoomLevel = f2;
    }

    public void setNaviMapViewListener(NaviMapViewListener naviMapViewListener) {
        this.i = naviMapViewListener;
    }

    public void setNetStatus(int i) {
        EngineMsgListener engineMsgListener = this.g;
        if (engineMsgListener == null) {
            return;
        }
        if (i == 1) {
            engineMsgListener.onLongLinkConnect();
        } else if (i == 2 && this.a != i) {
            engineMsgListener.onLongLinkDisConnect();
        }
        this.a = i;
    }

    public void setOverlayMapCallBack(ae aeVar) {
        AppBaseMap appBaseMap;
        if (aeVar == null || (appBaseMap = this.r) == null) {
            return;
        }
        appBaseMap.SetCallback(aeVar);
    }

    public void setOverlookGestureEnable(boolean z2) {
        this.Q = z2;
    }

    public void setRecommendPOIScene(RecommendPoiScene recommendPoiScene) {
        if (a()) {
            this.r.setRecommendPOIScene(recommendPoiScene.value);
        }
    }

    public void setScreenSize(int i, int i2) {
        this.u = i;
        this.v = i2;
    }

    public void setStreetArrowClickListener(am amVar) {
        this.e = amVar;
    }

    public void setStreetArrowShow(boolean z2) {
        if (a()) {
            this.r.SetStreetArrowShow(z2);
        }
    }

    public void setStreetMarkerClickable(String str, boolean z2) {
        if (!a()) {
            return;
        }
        this.r.SetStreetMarkerClickable(str, z2);
    }

    public void setStreetRoadClickable(boolean z2) {
        if (a()) {
            this.r.SetStreetRoadClickable(z2);
        }
    }

    public void setStyleMode(MapStyleMode mapStyleMode) {
        if (!a()) {
            return;
        }
        this.r.SetStyleMode(mapStyleMode.getMode());
    }

    public void setTargetStreetCustomMarkerVisibility(boolean z2, String str) {
        if (!a()) {
            return;
        }
        this.r.SetTargetStreetCustomMarkerVisibility(z2, str);
    }

    public void setTravelMode(boolean z2) {
        this.o = z2;
    }

    public void setTwoTouchClickZoomEnabled(boolean z2) {
        this.H = z2;
    }

    public void setUniversalFilter(String str) {
        if (a()) {
            this.r.setUniversalFilter(str);
        }
    }

    public void showBaseIndoorMap(boolean z2) {
        if (!a()) {
            return;
        }
        this.r.ShowBaseIndoorMap(z2);
    }

    public void showStreetPOIMarker(boolean z2) {
        if (!a()) {
            return;
        }
        this.r.ShowStreetPOIMarker(z2);
    }

    public void showUniversalLayer(Bundle bundle) {
        if (a()) {
            this.r.showUniversalLayer(bundle);
        }
    }

    public void startIndoorAnimation() {
        if (!a()) {
            return;
        }
        this.r.StartIndoorAnimation();
    }

    public boolean switchBaseIndoorMapFloor(String str, String str2) {
        if (!a()) {
            return false;
        }
        return this.r.SwitchBaseIndoorMapFloor(str, str2);
    }

    public void unInit() {
        AppBaseMap appBaseMap;
        d();
        Handler handler = this.t;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.t = null;
        }
        List<AppBaseMap> list = T;
        if (list != null) {
            list.remove(this.r);
        }
        List<ak> list2 = this.mListeners;
        if (list2 != null) {
            list2.clear();
        }
        if (!this.C || (appBaseMap = this.r) == null) {
            return;
        }
        appBaseMap.Release();
        this.r = null;
        this.C = false;
    }

    public void unInitForMultiTextureView() {
        AppBaseMap appBaseMap;
        if (!this.C || (appBaseMap = this.r) == null) {
            return;
        }
        appBaseMap.Release();
        this.r = null;
        this.C = false;
    }

    public void updateDrawFPS() {
        if (a()) {
            this.r.updateDrawFPS();
        }
    }

    public void updateOneOverlayItem(Bundle bundle) {
        this.r.updateOneOverlayItem(bundle);
    }
}
