package com.miui.gallery.map.cluster;

/* loaded from: classes2.dex */
public class MapLatLng {
    public float direction;
    public double latitude;
    public double longitude;

    public MapLatLng(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
        this.direction = 0.0f;
    }

    public MapLatLng(double d, double d2, float f) {
        this.latitude = d;
        this.longitude = d2;
        this.direction = f;
    }
}
