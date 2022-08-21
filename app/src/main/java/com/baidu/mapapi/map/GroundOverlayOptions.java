package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

/* loaded from: classes.dex */
public final class GroundOverlayOptions extends OverlayOptions {
    public int a;
    public Bundle c;
    private BitmapDescriptor d;
    private LatLng e;
    private int f;
    private int g;
    private LatLngBounds j;
    private float h = 0.5f;
    private float i = 0.5f;
    private float k = 1.0f;
    public boolean b = true;

    @Override // com.baidu.mapapi.map.OverlayOptions
    public Overlay a() {
        int i;
        LatLng latLng;
        int i2;
        GroundOverlay groundOverlay = new GroundOverlay();
        groundOverlay.B = this.b;
        groundOverlay.A = this.a;
        groundOverlay.C = this.c;
        BitmapDescriptor bitmapDescriptor = this.d;
        if (bitmapDescriptor != null) {
            groundOverlay.b = bitmapDescriptor;
            LatLngBounds latLngBounds = this.j;
            if (latLngBounds == null && (latLng = this.e) != null) {
                int i3 = this.f;
                if (i3 <= 0 || (i2 = this.g) <= 0) {
                    throw new IllegalArgumentException("BDMapSDKException: when you add ground overlay, the width and height must greater than 0");
                }
                groundOverlay.c = latLng;
                groundOverlay.f = this.h;
                groundOverlay.g = this.i;
                groundOverlay.d = i3;
                groundOverlay.e = i2;
                i = 2;
            } else if (this.e != null || latLngBounds == null) {
                throw new IllegalStateException("BDMapSDKException: when you add ground overlay, you must set one of position or bounds");
            } else {
                groundOverlay.h = latLngBounds;
                i = 1;
            }
            groundOverlay.a = i;
            groundOverlay.i = this.k;
            return groundOverlay;
        }
        throw new IllegalStateException("BDMapSDKException: when you add ground overlay, you must set the image");
    }

    public GroundOverlayOptions anchor(float f, float f2) {
        if (f >= 0.0f && f <= 1.0f && f2 >= 0.0f && f2 <= 1.0f) {
            this.h = f;
            this.i = f2;
        }
        return this;
    }

    public GroundOverlayOptions dimensions(int i) {
        this.f = i;
        this.g = Integer.MAX_VALUE;
        return this;
    }

    public GroundOverlayOptions dimensions(int i, int i2) {
        this.f = i;
        this.g = i2;
        return this;
    }

    public GroundOverlayOptions extraInfo(Bundle bundle) {
        this.c = bundle;
        return this;
    }

    public float getAnchorX() {
        return this.h;
    }

    public float getAnchorY() {
        return this.i;
    }

    public LatLngBounds getBounds() {
        return this.j;
    }

    public Bundle getExtraInfo() {
        return this.c;
    }

    public int getHeight() {
        int i = this.g;
        return i == Integer.MAX_VALUE ? (int) ((this.f * this.d.a.getHeight()) / this.d.a.getWidth()) : i;
    }

    public BitmapDescriptor getImage() {
        return this.d;
    }

    public LatLng getPosition() {
        return this.e;
    }

    public float getTransparency() {
        return this.k;
    }

    public int getWidth() {
        return this.f;
    }

    public int getZIndex() {
        return this.a;
    }

    public GroundOverlayOptions image(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor != null) {
            this.d = bitmapDescriptor;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: image can not be null");
    }

    public boolean isVisible() {
        return this.b;
    }

    public GroundOverlayOptions position(LatLng latLng) {
        if (latLng != null) {
            this.e = latLng;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: position can not be null");
    }

    public GroundOverlayOptions positionFromBounds(LatLngBounds latLngBounds) {
        if (latLngBounds != null) {
            this.j = latLngBounds;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: bounds can not be null");
    }

    public GroundOverlayOptions transparency(float f) {
        if (f <= 1.0f && f >= 0.0f) {
            this.k = f;
        }
        return this;
    }

    public GroundOverlayOptions visible(boolean z) {
        this.b = z;
        return this;
    }

    public GroundOverlayOptions zIndex(int i) {
        this.a = i;
        return this;
    }
}
