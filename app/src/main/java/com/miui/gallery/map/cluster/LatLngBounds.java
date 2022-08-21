package com.miui.gallery.map.cluster;

/* loaded from: classes2.dex */
public class LatLngBounds {
    public final MapLatLng northeast;
    public final MapLatLng southwest;

    public LatLngBounds(MapLatLng mapLatLng, MapLatLng mapLatLng2) {
        this.northeast = mapLatLng;
        this.southwest = mapLatLng2;
    }

    public boolean contains(MapLatLng mapLatLng) {
        if (mapLatLng == null) {
            return false;
        }
        MapLatLng mapLatLng2 = this.southwest;
        double d = mapLatLng2.latitude;
        MapLatLng mapLatLng3 = this.northeast;
        double d2 = mapLatLng3.latitude;
        double d3 = mapLatLng2.longitude;
        double d4 = mapLatLng3.longitude;
        double d5 = mapLatLng.latitude;
        double d6 = mapLatLng.longitude;
        return d5 >= d && d5 <= d2 && d6 >= d3 && d6 <= d4;
    }
}
