package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.LatLng;

/* loaded from: classes.dex */
public final class ArcOptions extends OverlayOptions {
    private static final String d = "ArcOptions";
    public int a;
    public Bundle c;
    private LatLng g;
    private LatLng h;
    private LatLng i;
    private int e = -16777216;
    private int f = 5;
    public boolean b = true;

    @Override // com.baidu.mapapi.map.OverlayOptions
    public Overlay a() {
        Arc arc = new Arc();
        arc.B = this.b;
        arc.A = this.a;
        arc.C = this.c;
        arc.a = this.e;
        arc.b = this.f;
        arc.c = this.g;
        arc.d = this.h;
        arc.e = this.i;
        return arc;
    }

    public ArcOptions color(int i) {
        this.e = i;
        return this;
    }

    public ArcOptions extraInfo(Bundle bundle) {
        this.c = bundle;
        return this;
    }

    public int getColor() {
        return this.e;
    }

    public LatLng getEndPoint() {
        return this.i;
    }

    public Bundle getExtraInfo() {
        return this.c;
    }

    public LatLng getMiddlePoint() {
        return this.h;
    }

    public LatLng getStartPoint() {
        return this.g;
    }

    public int getWidth() {
        return this.f;
    }

    public int getZIndex() {
        return this.a;
    }

    public boolean isVisible() {
        return this.b;
    }

    public ArcOptions points(LatLng latLng, LatLng latLng2, LatLng latLng3) {
        if (latLng == null || latLng2 == null || latLng3 == null) {
            throw new IllegalArgumentException("BDMapSDKException: start and middle and end points can not be null");
        }
        if (latLng == latLng2 || latLng == latLng3 || latLng2 == latLng3) {
            throw new IllegalArgumentException("BDMapSDKException: start and middle and end points can not be same");
        }
        this.g = latLng;
        this.h = latLng2;
        this.i = latLng3;
        return this;
    }

    public ArcOptions visible(boolean z) {
        this.b = z;
        return this;
    }

    public ArcOptions width(int i) {
        if (i > 0) {
            this.f = i;
        }
        return this;
    }

    public ArcOptions zIndex(int i) {
        this.a = i;
        return this;
    }
}
