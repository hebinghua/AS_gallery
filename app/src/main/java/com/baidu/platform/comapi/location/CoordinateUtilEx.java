package com.baidu.platform.comapi.location;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.Point;
import com.baidu.platform.comapi.basestruct.a;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CoordinateUtilEx {
    public static Point Coordinate_encryptEx(float f, float f2, String str) {
        if (str == null) {
            return null;
        }
        if (str.equals("")) {
            str = "bd09ll";
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1395470197:
                if (str.equals("bd09ll")) {
                    c = 0;
                    break;
                }
                break;
            case -1395470175:
                if (str.equals(CoordinateType.BD09MC)) {
                    c = 1;
                    break;
                }
                break;
            case 98175376:
                if (str.equals(CoordinateType.GCJ02)) {
                    c = 2;
                    break;
                }
                break;
            case 113079775:
                if (str.equals(CoordinateType.WGS84)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return CoordinateUtil.bd09llTobd09mc(f, f2);
            case 1:
                return new Point(f, f2);
            case 2:
                return CoordinateUtil.gcj02Tobd09mc(f, f2);
            case 3:
                return CoordinateUtil.wgs84Tobd09mc(f, f2);
            default:
                return null;
        }
    }

    public static ArrayList<Point> Coordinate_encryptExArray(ArrayList<Point> arrayList, String str) {
        int i;
        Point bd09llTobd09mc;
        String str2 = str;
        Point point = null;
        if (str2 == null) {
            return null;
        }
        if (str2.equals("")) {
            str2 = "bd09ll";
        }
        if (!str2.equals("bd09ll") && !str2.equals(CoordinateType.BD09MC) && !str2.equals(CoordinateType.GCJ02) && !str2.equals(CoordinateType.WGS84)) {
            return null;
        }
        int size = arrayList.size();
        float[] fArr = new float[size];
        float[] fArr2 = new float[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            fArr[i2] = arrayList.get(i2).getIntX() / 100000.0f;
            fArr2[i2] = arrayList.get(i2).getIntY() / 100000.0f;
        }
        ArrayList<Point> arrayList2 = new ArrayList<>();
        int i3 = 0;
        while (i3 < size) {
            char c = 65535;
            switch (str2.hashCode()) {
                case -1395470197:
                    if (str2.equals("bd09ll")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1395470175:
                    if (str2.equals(CoordinateType.BD09MC)) {
                        c = 1;
                        break;
                    }
                    break;
                case 98175376:
                    if (str2.equals(CoordinateType.GCJ02)) {
                        c = 2;
                        break;
                    }
                    break;
                case 113079775:
                    if (str2.equals(CoordinateType.WGS84)) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    i = size;
                    bd09llTobd09mc = CoordinateUtil.bd09llTobd09mc(fArr[i3], fArr2[i3]);
                    break;
                case 1:
                    i = size;
                    bd09llTobd09mc = new Point(fArr[i3], fArr2[i3]);
                    break;
                case 2:
                    bd09llTobd09mc = CoordinateUtil.gcj02Tobd09mc(fArr[i3], fArr2[i3]);
                    i = size;
                    break;
                case 3:
                    bd09llTobd09mc = CoordinateUtil.wgs84Tobd09mc(fArr[i3], fArr2[i3]);
                    i = size;
                    break;
                default:
                    bd09llTobd09mc = point;
                    i = size;
                    break;
            }
            if (bd09llTobd09mc != null) {
                arrayList2.add(bd09llTobd09mc);
            }
            i3++;
            size = i;
            point = null;
        }
        return arrayList2;
    }

    public static double getDistanceByMc(GeoPoint geoPoint, GeoPoint geoPoint2) {
        return CoordinateUtil.getDistanceByMc(geoPoint.getLongitude(), geoPoint.getLatitude(), geoPoint2.getLongitude(), geoPoint2.getLatitude());
    }

    public static double getDistanceByMc(Point point, Point point2) {
        return CoordinateUtil.getDistanceByMc(point.getDoubleX(), point.getDoubleY(), point2.getDoubleX(), point2.getDoubleY());
    }

    @Deprecated
    public static a getGeoComplexPointFromString(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return CoordinateUtil.geoStringToComplexPt(str);
    }

    @Deprecated
    public static a getGeoComplexPtBoundFromString(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return CoordinateUtil.geoStringToComplexPtBound(str);
    }

    public static Point getGeoPointFromString(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        return CoordinateUtil.geoStringToPoint(str);
    }

    public static String getStringFromGeoPoint(Point point) {
        return CoordinateUtil.pointToGeoString(point);
    }
}
