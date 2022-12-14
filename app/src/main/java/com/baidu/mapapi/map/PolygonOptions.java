package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class PolygonOptions extends OverlayOptions {
    public int a;
    public Bundle c;
    private Stroke d;
    private List<LatLng> f;
    private List<HoleOptions> g;
    private HoleOptions h;
    private int e = -16777216;
    private boolean i = false;
    private int j = 0;
    public boolean b = true;

    @Override // com.baidu.mapapi.map.OverlayOptions
    public Overlay a() {
        Polygon polygon = new Polygon();
        polygon.B = this.b;
        polygon.A = this.a;
        polygon.C = this.c;
        List<LatLng> list = this.f;
        if (list == null || list.size() < 2) {
            throw new IllegalStateException("BDMapSDKException: when you add polyline, you must at least supply 2 points");
        }
        polygon.c = this.f;
        polygon.b = this.e;
        polygon.a = this.d;
        polygon.d = this.g;
        polygon.e = this.h;
        polygon.f = this.i;
        polygon.g = this.j;
        return polygon;
    }

    public PolygonOptions addHoleOption(HoleOptions holeOptions) {
        this.h = holeOptions;
        return this;
    }

    public PolygonOptions addHoleOptions(List<HoleOptions> list) {
        this.g = list;
        return this;
    }

    public PolygonOptions dottedStroke(boolean z) {
        this.i = z;
        return this;
    }

    public PolygonOptions dottedStrokeType(PolylineDottedLineType polylineDottedLineType) {
        this.j = polylineDottedLineType.ordinal();
        return this;
    }

    public PolygonOptions extraInfo(Bundle bundle) {
        this.c = bundle;
        return this;
    }

    public PolygonOptions fillColor(int i) {
        this.e = i;
        return this;
    }

    public Bundle getExtraInfo() {
        return this.c;
    }

    public int getFillColor() {
        return this.e;
    }

    public List<LatLng> getPoints() {
        return this.f;
    }

    public Stroke getStroke() {
        return this.d;
    }

    public int getZIndex() {
        return this.a;
    }

    public boolean isVisible() {
        return this.b;
    }

    public PolygonOptions points(List<LatLng> list) {
        if (list != null) {
            if (list.size() <= 2) {
                throw new IllegalArgumentException("BDMapSDKException: points count can not less than three");
            }
            if (list.contains(null)) {
                throw new IllegalArgumentException("BDMapSDKException: points list can not contains null");
            }
            int i = 0;
            while (i < list.size()) {
                int i2 = i + 1;
                for (int i3 = i2; i3 < list.size(); i3++) {
                    if (list.get(i) == list.get(i3)) {
                        throw new IllegalArgumentException("BDMapSDKException: points list can not has same points");
                    }
                }
                i = i2;
            }
            this.f = list;
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: points list can not be null");
    }

    public PolygonOptions stroke(Stroke stroke) {
        this.d = stroke;
        return this;
    }

    public PolygonOptions visible(boolean z) {
        this.b = z;
        return this;
    }

    public PolygonOptions zIndex(int i) {
        this.a = i;
        return this;
    }
}
