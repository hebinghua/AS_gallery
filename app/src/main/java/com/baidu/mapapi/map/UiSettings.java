package com.baidu.mapapi.map;

/* loaded from: classes.dex */
public final class UiSettings {
    private com.baidu.mapsdkplatform.comapi.map.c a;

    public UiSettings(com.baidu.mapsdkplatform.comapi.map.c cVar) {
        this.a = cVar;
    }

    public boolean isCompassEnabled() {
        return this.a.r();
    }

    public boolean isOverlookingGesturesEnabled() {
        return this.a.y();
    }

    public boolean isRotateGesturesEnabled() {
        return this.a.x();
    }

    public boolean isScrollGesturesEnabled() {
        return this.a.v();
    }

    public boolean isZoomGesturesEnabled() {
        return this.a.w();
    }

    public void setAllGesturesEnabled(boolean z) {
        setRotateGesturesEnabled(z);
        setScrollGesturesEnabled(z);
        setOverlookingGesturesEnabled(z);
        setZoomGesturesEnabled(z);
        setDoubleClickZoomEnabled(z);
        setTwoTouchClickZoomEnabled(z);
    }

    public void setCompassEnabled(boolean z) {
        this.a.m(z);
    }

    public void setDoubleClickZoomEnabled(boolean z) {
        this.a.t(z);
    }

    public void setEnlargeCenterWithDoubleClickEnable(boolean z) {
        this.a.v(z);
    }

    public void setOverlookingGesturesEnabled(boolean z) {
        this.a.x(z);
    }

    public void setRotateGesturesEnabled(boolean z) {
        this.a.w(z);
    }

    public void setScrollGesturesEnabled(boolean z) {
        this.a.r(z);
    }

    public void setTwoTouchClickZoomEnabled(boolean z) {
        this.a.u(z);
    }

    public void setZoomGesturesEnabled(boolean z) {
        this.a.s(z);
    }
}
