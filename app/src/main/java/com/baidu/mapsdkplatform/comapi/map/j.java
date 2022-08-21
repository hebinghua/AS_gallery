package com.baidu.mapsdkplatform.comapi.map;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.basestruct.Point;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class j {
    private static double a(double d) {
        return (d * 3.141592653589793d) / 180.0d;
    }

    public static double a(LatLng latLng, LatLng latLng2) {
        if (latLng != null && latLng2 != null) {
            Point ll2point = CoordUtil.ll2point(latLng);
            Point ll2point2 = CoordUtil.ll2point(latLng2);
            if (ll2point != null && ll2point2 != null) {
                return CoordUtil.getDistance(ll2point, ll2point2);
            }
        }
        return -1.0d;
    }

    private static LatLng a(LatLng latLng, LatLng latLng2, double d, double d2) {
        double d3 = latLng.latitude;
        double d4 = latLng2.latitude;
        double d5 = latLng.longitude;
        double d6 = latLng2.longitude;
        double sin = Math.sin((1.0d - d) * d2) / Math.sin(d2);
        double sin2 = Math.sin(d * d2) / Math.sin(d2);
        double a = a(d3);
        double a2 = a(d4);
        double a3 = a(d5);
        double a4 = a(d6);
        double cos = (Math.cos(a) * sin * Math.cos(a3)) + (Math.cos(a2) * sin2 * Math.cos(a4));
        double cos2 = (Math.cos(a) * sin * Math.sin(a3)) + (Math.cos(a2) * sin2 * Math.sin(a4));
        return new LatLng(b(Math.atan2((sin * Math.sin(a)) + (sin2 * Math.sin(a2)), Math.sqrt(Math.pow(cos, 2.0d) + Math.pow(cos2, 2.0d)))), b(Math.atan2(cos2, cos)));
    }

    private static double b(double d) {
        return (d / 3.141592653589793d) * 180.0d;
    }

    public static List<LatLng> b(LatLng latLng, LatLng latLng2) {
        double a = a(latLng, latLng2);
        ArrayList arrayList = new ArrayList();
        if (150000.0d > a || a < 250000.0d) {
            arrayList.add(latLng);
            arrayList.add(latLng2);
            return arrayList;
        }
        double round = Math.round(a / 150000.0d);
        double c = c(latLng, latLng2);
        arrayList.add(latLng);
        for (double d = 0.0d; d < round; d += 1.0d) {
            arrayList.add(a(latLng, latLng2, d / round, c));
        }
        arrayList.add(latLng2);
        return arrayList;
    }

    private static double c(LatLng latLng, LatLng latLng2) {
        double a = a(latLng.latitude);
        double a2 = a(latLng2.latitude);
        double a3 = a(latLng.longitude);
        return Math.acos((Math.sin(a) * Math.sin(a2)) + (Math.cos(a) * Math.cos(a2) * Math.cos(Math.abs(a(latLng2.longitude) - a3))));
    }
}
