package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.LatLng;
import java.util.List;

/* loaded from: classes.dex */
public final class Polygon extends Overlay {
    public Stroke a;
    public int b;
    public List<LatLng> c;
    public List<HoleOptions> d;
    public HoleOptions e;
    public boolean f;
    public int g = 0;

    public Polygon() {
        this.type = com.baidu.mapsdkplatform.comapi.map.h.polygon;
    }

    private void b(Bundle bundle) {
        BitmapDescriptor fromAsset = BitmapDescriptorFactory.fromAsset(this.g == 1 ? "CircleDashTexture.png" : "lineDashTexture.png");
        if (fromAsset != null) {
            bundle.putBundle("image_info", fromAsset.b());
        }
    }

    private void c(List<HoleOptions> list, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        boolean b = Overlay.b(list, bundle2);
        bundle.putInt("has_holes", b ? 1 : 0);
        if (b) {
            bundle.putBundle("holes", bundle2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x008d  */
    @Override // com.baidu.mapapi.map.Overlay
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.os.Bundle a(android.os.Bundle r7) {
        /*
            r6 = this;
            super.a(r7)
            java.util.List<com.baidu.mapapi.model.LatLng> r0 = r6.c
            r1 = 0
            java.lang.Object r0 = r0.get(r1)
            com.baidu.mapapi.model.LatLng r0 = (com.baidu.mapapi.model.LatLng) r0
            com.baidu.platform.comapi.basestruct.GeoPoint r0 = com.baidu.mapapi.model.CoordUtil.ll2mc(r0)
            double r2 = r0.getLongitudeE6()
            java.lang.String r4 = "location_x"
            r7.putDouble(r4, r2)
            double r2 = r0.getLatitudeE6()
            java.lang.String r4 = "location_y"
            r7.putDouble(r4, r2)
            java.util.List<com.baidu.mapapi.model.LatLng> r2 = r6.c
            com.baidu.mapapi.map.Overlay.a(r2, r7)
            int r2 = r6.b
            com.baidu.mapapi.map.Overlay.a(r2, r7)
            com.baidu.mapapi.map.Stroke r2 = r6.a
            r3 = 1
            java.lang.String r4 = "has_stroke"
            if (r2 != 0) goto L37
            r7.putInt(r4, r1)
            goto L4a
        L37:
            r7.putInt(r4, r3)
            android.os.Bundle r2 = new android.os.Bundle
            r2.<init>()
            com.baidu.mapapi.map.Stroke r4 = r6.a
            android.os.Bundle r2 = r4.a(r2)
            java.lang.String r4 = "stroke"
            r7.putBundle(r4, r2)
        L4a:
            java.util.List<com.baidu.mapapi.map.HoleOptions> r2 = r6.d
            if (r2 == 0) goto L57
            int r2 = r2.size()
            if (r2 == 0) goto L57
            java.util.List<com.baidu.mapapi.map.HoleOptions> r2 = r6.d
            goto L65
        L57:
            com.baidu.mapapi.map.HoleOptions r2 = r6.e
            if (r2 == 0) goto L69
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            com.baidu.mapapi.map.HoleOptions r4 = r6.e
            r2.add(r4)
        L65:
            r6.c(r2, r7)
            goto L6e
        L69:
            java.lang.String r2 = "has_holes"
            r7.putInt(r2, r1)
        L6e:
            boolean r2 = r6.f
            java.lang.String r4 = "has_dotted_stroke"
            if (r2 == 0) goto L8d
            double r1 = r0.getLongitudeE6()
            java.lang.String r5 = "dotted_stroke_location_x"
            r7.putDouble(r5, r1)
            double r0 = r0.getLatitudeE6()
            java.lang.String r2 = "dotted_stroke_location_y"
            r7.putDouble(r2, r0)
            r7.putInt(r4, r3)
            r6.b(r7)
            goto L90
        L8d:
            r7.putInt(r4, r1)
        L90:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.map.Polygon.a(android.os.Bundle):android.os.Bundle");
    }

    public int getFillColor() {
        return this.b;
    }

    public HoleOptions getHoleOption() {
        return this.e;
    }

    public List<HoleOptions> getHoleOptions() {
        return this.d;
    }

    public List<LatLng> getPoints() {
        return this.c;
    }

    public Stroke getStroke() {
        return this.a;
    }

    public void setFillColor(int i) {
        this.b = i;
        this.listener.b(this);
    }

    public void setHoleOption(HoleOptions holeOptions) {
        this.e = holeOptions;
        this.d = null;
        this.listener.b(this);
    }

    public void setHoleOptions(List<HoleOptions> list) {
        this.d = list;
        this.e = null;
        this.listener.b(this);
    }

    public void setPoints(List<LatLng> list) {
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
            this.c = list;
            this.listener.b(this);
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: points list can not be null");
    }

    public void setStroke(Stroke stroke) {
        this.a = stroke;
        this.listener.b(this);
    }
}
