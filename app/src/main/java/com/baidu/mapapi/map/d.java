package com.baidu.mapapi.map;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.baidu.platform.comapi.map.ak;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class d implements ak {
    public final /* synthetic */ BaiduMap a;

    public d(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a() {
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(MotionEvent motionEvent) {
        BaiduMap.OnMapTouchListener onMapTouchListener;
        BaiduMap.OnMapTouchListener onMapTouchListener2;
        onMapTouchListener = this.a.r;
        if (onMapTouchListener != null) {
            onMapTouchListener2 = this.a.r;
            onMapTouchListener2.onTouch(motionEvent);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(com.baidu.mapsdkplatform.comapi.map.w wVar) {
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener;
        BaiduMap.OnSynchronizationListener onSynchronizationListener;
        BaiduMap.OnSynchronizationListener onSynchronizationListener2;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener2;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener3;
        int i = BaiduMap.mapStatusReason;
        int i2 = (i & 256) == 256 ? 3 : (i & 16) == 16 ? 2 : 1;
        onMapStatusChangeListener = this.a.q;
        if (onMapStatusChangeListener != null) {
            MapStatus a = MapStatus.a(wVar);
            onMapStatusChangeListener2 = this.a.q;
            onMapStatusChangeListener2.onMapStatusChangeStart(a);
            onMapStatusChangeListener3 = this.a.q;
            onMapStatusChangeListener3.onMapStatusChangeStart(a, i2);
        }
        onSynchronizationListener = this.a.G;
        if (onSynchronizationListener != null) {
            onSynchronizationListener2 = this.a.G;
            onSynchronizationListener2.onMapStatusChangeReason(i2);
        }
        BaiduMap.mapStatusReason = 0;
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(GeoPoint geoPoint) {
        BaiduMap.OnMapClickListener onMapClickListener;
        BaiduMap.OnMapClickListener onMapClickListener2;
        onMapClickListener = this.a.s;
        if (onMapClickListener != null) {
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            onMapClickListener2 = this.a.s;
            onMapClickListener2.onMapClick(mc2ll);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(String str) {
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        List<Overlay> list;
        CopyOnWriteArrayList copyOnWriteArrayList;
        CopyOnWriteArrayList copyOnWriteArrayList2;
        List<Overlay> list2;
        CopyOnWriteArrayList copyOnWriteArrayList3;
        CopyOnWriteArrayList copyOnWriteArrayList4;
        Map map;
        List<Overlay> list3;
        CopyOnWriteArrayList copyOnWriteArrayList5;
        CopyOnWriteArrayList copyOnWriteArrayList6;
        Map map2;
        InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener;
        com.baidu.mapsdkplatform.comapi.map.c cVar2;
        com.baidu.mapsdkplatform.comapi.map.c cVar3;
        com.baidu.mapsdkplatform.comapi.map.c cVar4;
        BaiduMap.OnMyLocationClickListener onMyLocationClickListener;
        BaiduMap.OnMyLocationClickListener onMyLocationClickListener2;
        BaiduMap.OnMapClickListener onMapClickListener;
        BaiduMap.OnMapClickListener onMapClickListener2;
        try {
            JSONObject jSONObject = new JSONObject(str);
            cVar = this.a.j;
            GeoPoint b = cVar.b(jSONObject.optInt("px"), jSONObject.optInt("py"));
            JSONArray optJSONArray = jSONObject.optJSONArray("dataset");
            JSONObject jSONObject2 = null;
            int i = -1;
            if (optJSONArray != null && (jSONObject2 = optJSONArray.optJSONObject(0)) != null) {
                i = jSONObject2.optInt(MapBundleKey.MapObjKey.OBJ_TYPE);
            }
            if (i == 17) {
                onMapClickListener = this.a.s;
                if (onMapClickListener == null) {
                    return;
                }
                MapPoi mapPoi = new MapPoi();
                mapPoi.a(jSONObject2);
                onMapClickListener2 = this.a.s;
                onMapClickListener2.onMapPoiClick(mapPoi);
                return;
            }
            if (i != 18) {
                if (i == 19) {
                    cVar2 = this.a.j;
                    if (cVar2 == null) {
                        return;
                    }
                    cVar3 = this.a.j;
                    com.baidu.mapsdkplatform.comapi.map.w C = cVar3.C();
                    if (C == null) {
                        return;
                    }
                    C.c = 0;
                    C.b = 0;
                    BaiduMap.mapStatusReason |= 16;
                    cVar4 = this.a.j;
                    cVar4.a(C, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
                    return;
                }
                String str2 = "";
                if (i == 90909) {
                    if (jSONObject2 != null) {
                        str2 = jSONObject2.optString("marker_id");
                    }
                    map = this.a.L;
                    Set<String> keySet = map.keySet();
                    if (!keySet.isEmpty() && keySet.contains(str2)) {
                        for (String str3 : keySet) {
                            if (str3 != null && str3.equals(str2)) {
                                map2 = this.a.L;
                                InfoWindow infoWindow = (InfoWindow) map2.get(str3);
                                if (infoWindow != null && (onInfoWindowClickListener = infoWindow.e) != null) {
                                    onInfoWindowClickListener.onInfoWindowClick();
                                    return;
                                }
                            }
                        }
                        return;
                    }
                    list3 = this.a.k;
                    for (Overlay overlay : list3) {
                        if ((overlay instanceof Marker) && overlay.z.equals(str2)) {
                            copyOnWriteArrayList5 = this.a.x;
                            if (!copyOnWriteArrayList5.isEmpty()) {
                                copyOnWriteArrayList6 = this.a.x;
                                Iterator it = copyOnWriteArrayList6.iterator();
                                while (it.hasNext()) {
                                    ((BaiduMap.OnMarkerClickListener) it.next()).onMarkerClick((Marker) overlay);
                                }
                                return;
                            }
                            a(b);
                        }
                    }
                    return;
                } else if (i == 90910) {
                    if (jSONObject2 != null) {
                        str2 = jSONObject2.optString("polyline_id");
                    }
                    list2 = this.a.k;
                    for (Overlay overlay2 : list2) {
                        if ((overlay2 instanceof Polyline) && overlay2.z.equals(str2)) {
                            copyOnWriteArrayList3 = this.a.y;
                            if (!copyOnWriteArrayList3.isEmpty()) {
                                copyOnWriteArrayList4 = this.a.y;
                                Iterator it2 = copyOnWriteArrayList4.iterator();
                                while (it2.hasNext()) {
                                    ((BaiduMap.OnPolylineClickListener) it2.next()).onPolylineClick((Polyline) overlay2);
                                }
                            } else {
                                a(b);
                            }
                        }
                    }
                    return;
                } else if (i != 90911) {
                    return;
                } else {
                    if (jSONObject2 != null) {
                        str2 = jSONObject2.optString("multipoint_id");
                    }
                    list = this.a.k;
                    for (Overlay overlay3 : list) {
                        if ((overlay3 instanceof MultiPoint) && overlay3.z.equals(str2)) {
                            copyOnWriteArrayList = this.a.z;
                            if (!copyOnWriteArrayList.isEmpty()) {
                                copyOnWriteArrayList2 = this.a.z;
                                Iterator it3 = copyOnWriteArrayList2.iterator();
                                while (it3.hasNext()) {
                                    BaiduMap.OnMultiPointClickListener onMultiPointClickListener = (BaiduMap.OnMultiPointClickListener) it3.next();
                                    MultiPoint multiPoint = (MultiPoint) overlay3;
                                    List<MultiPointItem> multiPointItems = multiPoint.getMultiPointItems();
                                    if (jSONObject2 != null) {
                                        int optInt = jSONObject2.optInt("multipoint_index");
                                        if (multiPointItems != null && optInt >= 0 && multiPointItems.size() > optInt) {
                                            onMultiPointClickListener.onMultiPointClick(multiPoint, multiPointItems.get(optInt));
                                        }
                                    }
                                }
                            } else {
                                a(b);
                            }
                        }
                    }
                    return;
                }
            }
            onMyLocationClickListener = this.a.B;
            if (onMyLocationClickListener != null) {
                onMyLocationClickListener2 = this.a.B;
                onMyLocationClickListener2.onMyLocationClick();
                return;
            }
            a(b);
        } catch (Exception unused) {
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(GL10 gl10, com.baidu.mapsdkplatform.comapi.map.w wVar) {
        Map map;
        BaiduMap.OnMapDrawFrameCallback onMapDrawFrameCallback;
        BaiduMap.OnMapDrawFrameCallback onMapDrawFrameCallback2;
        Map map2;
        Map map3;
        View view;
        map = this.a.L;
        if (map != null) {
            map2 = this.a.L;
            if (!map2.values().isEmpty()) {
                map3 = this.a.L;
                for (InfoWindow infoWindow : map3.values()) {
                    if (infoWindow != null && (view = infoWindow.c) != null) {
                        view.post(new e(this, infoWindow));
                    }
                }
            }
        }
        onMapDrawFrameCallback = this.a.D;
        if (onMapDrawFrameCallback != null) {
            MapStatus a = MapStatus.a(wVar);
            onMapDrawFrameCallback2 = this.a.D;
            onMapDrawFrameCallback2.onMapDrawFrame(a);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(boolean z) {
        BaiduMap.OnBaseIndoorMapListener onBaseIndoorMapListener;
        BaiduMap.OnBaseIndoorMapListener onBaseIndoorMapListener2;
        onBaseIndoorMapListener = this.a.E;
        if (onBaseIndoorMapListener != null) {
            MapBaseIndoorMapInfo focusedBaseIndoorMapInfo = this.a.getFocusedBaseIndoorMapInfo();
            onBaseIndoorMapListener2 = this.a.E;
            onBaseIndoorMapListener2.onBaseIndoorMapMode(z, focusedBaseIndoorMapInfo);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void a(boolean z, int i) {
        BaiduMap.OnMapRenderValidDataListener onMapRenderValidDataListener;
        String a;
        BaiduMap.OnMapRenderValidDataListener onMapRenderValidDataListener2;
        onMapRenderValidDataListener = this.a.F;
        if (onMapRenderValidDataListener != null) {
            a = this.a.a(i);
            onMapRenderValidDataListener2 = this.a.F;
            onMapRenderValidDataListener2.onMapRenderValidData(z, i, a);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void b() {
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        BaiduMap.OnMapLoadedCallback onMapLoadedCallback;
        BaiduMap.OnMapLoadedCallback onMapLoadedCallback2;
        BaiduMap baiduMap = this.a;
        cVar = baiduMap.j;
        baiduMap.f = new Projection(cVar);
        this.a.T = true;
        onMapLoadedCallback = this.a.t;
        if (onMapLoadedCallback != null) {
            onMapLoadedCallback2 = this.a.t;
            onMapLoadedCallback2.onMapLoaded();
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void b(com.baidu.mapsdkplatform.comapi.map.w wVar) {
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener2;
        onMapStatusChangeListener = this.a.q;
        if (onMapStatusChangeListener != null) {
            MapStatus a = MapStatus.a(wVar);
            onMapStatusChangeListener2 = this.a.q;
            onMapStatusChangeListener2.onMapStatusChange(a);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void b(GeoPoint geoPoint) {
        BaiduMap.OnMapDoubleClickListener onMapDoubleClickListener;
        BaiduMap.OnMapDoubleClickListener onMapDoubleClickListener2;
        onMapDoubleClickListener = this.a.v;
        if (onMapDoubleClickListener != null) {
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            onMapDoubleClickListener2 = this.a.v;
            onMapDoubleClickListener2.onMapDoubleClick(mc2ll);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public boolean b(String str) {
        JSONObject optJSONObject;
        Map map;
        List<Overlay> list;
        Projection projection;
        Marker marker;
        Projection projection2;
        Marker marker2;
        BaiduMap.OnMarkerDragListener onMarkerDragListener;
        BaiduMap.OnMarkerDragListener onMarkerDragListener2;
        Marker marker3;
        try {
            JSONArray optJSONArray = new JSONObject(str).optJSONArray("dataset");
            if (optJSONArray == null || (optJSONObject = optJSONArray.optJSONObject(0)) == null || optJSONObject.optInt(MapBundleKey.MapObjKey.OBJ_TYPE) != 90909) {
                return false;
            }
            String optString = optJSONObject.optString("marker_id");
            map = this.a.L;
            Set keySet = map.keySet();
            if (!keySet.isEmpty() && keySet.contains(optString)) {
                return false;
            }
            list = this.a.k;
            for (Overlay overlay : list) {
                if ((overlay instanceof Marker) && overlay.z.equals(optString)) {
                    Marker marker4 = (Marker) overlay;
                    if (!marker4.f) {
                        return false;
                    }
                    this.a.N = marker4;
                    projection = this.a.f;
                    marker = this.a.N;
                    Point screenLocation = projection.toScreenLocation(marker.a);
                    Point point = new Point(screenLocation.x, screenLocation.y - 60);
                    projection2 = this.a.f;
                    LatLng fromScreenLocation = projection2.fromScreenLocation(point);
                    marker2 = this.a.N;
                    marker2.setPosition(fromScreenLocation);
                    onMarkerDragListener = this.a.A;
                    if (onMarkerDragListener != null) {
                        onMarkerDragListener2 = this.a.A;
                        marker3 = this.a.N;
                        onMarkerDragListener2.onMarkerDragStart(marker3);
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void c() {
        BaiduMap.OnMapRenderCallback onMapRenderCallback;
        BaiduMap.OnMapRenderCallback onMapRenderCallback2;
        onMapRenderCallback = this.a.u;
        if (onMapRenderCallback != null) {
            onMapRenderCallback2 = this.a.u;
            onMapRenderCallback2.onMapRenderFinished();
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void c(com.baidu.mapsdkplatform.comapi.map.w wVar) {
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener;
        BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener2;
        onMapStatusChangeListener = this.a.q;
        if (onMapStatusChangeListener != null) {
            MapStatus a = MapStatus.a(wVar);
            onMapStatusChangeListener2 = this.a.q;
            onMapStatusChangeListener2.onMapStatusChangeFinish(a);
        }
        BaiduMap.mapStatusReason = 0;
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void c(GeoPoint geoPoint) {
        BaiduMap.OnMapLongClickListener onMapLongClickListener;
        BaiduMap.OnMapLongClickListener onMapLongClickListener2;
        onMapLongClickListener = this.a.w;
        if (onMapLongClickListener != null) {
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            onMapLongClickListener2 = this.a.w;
            onMapLongClickListener2.onMapLongClick(mc2ll);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void d() {
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        Lock lock;
        Lock lock2;
        HeatMap heatMap;
        HeatMap heatMap2;
        com.baidu.mapsdkplatform.comapi.map.c cVar2;
        cVar = this.a.j;
        if (cVar != null) {
            cVar2 = this.a.j;
            cVar2.b(false);
        }
        lock = this.a.J;
        lock.lock();
        try {
            heatMap = this.a.I;
            if (heatMap != null) {
                BaiduMap baiduMap = this.a;
                heatMap2 = baiduMap.I;
                baiduMap.a(heatMap2);
            }
        } finally {
            lock2 = this.a.J;
            lock2.unlock();
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void d(GeoPoint geoPoint) {
        Marker marker;
        Marker marker2;
        Projection projection;
        Projection projection2;
        Marker marker3;
        BaiduMap.OnMarkerDragListener onMarkerDragListener;
        Marker marker4;
        BaiduMap.OnMarkerDragListener onMarkerDragListener2;
        Marker marker5;
        marker = this.a.N;
        if (marker != null) {
            marker2 = this.a.N;
            if (!marker2.f) {
                return;
            }
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            projection = this.a.f;
            Point screenLocation = projection.toScreenLocation(mc2ll);
            Point point = new Point(screenLocation.x, screenLocation.y - 60);
            projection2 = this.a.f;
            LatLng fromScreenLocation = projection2.fromScreenLocation(point);
            marker3 = this.a.N;
            marker3.setPosition(fromScreenLocation);
            onMarkerDragListener = this.a.A;
            if (onMarkerDragListener == null) {
                return;
            }
            marker4 = this.a.N;
            if (!marker4.f) {
                return;
            }
            onMarkerDragListener2 = this.a.A;
            marker5 = this.a.N;
            onMarkerDragListener2.onMarkerDrag(marker5);
        }
    }

    @Override // com.baidu.platform.comapi.map.ak
    public void e(GeoPoint geoPoint) {
        Marker marker;
        Marker marker2;
        Projection projection;
        Projection projection2;
        Marker marker3;
        BaiduMap.OnMarkerDragListener onMarkerDragListener;
        Marker marker4;
        BaiduMap.OnMarkerDragListener onMarkerDragListener2;
        Marker marker5;
        marker = this.a.N;
        if (marker != null) {
            marker2 = this.a.N;
            if (!marker2.f) {
                return;
            }
            LatLng mc2ll = CoordUtil.mc2ll(geoPoint);
            projection = this.a.f;
            Point screenLocation = projection.toScreenLocation(mc2ll);
            Point point = new Point(screenLocation.x, screenLocation.y - 60);
            projection2 = this.a.f;
            LatLng fromScreenLocation = projection2.fromScreenLocation(point);
            marker3 = this.a.N;
            marker3.setPosition(fromScreenLocation);
            onMarkerDragListener = this.a.A;
            if (onMarkerDragListener != null) {
                marker4 = this.a.N;
                if (marker4.f) {
                    onMarkerDragListener2 = this.a.A;
                    marker5 = this.a.N;
                    onMarkerDragListener2.onMarkerDragEnd(marker5);
                }
            }
            this.a.N = null;
        }
    }
}
