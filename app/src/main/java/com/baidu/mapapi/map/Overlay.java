package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Overlay {
    public int A;
    public boolean B;
    public Bundle C;
    public a listener;
    public com.baidu.mapsdkplatform.comapi.map.h type;
    public String z = System.currentTimeMillis() + "_" + hashCode();

    /* loaded from: classes.dex */
    public interface a {
        void a(Overlay overlay);

        void b(Overlay overlay);

        boolean c(Overlay overlay);
    }

    public static void a(int i, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putFloat("red", ((i >> 16) & 255) / 255.0f);
        bundle2.putFloat("green", ((i >> 8) & 255) / 255.0f);
        bundle2.putFloat("blue", (i & 255) / 255.0f);
        bundle2.putFloat("alpha", (i >>> 24) / 255.0f);
        bundle.putBundle("color", bundle2);
    }

    public static void a(List<LatLng> list, Bundle bundle) {
        int size = list.size();
        double[] dArr = new double[size];
        double[] dArr2 = new double[size];
        for (int i = 0; i < size; i++) {
            GeoPoint ll2mc = CoordUtil.ll2mc(list.get(i));
            dArr[i] = ll2mc.getLongitudeE6();
            dArr2[i] = ll2mc.getLatitudeE6();
        }
        bundle.putDoubleArray("x_array", dArr);
        bundle.putDoubleArray("y_array", dArr2);
    }

    public static void b(int i, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putFloat("red", ((i >> 16) & 255) / 255.0f);
        bundle2.putFloat("green", ((i >> 8) & 255) / 255.0f);
        bundle2.putFloat("blue", (i & 255) / 255.0f);
        bundle2.putFloat("alpha", (i >>> 24) / 255.0f);
        bundle.putBundle("m_topFaceColor", bundle2);
    }

    public static boolean b(List<HoleOptions> list, Bundle bundle) {
        boolean z;
        boolean z2;
        if (list == null || list.size() == 0) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (HoleOptions holeOptions : list) {
            if (holeOptions instanceof CircleHoleOptions) {
                arrayList.add((CircleHoleOptions) holeOptions);
            } else if (holeOptions instanceof PolygonHoleOptions) {
                arrayList2.add((PolygonHoleOptions) holeOptions);
            }
        }
        if (arrayList.size() != 0) {
            z = c(arrayList, bundle);
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            bundle.putInt("has_circle_hole", i);
        } else {
            bundle.putInt("has_circle_hole", 0);
            z = false;
        }
        if (arrayList2.size() != 0) {
            z2 = d(arrayList2, bundle);
            int i3 = z2 ? 1 : 0;
            int i4 = z2 ? 1 : 0;
            bundle.putInt("has_polygon_hole", i3);
        } else {
            bundle.putInt("has_polygon_hole", 0);
            z2 = false;
        }
        return z || z2;
    }

    public static void c(int i, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putFloat("red", ((i >> 16) & 255) / 255.0f);
        bundle2.putFloat("green", ((i >> 8) & 255) / 255.0f);
        bundle2.putFloat("blue", (i & 255) / 255.0f);
        bundle2.putFloat("alpha", (i >>> 24) / 255.0f);
        bundle.putBundle("m_sideFaceColor", bundle2);
    }

    private static boolean c(List<CircleHoleOptions> list, Bundle bundle) {
        int size = list.size();
        double[] dArr = new double[size];
        double[] dArr2 = new double[size];
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            LatLng holeCenter = list.get(i).getHoleCenter();
            int holeRadius = list.get(i).getHoleRadius();
            if (holeCenter == null || holeRadius <= 0) {
                return false;
            }
            GeoPoint ll2mc = CoordUtil.ll2mc(holeCenter);
            dArr[i] = ll2mc.getLongitudeE6();
            dArr2[i] = ll2mc.getLatitudeE6();
            iArr[i] = holeRadius;
        }
        bundle.putDoubleArray("circle_hole_x_array", dArr);
        bundle.putDoubleArray("circle_hole_y_array", dArr2);
        bundle.putIntArray("circle_hole_radius_array", iArr);
        return true;
    }

    private static boolean d(List<PolygonHoleOptions> list, Bundle bundle) {
        int size = list.size();
        int[] iArr = new int[size];
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            List<LatLng> holePoints = list.get(i).getHolePoints();
            if (holePoints == null) {
                return false;
            }
            arrayList.addAll(holePoints);
            iArr[i] = holePoints.size();
        }
        int size2 = arrayList.size();
        if (size2 == 0) {
            return false;
        }
        bundle.putIntArray("polygon_hole_count_array", iArr);
        double[] dArr = new double[size2];
        double[] dArr2 = new double[size2];
        for (int i2 = 0; i2 < size2; i2++) {
            GeoPoint ll2mc = CoordUtil.ll2mc((LatLng) arrayList.get(i2));
            dArr[i2] = ll2mc.getLongitudeE6();
            dArr2[i2] = ll2mc.getLatitudeE6();
        }
        bundle.putDoubleArray("polygon_hole_x_array", dArr);
        bundle.putDoubleArray("polygon_hole_y_array", dArr2);
        return true;
    }

    public Bundle a() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.z);
        bundle.putInt(nexExportFormat.TAG_FORMAT_TYPE, this.type.ordinal());
        return bundle;
    }

    public Bundle a(Bundle bundle) {
        bundle.putString("id", this.z);
        bundle.putInt(nexExportFormat.TAG_FORMAT_TYPE, this.type.ordinal());
        bundle.putInt("visibility", this.B ? 1 : 0);
        bundle.putInt("z_index", this.A);
        return bundle;
    }

    public Bundle getExtraInfo() {
        return this.C;
    }

    public int getZIndex() {
        return this.A;
    }

    public boolean isRemoved() {
        return this.listener.c(this);
    }

    public boolean isVisible() {
        return this.B;
    }

    public void remove() {
        this.listener.a(this);
    }

    public void setExtraInfo(Bundle bundle) {
        this.C = bundle;
    }

    public void setVisible(boolean z) {
        this.B = z;
        this.listener.b(this);
    }

    public void setZIndex(int i) {
        this.A = i;
        this.listener.b(this);
    }
}
