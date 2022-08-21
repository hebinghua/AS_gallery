package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.map.GradientLineOptions;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.List;

/* loaded from: classes.dex */
public class GradientLine extends Overlay {
    public List<LatLng> a;
    public int[] b;
    public int[] c;
    public int d = 5;
    public GradientLineOptions.LineDirectionCross180 e = GradientLineOptions.LineDirectionCross180.NONE;

    public GradientLine() {
        this.type = com.baidu.mapsdkplatform.comapi.map.h.gradientLine;
    }

    private static void a(List<LatLng> list, GradientLineOptions.LineDirectionCross180 lineDirectionCross180, Bundle bundle) {
        int size = list.size();
        double[] dArr = new double[size];
        double[] dArr2 = new double[size];
        for (int i = 0; i < size; i++) {
            LatLng latLng = list.get(i);
            GradientLineOptions.LineDirectionCross180 lineDirectionCross1802 = GradientLineOptions.LineDirectionCross180.FROM_EAST_TO_WEST;
            if (lineDirectionCross180 != lineDirectionCross1802 || latLng.longitude >= SearchStatUtils.POW) {
                GradientLineOptions.LineDirectionCross180 lineDirectionCross1803 = GradientLineOptions.LineDirectionCross180.FROM_WEST_TO_EAST;
                if (lineDirectionCross180 != lineDirectionCross1803 || latLng.longitude <= SearchStatUtils.POW) {
                    bundle.putInt("lineDirectionCross180", GradientLineOptions.LineDirectionCross180.NONE.ordinal());
                } else {
                    LatLng latLng2 = new LatLng(latLng.latitude, latLng.longitude - 360.0d);
                    bundle.putInt("lineDirectionCross180", lineDirectionCross1803.ordinal());
                    latLng = latLng2;
                }
            } else {
                LatLng latLng3 = new LatLng(latLng.latitude, latLng.longitude + 360.0d);
                bundle.putInt("lineDirectionCross180", lineDirectionCross1802.ordinal());
                latLng = latLng3;
            }
            GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
            dArr[i] = ll2mc.getLongitudeE6();
            dArr2[i] = ll2mc.getLatitudeE6();
        }
        bundle.putDoubleArray("x_array", dArr);
        bundle.putDoubleArray("y_array", dArr2);
    }

    private static void a(int[] iArr, Bundle bundle) {
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        bundle.putIntArray("color_indexs", iArr);
    }

    private static void b(int[] iArr, Bundle bundle) {
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        bundle.putIntArray("color_array", iArr);
    }

    @Override // com.baidu.mapapi.map.Overlay
    public Bundle a(Bundle bundle) {
        super.a(bundle);
        List<LatLng> list = this.a;
        if (list == null || list.size() < 2) {
            throw new IllegalStateException("BDMapSDKException: when you add GradientLine, you must at least supply 2 points");
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(this.a.get(0));
        bundle.putDouble("location_x", ll2mc.getLongitudeE6());
        bundle.putDouble("location_y", ll2mc.getLatitudeE6());
        bundle.putInt(nexExportFormat.TAG_FORMAT_WIDTH, this.d);
        int[] iArr = this.b;
        if (iArr == null) {
            throw new IllegalStateException("BDMapSDKException: Indexs array can not be null");
        }
        if (iArr.length == 0) {
            throw new IllegalStateException("BDMapSDKException: Indexs array size can not be Equal to zero");
        }
        a(this.a, this.e, bundle);
        a(this.b, bundle);
        int[] iArr2 = this.c;
        if (iArr2 == null) {
            throw new IllegalStateException("BDMapSDKException: colors array can not be null");
        }
        if (iArr2.length == 0) {
            throw new IllegalStateException("BDMapSDKException: colors array size can not be Equal to zero");
        }
        b(iArr2, bundle);
        return bundle;
    }

    public int[] getColors() {
        return this.c;
    }

    public int[] getIndexs() {
        return this.b;
    }

    public GradientLineOptions.LineDirectionCross180 getLineDirectionCross180() {
        return this.e;
    }

    public List<LatLng> getPoints() {
        return this.a;
    }

    public float getWidth() {
        return this.d;
    }

    public void lineDirectionCross180(GradientLineOptions.LineDirectionCross180 lineDirectionCross180) {
        this.e = lineDirectionCross180;
    }

    public void setColorIndex(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            throw new IllegalArgumentException("BDMapSDKException: indexList can not empty");
        }
        this.b = iArr;
        this.listener.b(this);
    }

    public void setColorList(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            throw new IllegalArgumentException("BDMapSDKException: colorList can not empty");
        }
        this.c = iArr;
        this.listener.b(this);
    }

    public void setPoints(List<LatLng> list) {
        if (list != null) {
            if (list.size() < 2) {
                throw new IllegalArgumentException("BDMapSDKException: points count can not less than 2");
            }
            if (list.contains(null)) {
                throw new IllegalArgumentException("BDMapSDKException: points list can not contains null");
            }
            this.a = list;
            this.listener.b(this);
            return;
        }
        throw new IllegalArgumentException("BDMapSDKException: points list can not be null");
    }

    public void setWidth(int i) {
        if (i > 0) {
            this.d = i;
            this.listener.b(this);
        }
    }
}
