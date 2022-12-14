package com.baidu.mapapi.model;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapsdkplatform.comapi.util.CoordTrans;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.Point;
import java.util.List;

/* loaded from: classes.dex */
public class CoordUtil {
    public static LatLng Coordinate_encryptEx(float f, float f2, String str) {
        return com.baidu.mapsdkplatform.comapi.util.b.a(f, f2, str);
    }

    public static LatLng decodeLocation(String str) {
        CoordType coordType = SDKInitializer.getCoordType();
        CoordType coordType2 = CoordType.GCJ02;
        LatLng a = com.baidu.mapsdkplatform.comapi.util.b.a(str);
        return coordType == coordType2 ? CoordTrans.baiduToGcj(a) : a;
    }

    public static List<LatLng> decodeLocationList(String str) {
        return com.baidu.mapsdkplatform.comapi.util.b.c(str);
    }

    public static List<List<LatLng>> decodeLocationList2D(String str) {
        return com.baidu.mapsdkplatform.comapi.util.b.d(str);
    }

    public static LatLng decodeNodeLocation(String str) {
        CoordType coordType = SDKInitializer.getCoordType();
        CoordType coordType2 = CoordType.GCJ02;
        LatLng b = com.baidu.mapsdkplatform.comapi.util.b.b(str);
        return coordType == coordType2 ? CoordTrans.baiduToGcj(b) : b;
    }

    public static double getDistance(Point point, Point point2) {
        return com.baidu.mapsdkplatform.comjni.tools.a.a(point, point2);
    }

    public static int getMCDistanceByOneLatLngAndRadius(LatLng latLng, int i) {
        return SDKInitializer.getCoordType() == CoordType.GCJ02 ? com.baidu.mapsdkplatform.comapi.util.b.a(CoordTrans.gcjToBaidu(latLng), i) : com.baidu.mapsdkplatform.comapi.util.b.a(latLng, i);
    }

    public static GeoPoint ll2mc(LatLng latLng) {
        return SDKInitializer.getCoordType() == CoordType.GCJ02 ? com.baidu.mapsdkplatform.comapi.util.b.a(CoordTrans.gcjToBaidu(latLng)) : com.baidu.mapsdkplatform.comapi.util.b.a(latLng);
    }

    public static Point ll2point(LatLng latLng) {
        return SDKInitializer.getCoordType() == CoordType.GCJ02 ? com.baidu.mapsdkplatform.comapi.util.b.b(CoordTrans.gcjToBaidu(latLng)) : com.baidu.mapsdkplatform.comapi.util.b.b(latLng);
    }

    public static LatLng mc2ll(GeoPoint geoPoint) {
        CoordType coordType = SDKInitializer.getCoordType();
        CoordType coordType2 = CoordType.GCJ02;
        LatLng a = com.baidu.mapsdkplatform.comapi.util.b.a(geoPoint);
        return coordType == coordType2 ? CoordTrans.baiduToGcj(a) : a;
    }
}
