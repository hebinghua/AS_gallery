package com.baidu.mapapi.map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.BuildingInfo;
import java.util.List;

/* loaded from: classes.dex */
public class PrismOptions extends OverlayOptions {
    private float b;
    private List<LatLng> c;
    private BuildingInfo f;
    private BitmapDescriptor g;
    private int d = -16777216;
    private int e = -16777216;
    public boolean a = true;

    @Override // com.baidu.mapapi.map.OverlayOptions
    public Overlay a() {
        List<LatLng> list;
        Prism prism = new Prism();
        prism.B = this.a;
        prism.f = this.g;
        prism.a = this.b;
        if (this.f != null || ((list = this.c) != null && list.size() > 3)) {
            prism.b = this.c;
            prism.d = this.e;
            prism.c = this.d;
            prism.e = this.f;
            return prism;
        }
        throw new IllegalStateException("BDMapSDKException: when you add prism, you must at least supply 4 points");
    }

    public PrismOptions customSideImage(BitmapDescriptor bitmapDescriptor) {
        this.g = bitmapDescriptor;
        return this;
    }

    public BuildingInfo getBuildingInfo() {
        return this.f;
    }

    public BitmapDescriptor getCustomSideImage() {
        return this.g;
    }

    public float getHeight() {
        return this.b;
    }

    public List<LatLng> getPoints() {
        return this.c;
    }

    public int getSideFaceColor() {
        return this.e;
    }

    public int getTopFaceColor() {
        return this.d;
    }

    public boolean isVisible() {
        return this.a;
    }

    public PrismOptions setBuildingInfo(BuildingInfo buildingInfo) {
        this.f = buildingInfo;
        return this;
    }

    public PrismOptions setHeight(float f) {
        this.b = f;
        return this;
    }

    public PrismOptions setPoints(List<LatLng> list) {
        this.c = list;
        return this;
    }

    public PrismOptions setSideFaceColor(int i) {
        this.e = i;
        return this;
    }

    public PrismOptions setTopFaceColor(int i) {
        this.d = i;
        return this;
    }

    public PrismOptions visible(boolean z) {
        this.a = z;
        return this;
    }
}
