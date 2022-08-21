package com.miui.gallery.map.projection;

import com.miui.gallery.map.cluster.MapLatLng;

/* loaded from: classes2.dex */
public class SphericalMercatorProjection {
    public final double mWorldWidth;

    public SphericalMercatorProjection(double d) {
        this.mWorldWidth = d;
    }

    public Point toPoint(MapLatLng mapLatLng) {
        double sin = Math.sin(Math.toRadians(mapLatLng.latitude));
        double d = this.mWorldWidth;
        return new Point(((mapLatLng.longitude / 360.0d) + 0.5d) * d, (((Math.log((sin + 1.0d) / (1.0d - sin)) * 0.5d) / (-6.283185307179586d)) + 0.5d) * d);
    }
}
