package com.baidu.mapsdkplatform.comapi.map;

import com.baidu.mapapi.map.MapLayer;

/* loaded from: classes.dex */
/* synthetic */ class d {
    public static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[MapLayer.values().length];
        a = iArr;
        try {
            iArr[MapLayer.MAP_LAYER_LOCATION.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[MapLayer.MAP_LAYER_OVERLAY.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[MapLayer.MAP_LAYER_INDOOR_POI.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[MapLayer.MAP_LAYER_POI_MARKER.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
    }
}
