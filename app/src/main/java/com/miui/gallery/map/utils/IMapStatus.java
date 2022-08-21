package com.miui.gallery.map.utils;

import com.miui.gallery.map.cluster.LatLngBounds;
import com.miui.gallery.map.cluster.MapLatLng;

/* loaded from: classes2.dex */
public interface IMapStatus {
    LatLngBounds getBound();

    MapLatLng getTarget();

    float getZoomLevel();
}
