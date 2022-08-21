package com.baidu.mapapi.map;

import android.graphics.Point;
import com.baidu.mapapi.map.v;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
public class WeightedLatLng extends v.a {
    public static final double DEFAULT_INTENSITY = 1.0d;
    private Point a;
    public final double intensity;
    public final LatLng latLng;

    public WeightedLatLng(LatLng latLng) {
        this(latLng, 1.0d);
    }

    public WeightedLatLng(LatLng latLng, double d) {
        if (latLng != null) {
            this.latLng = latLng;
            GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
            this.a = new Point((int) ll2mc.getLongitudeE6(), (int) ll2mc.getLatitudeE6());
            if (d > SearchStatUtils.POW) {
                this.intensity = d;
                return;
            } else {
                this.intensity = 1.0d;
                return;
            }
        }
        throw new IllegalArgumentException("BDMapSDKException: latLng can not be null");
    }

    @Override // com.baidu.mapapi.map.v.a
    public Point a() {
        return this.a;
    }
}
