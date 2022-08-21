package com.miui.gallery.map.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.miui.gallery.map.cluster.MapLatLng;

/* loaded from: classes2.dex */
public class LocationConverter {
    public static final CoordinateConverter BDCoordinateConverter = new CoordinateConverter().from(CoordinateConverter.CoordType.GPS);

    public static MapLatLng convertToMapLatLng(double d, double d2) {
        LatLng convert = BDCoordinateConverter.coord(new LatLng(d, d2)).convert();
        return new MapLatLng(convert.latitude, convert.longitude);
    }
}
