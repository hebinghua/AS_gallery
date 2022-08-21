package com.baidu.mapapi.map;

import com.baidu.mapapi.model.LatLng;

/* loaded from: classes.dex */
public final class MultiPointItem {
    private LatLng a;
    private String b;

    public MultiPointItem(LatLng latLng) {
        if (latLng != null) {
            this.a = latLng;
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: MultiPointItem point can not be null");
    }

    public LatLng getPoint() {
        return this.a;
    }

    public String getTitle() {
        return this.b;
    }

    public void setTitle(String str) {
        this.b = str;
    }
}
