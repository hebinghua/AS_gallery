package com.baidu.mapapi.map;

import android.graphics.Point;
import android.graphics.PointF;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkplatform.comapi.map.w;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/* loaded from: classes.dex */
public final class Projection {
    private com.baidu.mapsdkplatform.comapi.map.c a;

    public Projection(com.baidu.mapsdkplatform.comapi.map.c cVar) {
        this.a = cVar;
    }

    public LatLng fromScreenLocation(Point point) {
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        if (point == null || (cVar = this.a) == null) {
            return null;
        }
        return CoordUtil.mc2ll(cVar.b(point.x, point.y));
    }

    public float metersToEquatorPixels(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        return (float) (f / this.a.J());
    }

    public PointF toOpenGLLocation(LatLng latLng, MapStatus mapStatus) {
        if (latLng == null || mapStatus == null) {
            return null;
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        com.baidu.mapsdkplatform.comapi.map.w wVar = mapStatus.a;
        return new PointF((float) (ll2mc.getLongitudeE6() - wVar.d), (float) (ll2mc.getLatitudeE6() - wVar.e));
    }

    public PointF toOpenGLNormalization(LatLng latLng, MapStatus mapStatus) {
        if (latLng == null || mapStatus == null) {
            return null;
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        w.a aVar = mapStatus.a.k;
        return new PointF((float) ((((ll2mc.getLongitudeE6() - aVar.a) * 2.0d) / Math.abs(aVar.b - aVar.a)) - 1.0d), (float) ((((ll2mc.getLatitudeE6() - aVar.d) * 2.0d) / Math.abs(aVar.c - aVar.d)) - 1.0d));
    }

    public Point toScreenLocation(LatLng latLng) {
        if (latLng == null || this.a == null) {
            return null;
        }
        return this.a.a(CoordUtil.ll2mc(latLng));
    }
}
