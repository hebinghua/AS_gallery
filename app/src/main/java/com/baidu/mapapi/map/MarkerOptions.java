package com.baidu.mapapi.map;

import android.graphics.Point;
import android.os.Bundle;
import com.baidu.mapapi.model.LatLng;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class MarkerOptions extends OverlayOptions {
    public int a;
    public Bundle c;
    private LatLng d;
    private BitmapDescriptor e;
    private float j;
    private String k;
    private int l;
    private ArrayList<BitmapDescriptor> n;
    private Point u;
    private InfoWindow w;
    private float f = 0.5f;
    private float g = 1.0f;
    private boolean h = true;
    private boolean i = false;
    private boolean m = false;
    private int o = 20;
    private float p = 1.0f;
    private float q = 1.0f;
    private float r = 1.0f;
    private int s = MarkerAnimateType.none.ordinal();
    private boolean t = false;
    private boolean v = true;
    public boolean b = true;

    /* loaded from: classes.dex */
    public enum MarkerAnimateType {
        none,
        drop,
        grow,
        jump
    }

    @Override // com.baidu.mapapi.map.OverlayOptions
    public Overlay a() {
        Marker marker = new Marker();
        marker.B = this.b;
        marker.A = this.a;
        marker.C = this.c;
        LatLng latLng = this.d;
        if (latLng != null) {
            marker.a = latLng;
            BitmapDescriptor bitmapDescriptor = this.e;
            if (bitmapDescriptor == null && this.n == null) {
                throw new IllegalStateException("BDMapSDKException: when you add marker, you must set the icon or icons");
            }
            marker.b = bitmapDescriptor;
            marker.c = this.f;
            marker.d = this.g;
            marker.e = this.h;
            marker.f = this.i;
            marker.g = this.j;
            marker.h = this.k;
            marker.i = this.l;
            marker.j = this.m;
            marker.p = this.n;
            marker.q = this.o;
            marker.l = this.r;
            marker.s = this.p;
            marker.t = this.q;
            marker.m = this.s;
            marker.n = this.t;
            marker.w = this.w;
            marker.o = this.v;
            Point point = this.u;
            if (point != null) {
                marker.v = point;
            }
            return marker;
        }
        throw new IllegalStateException("BDMapSDKException: when you add marker, you must set the position");
    }

    public MarkerOptions alpha(float f) {
        if (f < 0.0f || f > 1.0f) {
            this.r = 1.0f;
            return this;
        }
        this.r = f;
        return this;
    }

    public MarkerOptions anchor(float f, float f2) {
        if (f >= 0.0f && f <= 1.0f && f2 >= 0.0f && f2 <= 1.0f) {
            this.f = f;
            this.g = f2;
        }
        return this;
    }

    public MarkerOptions animateType(MarkerAnimateType markerAnimateType) {
        if (markerAnimateType == null) {
            markerAnimateType = MarkerAnimateType.none;
        }
        this.s = markerAnimateType.ordinal();
        return this;
    }

    public MarkerOptions clickable(boolean z) {
        this.v = z;
        return this;
    }

    public MarkerOptions draggable(boolean z) {
        this.i = z;
        return this;
    }

    public MarkerOptions extraInfo(Bundle bundle) {
        this.c = bundle;
        return this;
    }

    public MarkerOptions fixedScreenPosition(Point point) {
        this.u = point;
        this.t = true;
        return this;
    }

    public MarkerOptions flat(boolean z) {
        this.m = z;
        return this;
    }

    public float getAlpha() {
        return this.r;
    }

    public float getAnchorX() {
        return this.f;
    }

    public float getAnchorY() {
        return this.g;
    }

    public MarkerAnimateType getAnimateType() {
        int i = this.s;
        return i != 1 ? i != 2 ? i != 3 ? MarkerAnimateType.none : MarkerAnimateType.jump : MarkerAnimateType.grow : MarkerAnimateType.drop;
    }

    public Bundle getExtraInfo() {
        return this.c;
    }

    public BitmapDescriptor getIcon() {
        return this.e;
    }

    public ArrayList<BitmapDescriptor> getIcons() {
        return this.n;
    }

    public int getPeriod() {
        return this.o;
    }

    public LatLng getPosition() {
        return this.d;
    }

    public float getRotate() {
        return this.j;
    }

    @Deprecated
    public String getTitle() {
        return this.k;
    }

    public int getZIndex() {
        return this.a;
    }

    public MarkerOptions icon(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor != null) {
            this.e = bitmapDescriptor;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: marker's icon can not be null");
    }

    public MarkerOptions icons(ArrayList<BitmapDescriptor> arrayList) {
        if (arrayList != null) {
            if (arrayList.size() == 0) {
                return this;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) == null || arrayList.get(i).a == null) {
                    return this;
                }
            }
            this.n = arrayList;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: marker's icons can not be null");
    }

    public MarkerOptions infoWindow(InfoWindow infoWindow) {
        this.w = infoWindow;
        return this;
    }

    public boolean isDraggable() {
        return this.i;
    }

    public boolean isFlat() {
        return this.m;
    }

    public boolean isPerspective() {
        return this.h;
    }

    public boolean isVisible() {
        return this.b;
    }

    public MarkerOptions period(int i) {
        if (i > 0) {
            this.o = i;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: marker's period must be greater than zero ");
    }

    public MarkerOptions perspective(boolean z) {
        this.h = z;
        return this;
    }

    public MarkerOptions position(LatLng latLng) {
        if (latLng != null) {
            this.d = latLng;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: marker's position can not be null");
    }

    public MarkerOptions rotate(float f) {
        while (f < 0.0f) {
            f += 360.0f;
        }
        this.j = f % 360.0f;
        return this;
    }

    public MarkerOptions scaleX(float f) {
        if (f < 0.0f) {
            return this;
        }
        this.p = f;
        return this;
    }

    public MarkerOptions scaleY(float f) {
        if (f < 0.0f) {
            return this;
        }
        this.q = f;
        return this;
    }

    @Deprecated
    public MarkerOptions title(String str) {
        this.k = str;
        return this;
    }

    public MarkerOptions visible(boolean z) {
        this.b = z;
        return this;
    }

    public MarkerOptions yOffset(int i) {
        this.l = i;
        return this;
    }

    public MarkerOptions zIndex(int i) {
        this.a = i;
        return this;
    }
}
