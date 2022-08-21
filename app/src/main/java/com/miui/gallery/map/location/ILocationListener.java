package com.miui.gallery.map.location;

import com.miui.gallery.map.cluster.MapLatLng;

/* loaded from: classes2.dex */
public interface ILocationListener {
    void onReceiveLocationFailed(int i);

    void onReceiveLocationSuccess(MapLatLng mapLatLng);
}
